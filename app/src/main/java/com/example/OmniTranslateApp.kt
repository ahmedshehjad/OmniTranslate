package com.example

import android.app.Application
import android.provider.Settings
import android.util.Log
import com.example.data.AppDatabase
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.security.MessageDigest

class OmniTranslateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Room DB
        AppDatabase.getInstance(this)

        // Automatically detect and configure this device as an official AdMob Test Device
        try {
            val testDeviceIds = mutableListOf(
                AdRequest.DEVICE_ID_EMULATOR,
                "46043d5e-bd7b-421e-9b16-9e6be6f28d7a"
            )

            // Dynamically calculate the MD5 hash of this physical device's Android ID
            val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            if (!androidId.isNullOrBlank()) {
                val md = MessageDigest.getInstance("MD5")
                val digest = md.digest(androidId.toByteArray())
                val hexUpper = digest.joinToString("") { "%02X".format(it) }
                val hexLower = digest.joinToString("") { "%02x".format(it) }
                testDeviceIds.add(hexUpper)
                testDeviceIds.add(hexLower)
                testDeviceIds.add(androidId)
                Log.d("AdMobTestDevice", "Registered Test Device ID: $hexUpper")
            }

            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
            MobileAds.setRequestConfiguration(configuration)

            // Initialize MobileAds SDK
            MobileAds.initialize(this) {}
        } catch (e: Exception) {
            Log.e("OmniTranslateApp", "Error configuring test device", e)
        }
    }
}
