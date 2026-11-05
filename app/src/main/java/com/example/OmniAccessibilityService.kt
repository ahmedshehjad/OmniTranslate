package com.example

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.data.AppDatabase
import com.example.data.ClipboardItem
import com.example.engine.TranslationEngine
import com.example.engine.TtsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OmniAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var lastProcessedText: String = ""
    private var lastProcessedTime: Long = 0L
    private var ttsManager: TtsManager? = null

    companion object {
        private const val TAG = "OmniAccessibility"

        private val _isConnected = MutableStateFlow(false)
        val isConnected = _isConnected.asStateFlow()

        private val _isScreenInspectionEnabled = MutableStateFlow(true)
        val isScreenInspectionEnabled = _isScreenInspectionEnabled.asStateFlow()

        private val _isAutoSpeakEnabled = MutableStateFlow(true)
        val isAutoSpeakEnabled = _isAutoSpeakEnabled.asStateFlow()

        private val _lastDetectedText = MutableStateFlow("")
        val lastDetectedText = _lastDetectedText.asStateFlow()

        private val _lastTranslatedText = MutableStateFlow("")
        val lastTranslatedText = _lastTranslatedText.asStateFlow()

        fun setScreenInspectionEnabled(enabled: Boolean) {
            _isScreenInspectionEnabled.value = enabled
        }

        fun setAutoSpeakEnabled(enabled: Boolean) {
            _isAutoSpeakEnabled.value = enabled
        }

        fun isAccessibilityServiceEnabled(context: Context): Boolean {
            val expectedComponentName = ComponentName(context, OmniAccessibilityService::class.java)
            val enabledServicesSetting = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

            val colonSplitter = TextUtils.SimpleStringSplitter(':')
            colonSplitter.setString(enabledServicesSetting)

            while (colonSplitter.hasNext()) {
                val componentNameString = colonSplitter.next()
                val enabledComponent = ComponentName.unflattenFromString(componentNameString)
                if (enabledComponent != null && enabledComponent == expectedComponentName) {
                    return true
                }
            }
            return false
        }

        fun openAccessibilitySettings(context: Context) {
            try {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to open accessibility settings", e)
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        _isConnected.value = true
        Log.d(TAG, "OmniAccessibilityService connected")

        // Initialize Text-to-Speech engine
        ttsManager = TtsManager(applicationContext)

        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or
                    AccessibilityEvent.TYPE_VIEW_LONG_CLICKED or
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED or
                    AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED or
                    AccessibilityEvent.TYPE_VIEW_SELECTED or
                    AccessibilityEvent.TYPE_VIEW_FOCUSED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            notificationTimeout = 100
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
        this.serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || !_isScreenInspectionEnabled.value) return

        // Skip events originated by our own application to avoid recursive loops
        val eventPackage = event.packageName?.toString() ?: ""
        if (eventPackage == packageName) return

        val eventType = event.eventType
        val isLongPressOrSelection = eventType == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED ||
                eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED

        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED ||
            eventType == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED ||
            eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ||
            eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED ||
            eventType == AccessibilityEvent.TYPE_VIEW_SELECTED ||
            eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED
        ) {
            val source = event.source ?: return
            extractTextAndProcess(source, isLongPressOrSelection)
            safeRecycle(source)
        }
    }

    private fun extractTextAndProcess(nodeInfo: AccessibilityNodeInfo?, isLongPress: Boolean) {
        if (nodeInfo == null) return

        val text = nodeInfo.text?.toString()?.trim()
        if (!text.isNullOrEmpty() && text.length >= 3) {
            processDetectedText(text, isLongPress)
        }

        val childCount = nodeInfo.childCount
        for (i in 0 until childCount) {
            val child = try {
                nodeInfo.getChild(i)
            } catch (e: Exception) {
                null
            } ?: continue

            extractTextAndProcess(child, isLongPress)
            safeRecycle(child)
        }
    }

    private fun processDetectedText(text: String, isLongPress: Boolean) {
        val now = System.currentTimeMillis()
        // Debounce: Ignore duplicate text detected within 2 seconds
        if (text == lastProcessedText && now - lastProcessedTime < 2000L) {
            return
        }

        lastProcessedText = text
        lastProcessedTime = now
        _lastDetectedText.value = text
        Log.d(TAG, "Extracted text: $text (isLongPress: $isLongPress)")

        serviceScope.launch {
            try {
                // Auto identify source language or default to English
                val detectedLang = TranslationEngine.identifyLanguage(text) ?: "en"
                val translationResult = TranslationEngine.translate(text, detectedLang, "bn")
                val translated = translationResult.getOrNull()

                if (!translated.isNullOrEmpty()) {
                    _lastTranslatedText.value = translated

                    // Store in Room database clipboard history
                    val database = AppDatabase.getInstance(applicationContext)
                    database.clipboardDao().insert(
                        ClipboardItem(
                            originalText = text,
                            translatedText = translated,
                            sourceLanguage = detectedLang,
                            targetLanguage = "bn",
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }

                // If user pressed & held the text and auto-speak is enabled: Speak aloud!
                if (isLongPress && _isAutoSpeakEnabled.value) {
                    ttsManager?.speak(text, detectedLang)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Translation/TTS error in accessibility service", e)
            }
        }
    }

    private fun safeRecycle(node: AccessibilityNodeInfo) {
        // In Android 11+ (API 30+), recycling is automatically handled by the framework
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            try {
                node.recycle()
            } catch (ignored: Exception) {}
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "OmniAccessibilityService interrupted")
        ttsManager?.stop()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        _isConnected.value = false
        ttsManager?.stop()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _isConnected.value = false
        ttsManager?.shutdown()
        ttsManager = null
        serviceScope.cancel()
        Log.d(TAG, "OmniAccessibilityService destroyed")
    }
}
