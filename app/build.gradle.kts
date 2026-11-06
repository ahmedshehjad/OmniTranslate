import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

val releaseKeystorePath = providers.environmentVariable("KEYSTORE_PATH").orNull
val releaseStorePassword = providers.environmentVariable("OMNITRANSLATE_STORE_PASSWORD").orNull
  ?: providers.environmentVariable("STORE_PASSWORD").orNull
val releaseKeyPassword = providers.environmentVariable("OMNITRANSLATE_KEY_PASSWORD").orNull
  ?: providers.environmentVariable("KEY_PASSWORD").orNull
val releaseKeyAlias = providers.environmentVariable("OMNITRANSLATE_KEY_ALIAS").orNull
  ?: providers.environmentVariable("KEY_ALIAS").orNull

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.omnitranslate.qkvzpt"
    minSdk = 24
    targetSdk = 36
    versionCode = 2
    versionName = "1.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  // Keep release signing optional during Gradle configuration so debug builds and unit tests
  // can run without production credentials. Release tasks are blocked by validateReleaseSigning.
  signingConfigs {
    create("release") {
      if (!releaseKeystorePath.isNullOrBlank()) storeFile = file(releaseKeystorePath)
      if (!releaseStorePassword.isNullOrBlank()) storePassword = releaseStorePassword
      if (!releaseKeyPassword.isNullOrBlank()) keyPassword = releaseKeyPassword
      if (!releaseKeyAlias.isNullOrBlank()) keyAlias = releaseKeyAlias
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  lint {
    abortOnError = false
    checkReleaseBuilds = false
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      if (!releaseKeystorePath.isNullOrBlank() &&
          !releaseStorePassword.isNullOrBlank() &&
          !releaseKeyPassword.isNullOrBlank() &&
          !releaseKeyAlias.isNullOrBlank()) {
        signingConfig = signingConfigs.getByName("release")
      }
    }
    debug {
      signingConfig = signingConfigs.getByName("debugConfig")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "META-INF/INDEX.LIST"
      excludes += "META-INF/DEPENDENCIES"
    }
  }
}

// Fail closed only when a release task is requested; debug builds/tests need no release secrets.
val validateReleaseSigning = tasks.register("validateReleaseSigning") {
  group = "verification"
  description = "Checks production signing credentials before any release artifact is built."
  doLast {
    val required = mapOf(
      "KEYSTORE_PATH" to releaseKeystorePath,
      "OMNITRANSLATE_STORE_PASSWORD" to releaseStorePassword,
      "OMNITRANSLATE_KEY_PASSWORD" to releaseKeyPassword,
      "OMNITRANSLATE_KEY_ALIAS" to releaseKeyAlias
    )
    val missing = required.filterValues { it.isNullOrBlank() }.keys
    if (missing.isNotEmpty()) {
      throw GradleException("Missing required release signing environment variables: ${missing.joinToString()}")
    }
    val keystore = file(releaseKeystorePath!!)
    if (!keystore.isFile || keystore.length() == 0L) {
      throw GradleException("Release keystore file is missing or empty: ${keystore.absolutePath}")
    }
  }
}

tasks.configureEach {
  if (name == "preReleaseBuild" || name == "bundleRelease") {
    dependsOn(validateReleaseSigning)
  }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.mlkit.translate)
  implementation(libs.mlkit.language.id)
  implementation(libs.mlkit.text.recognition)
  implementation(libs.play.services.ads)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.firebase.appcheck.debug)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
