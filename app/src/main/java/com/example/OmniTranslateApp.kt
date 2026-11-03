package com.example

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class OmniTranslateApp : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            if (BuildConfig.DEBUG) {
                // Test-device configuration belongs in debug builds only.
                val testDeviceIds = listOf(
                    AdRequest.DEVICE_ID_EMULATOR,
                    "46043d5e-bd7b-421e-9b16-9e6be6f28d7a"
                )
                val configuration = RequestConfiguration.Builder()
                    .setTestDeviceIds(testDeviceIds)
                    .build()
                MobileAds.setRequestConfiguration(configuration)
            }

            MobileAds.initialize(this) {}
        } catch (e: Exception) {
            Log.e("OmniTranslateApp", "Error initializing Mobile Ads", e)
        }
    }
}
