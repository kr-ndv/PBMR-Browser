package com.pbmr.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tab
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pbmr.browser.core.design.components.FamilyFloatingNavigationBar
import com.pbmr.browser.core.design.components.NavigationDestination
import com.pbmr.browser.core.design.theme.FamilyAccent
import com.pbmr.browser.core.design.theme.FamilyTheme
import com.pbmr.browser.core.engine.SessionManager
import com.pbmr.browser.core.performance.TabSleeper
import com.pbmr.browser.core.privacy.FilterListManager
import com.pbmr.browser.core.privacy.FireButtonController
import com.pbmr.browser.core.privacy.PanicButtonController
import com.pbmr.browser.core.profile.ProfileManager
import com.pbmr.browser.core.tabs.SessionSnapshotManager
import com.pbmr.browser.core.tabs.TabArchiveManager
import com.pbmr.browser.core.tabs.TabGroupManager
import com.pbmr.browser.feature.browser.BrowserScreen
import com.pbmr.browser.feature.browser.CommandPaletteSheet
import com.pbmr.browser.feature.library.LibraryScreen
import com.pbmr.browser.feature.privacy.PrivacyDashboardScreen
import com.pbmr.browser.feature.profiles.ProfileSwitcherSheet
import com.pbmr.browser.feature.settings.SettingsScreen
import com.pbmr.browser.feature.tabs.TabsScreen

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var tabSleeper: TabSleeper
    private lateinit var fireButtonController: FireButtonController
    private lateinit var profileManager: ProfileManager
    private lateinit var filterListManager: FilterListManager
    private lateinit var panicButtonController: PanicButtonController
    private lateinit var sessionSnapshotManager: SessionSnapshotManager
    private lateinit var tabArchiveManager: TabArchiveManager
    private lateinit var tabGroupManager: TabGroupManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sessionManager = SessionManager(applicationContext)
        profileManager = ProfileManager(applicationContext)
        fireButtonController = FireButtonController(applicationContext, sessionManager)
        filterListManager = FilterListManager()
        panicButtonController = PanicButtonController(
            applicationContext,
            sessionManager,
            fireButtonController,
            profileManager
        )
        sessionSnapshotManager = SessionSnapshotManager(sessionManager)
        tabArchiveManager = TabArchiveManager(sessionManager)
        tabGroupManager = TabGroupManager()

        handleShortcutIntent(intent)

        setContent {
            val scope = rememberCoroutineScope()
            remember {
                tabSleeper = TabSleeper(sessionManager, scope).apply { startMonitoring() }
            }

            val activeProfile by profileManager.activeProfile.collectAsState()
            var currentAccent by remember(activeProfile.accent) { mutableStateOf(activeProfile.accent) }
            var isAmoled by remember { mutableStateOf(false) }
            var isBottomOmnibar by remember { mutableStateOf(true) }

            FamilyTheme(
                accent = currentAccent,
                amoled = isAmoled
            ) {
                MainAppContent(
                    sessionManager = sessionManager,
                    tabSleeper = tabSleeper,
                    fireButtonController = fireButtonController,
                    profileManager = profileManager,
                    filterListManager = filterListManager,
                    panicButtonController = panicButtonController,
                    currentAccent = currentAccent,
                    onAccentChange = { currentAccent = it },
                    isAmoled = isAmoled,
                    onAmoledChange = { isAmoled = it },
                    isBottomOmnibar = isBottomOmnibar,
                    onBottomOmnibarChange = { isBottomOmnibar = it }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tabSleeper.isInitialized) {
            tabSleeper.stopMonitoring()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        com.pbmr.browser.core.engine.MediaSessionController.handleUserLeaveHint(this)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleShortcutIntent(intent)
    }

    private fun handleShortcutIntent(intent: android.content.Intent?) {
        when (intent?.action) {
            "com.pbmr.browser.ACTION_NEW_PRIVATE_TAB" -> {
                sessionManager.createTab(isPrivate = true)
            }
            "com.pbmr.browser.ACTION_FIRE_BURN" -> {
                fireButtonController.burnAndClose()
            }
        }
    }
}

@Composable
fun MainAppContent(
    sessionManager: SessionManager,
    tabSleeper: TabSleeper,
    fireButtonController: FireButtonController,
    profileManager: ProfileManager,
    filterListManager: FilterListManager,
    panicButtonController: PanicButtonController,
    currentAccent: FamilyAccent,
    onAccentChange: (FamilyAccent) -> Unit,
    isAmoled: Boolean,
    onAmoledChange: (Boolean) -> Unit,
    isBottomOmnibar: Boolean,
    onBottomOmnibarChange: (Boolean) -> Unit
) {
    var currentRoute by remember { mutableStateOf("browser") }
    val activeSession by sessionManager.activeSession.collectAsState()
    val activeProfile by profileManager.activeProfile.collectAsState()
    val tabs by sessionManager.tabs.collectAsState()

    var showProfileSheet by remember { mutableStateOf(false) }
    var showCommandPalette by remember { mutableStateOf(false) }

    val navigationDestinations = listOf(
        NavigationDestination(
            route = "browser",
            label = "Web",
            selectedIcon = Icons.Filled.Public,
            unselectedIcon = Icons.Outlined.Public
        ),
        NavigationDestination(
            route = "tabs",
            label = "Tabs",
            selectedIcon = Icons.Filled.Tab,
            unselectedIcon = Icons.Outlined.Tab,
            badgeCount = tabs.size
        ),
        NavigationDestination(
            route = "privacy",
            label = "Shields",
            selectedIcon = Icons.Filled.Security,
            unselectedIcon = Icons.Outlined.Security
        ),
        NavigationDestination(
            route = "library",
            label = "Library",
            selectedIcon = Icons.Filled.Bookmark,
            unselectedIcon = Icons.Outlined.BookmarkBorder
        ),
        NavigationDestination(
            route = "settings",
            label = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Main Screen Content based on route
        when (currentRoute) {
            "browser" -> {
                BrowserScreen(
                    session = activeSession,
                    isBottomOmnibar = isBottomOmnibar,
                    profileName = activeProfile.name,
                    onNavigateToTabs = { currentRoute = "tabs" },
                    onProfileClick = { showProfileSheet = true },
                    onCommandPaletteClick = { showCommandPalette = true }
                )
            }

            "tabs" -> {
                TabsScreen(
                    sessionManager = sessionManager,
                    fireButtonController = fireButtonController,
                    onTabSelected = { currentRoute = "browser" }
                )
            }

            "privacy" -> {
                PrivacyDashboardScreen(
                    filterListManager = filterListManager,
                    panicButtonController = panicButtonController,
                    onTriggerPanic = {
                        panicButtonController.executePanic {
                            currentRoute = "browser"
                        }
                    }
                )
            }

            "library" -> {
                LibraryScreen(
                    onNavigateToUrl = { url ->
                        activeSession?.loadUri(url, sessionManager.activeSession.value?.geckoSession?.context ?: return@LibraryScreen)
                        currentRoute = "browser"
                    }
                )
            }

            "settings" -> {
                SettingsScreen(
                    currentAccent = currentAccent,
                    onAccentChange = onAccentChange,
                    isAmoled = isAmoled,
                    onAmoledChange = onAmoledChange,
                    isBottomOmnibar = isBottomOmnibar,
                    onBottomOmnibarChange = onBottomOmnibarChange,
                    tabSleeper = tabSleeper
                )
            }
        }

        // PBMR App Family Floating Navigation Bar with Sliding Spring Pill
        FamilyFloatingNavigationBar(
            destinations = navigationDestinations,
            currentRoute = currentRoute,
            onDestinationSelected = { route -> currentRoute = route },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Modal Sheets
        if (showProfileSheet) {
            ProfileSwitcherSheet(
                profileManager = profileManager,
                onDismissRequest = { showProfileSheet = false },
                onManageProfiles = { currentRoute = "settings" }
            )
        }

        if (showCommandPalette) {
            CommandPaletteSheet(
                onDismissRequest = { showCommandPalette = false },
                onNewTab = {
                    sessionManager.createNewTab("about:blank")
                    currentRoute = "browser"
                },
                onNewPrivateTab = {
                    sessionManager.createNewTab("about:blank", isPrivate = true)
                    currentRoute = "browser"
                },
                onSwitchProfile = { showProfileSheet = true },
                onOpenTabs = { currentRoute = "tabs" },
                onOpenLibrary = { currentRoute = "library" },
                onOpenSettings = { currentRoute = "settings" },
                onTriggerFire = {
                    fireButtonController.executeBurn { currentRoute = "browser" }
                }
            )
        }
    }
}
