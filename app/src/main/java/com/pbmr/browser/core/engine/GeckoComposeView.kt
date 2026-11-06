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
                setSession(session.geckoSession)
            }
        },
        update = { geckoView ->
            if (geckoView.session != session.geckoSession) {
                geckoView.releaseSession()
                geckoView.setSession(session.geckoSession)
            }
        }
    )

    DisposableEffect(session) {
        onDispose {
            // No-op or cleanup if needed
        }
    }
}
