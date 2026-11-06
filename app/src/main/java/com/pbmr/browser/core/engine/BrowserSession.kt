package com.pbmr.browser.core.engine

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings

/**
 * Encapsulates a single GeckoSession with reactive StateFlows for UI binding.
 */
class BrowserSession(
    val id: String,
    val isPrivate: Boolean = false,
    val profileId: String = "default"
) {
    val geckoSession: GeckoSession = GeckoSession(
        GeckoSessionSettings.Builder()
            .usePrivateMode(isPrivate)
            .build()
    )

    private val _url = MutableStateFlow("about:blank")
    val url: StateFlow<String> = _url.asStateFlow()

    private val _title = MutableStateFlow("New Tab")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val _canGoForward = MutableStateFlow(false)
    val canGoForward: StateFlow<Boolean> = _canGoForward.asStateFlow()

    private val _isSecure = MutableStateFlow(false)
    val isSecure: StateFlow<Boolean> = _isSecure.asStateFlow()

    private val _blockedTrackerCount = MutableStateFlow(0)
    val blockedTrackerCount: StateFlow<Int> = _blockedTrackerCount.asStateFlow()

    private val _isSuspended = MutableStateFlow(false)
    val isSuspended: StateFlow<Boolean> = _isSuspended.asStateFlow()

    var lastActiveTimestamp: Long = System.currentTimeMillis()

    init {
        setupDelegates()
    }

    private fun setupDelegates() {
        geckoSession.progressDelegate = object : GeckoSession.ProgressDelegate {
            override fun onPageStart(session: GeckoSession, url: String) {
                _url.value = url
                _isLoading.value = true
                _progress.value = 0.05f
                _blockedTrackerCount.value = 0
            }

            override fun onPageStop(session: GeckoSession, success: Boolean) {
                _isLoading.value = false
                _progress.value = 1.0f
            }

            override fun onProgressChange(session: GeckoSession, progress: Int) {
                _progress.value = (progress / 100f).coerceIn(0f, 1f)
            }

            override fun onSecurityChange(
                session: GeckoSession,
                securityInfo: GeckoSession.ProgressDelegate.SecurityInformation
            ) {
                _isSecure.value = securityInfo.isSecure
            }
        }

        geckoSession.navigationDelegate = object : GeckoSession.NavigationDelegate {
            override fun onLocationChange(session: GeckoSession, url: String?, perms: MutableList<GeckoSession.PermissionDelegate.ContentPermission>?) {
                if (url != null) {
                    _url.value = url
                }
            }

            override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
                _canGoBack.value = canGoBack
            }

            override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
                _canGoForward.value = canGoForward
            }
        }

        geckoSession.contentBlockingDelegate = object : ContentBlocking.Delegate {
            override fun onContentBlocked(session: GeckoSession, event: ContentBlocking.BlockEvent) {
                _blockedTrackerCount.value += 1
            }
        }
    }

    fun open(runtimeContext: Context) {
        if (!geckoSession.isOpen) {
            val runtime = GeckoManager.getRuntime(runtimeContext, profileId)
            geckoSession.open(runtime)
        }
        _isSuspended.value = false
        lastActiveTimestamp = System.currentTimeMillis()
    }

    fun loadUri(uri: String, context: Context) {
        open(context)
        val formattedUri = formatInputUri(uri)
        geckoSession.loadUri(formattedUri)
    }

    fun reload() {
        geckoSession.reload()
    }

    fun stop() {
        geckoSession.stop()
    }

    fun goBack() {
        geckoSession.goBack()
    }

    fun goForward() {
        geckoSession.goForward()
    }

    fun suspendSession() {
        if (geckoSession.isOpen) {
            geckoSession.close()
            _isSuspended.value = true
        }
    }

    fun close() {
        if (geckoSession.isOpen) {
            geckoSession.close()
        }
    }

    private fun formatInputUri(input: String): String {
        val trimmed = input.trim()
        return if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("about:")) {
            trimmed
        } else if (trimmed.contains(".") && !trimmed.contains(" ")) {
            "https://$trimmed"
        } else {
            // Default to DuckDuckGo search query
            "https://duckduckgo.com/?q=${java.net.URLEncoder.encode(trimmed, "UTF-8")}"
        }
    }
}
