package com.pbmr.browser.core.data.model

import com.pbmr.browser.core.design.theme.FamilyAccent

/**
 * Model representing an independent Desktop-Class Browser Profile.
 * Each profile owns its own isolated Gecko storage context (cookies, cache,
 * localStorage, indexedDB, and permissions).
 */
data class BrowserProfile(
    val id: String,
    val name: String,
    val accent: FamilyAccent = FamilyAccent.FOREST,
    val iconName: String = "Person",
    val isTemporary: Boolean = false,
    val isLocked: Boolean = false,
    val pinHash: String? = null,
    val customHomepage: String? = null,
    val defaultSearchEngine: String = "DuckDuckGo",
    val isDefault: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis()
)
