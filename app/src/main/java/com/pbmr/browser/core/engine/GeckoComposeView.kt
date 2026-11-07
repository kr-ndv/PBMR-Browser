package com.pbmr.browser.core.engine

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.geckoview.GeckoView

/**
 * Jetpack Compose wrapper for Mozilla's GeckoView.
 */
@Composable
fun GeckoComposeView(
    session: BrowserSession?,
    modifier: Modifier = Modifier
) {
    if (session == null) return

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            GeckoView(context).apply {
                try {
                    setSession(session.geckoSession)
                } catch (e: Exception) {
                    // Session might already be attached to an earlier instance
                }
            }
        },
        update = { geckoView ->
            if (geckoView.session != session.geckoSession) {
                try {
                    geckoView.releaseSession()
                } catch (_: Exception) {}
                try {
                    geckoView.setSession(session.geckoSession)
                } catch (_: Exception) {}
            }
        },
        onRelease = { geckoView ->
            try {
                geckoView.releaseSession()
            } catch (_: Exception) {}
        }
    )
}
