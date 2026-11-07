package com.pbmr.browser.feature.browser

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pbmr.browser.core.engine.BrowserSession
import com.pbmr.browser.core.engine.GeckoComposeView
import com.pbmr.browser.feature.startpage.StartPageScreen

/**
 * Core Browser Screen with GeckoView canvas, Omnibar, and Start Page Hub.
 */
@Composable
fun BrowserScreen(
    session: BrowserSession?,
    isBottomOmnibar: Boolean = true,
    profileName: String = "Personal",
    onNavigateToTabs: () -> Unit,
    onProfileClick: () -> Unit = {},
    onCommandPaletteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (session == null) return
    val context = LocalContext.current

    val url by session.url.collectAsState()
    val isLoading by session.isLoading.collectAsState()
    val progress by session.progress.collectAsState()
    val isSecure by session.isSecure.collectAsState()
    val canGoBack by session.canGoBack.collectAsState()
    val blockedTrackerCount by session.blockedTrackerCount.collectAsState()

    var showShieldsSheet by remember { mutableStateOf(false) }
    var showQrDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = canGoBack) {
        session.goBack()
    }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Omnibar (if configured)
            if (!isBottomOmnibar) {
                Box(modifier = Modifier.padding(top = statusBarPadding)) {
                    Omnibar(
                        url = url,
                        isLoading = isLoading,
                        progress = progress,
                        isSecure = isSecure,
                        blockedTrackerCount = blockedTrackerCount,
                        profileName = profileName,
                        onNavigate = { target -> session.loadUri(target, context) },
                        onReload = { session.reload() },
                        onStop = { session.stop() },
                        onShieldsClick = { showShieldsSheet = true },
                        onProfileClick = onProfileClick,
                        onCommandPaletteClick = onCommandPaletteClick,
                        onQrScanClick = { showQrDialog = true }
                    )
                }
            }

            // Web Canvas or Start Page Hub
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Permanently mount GeckoComposeView so session is attached and ready
                GeckoComposeView(session = session)

                if (url == "about:blank" || url.isEmpty()) {
                    StartPageScreen(
                        onSearch = { query -> session.loadUri(query, context) },
                        onOpenSpeedDial = { speedDialUrl -> session.loadUri(speedDialUrl, context) }
                    )
                }
            }

            // Bottom Omnibar (default - ergonomic near floating navigation bar)
            if (isBottomOmnibar) {
                Box(modifier = Modifier.padding(bottom = 76.dp)) {
                    Omnibar(
                        url = url,
                        isLoading = isLoading,
                        progress = progress,
                        isSecure = isSecure,
                        blockedTrackerCount = blockedTrackerCount,
                        profileName = profileName,
                        onNavigate = { target -> session.loadUri(target, context) },
                        onReload = { session.reload() },
                        onStop = { session.stop() },
                        onShieldsClick = { showShieldsSheet = true },
                        onProfileClick = onProfileClick,
                        onCommandPaletteClick = onCommandPaletteClick,
                        onQrScanClick = { showQrDialog = true }
                    )
                }
            }
        }

        // Shields Bottom Sheet
        if (showShieldsSheet) {
            ShieldsSheet(
                url = url,
                isSecure = isSecure,
                blockedTrackerCount = blockedTrackerCount,
                onDismissRequest = { showShieldsSheet = false }
            )
        }

        // QR Code Scanner Sheet
        if (showQrDialog) {
            QrScanDialog(
                onDismissRequest = { showQrDialog = false },
                onQrScanned = { target -> session.loadUri(target, context) }
            )
        }
    }
}
