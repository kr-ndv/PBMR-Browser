package com.pbmr.browser.core.profile

import android.content.Context
import com.pbmr.browser.core.data.model.BrowserProfile
import com.pbmr.browser.core.design.theme.FamilyAccent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * High-level manager coordinating browser profiles, persistence, and storage boundaries.
 */
class ProfileManager(
    private val context: Context,
    val storageController: ProfileStorageController = ProfileStorageController(context),
    val rulesEngine: ProfileRulesEngine = ProfileRulesEngine()
) {
    private val defaultProfile = BrowserProfile(
        id = "default",
        name = "Personal",
        accent = FamilyAccent.FOREST,
        isDefault = true
    )

    private val _profiles = MutableStateFlow<List<BrowserProfile>>(listOf(defaultProfile))
    val profiles: StateFlow<List<BrowserProfile>> = _profiles.asStateFlow()

    private val _activeProfile = MutableStateFlow(defaultProfile)
    val activeProfile: StateFlow<BrowserProfile> = _activeProfile.asStateFlow()

    init {
        // Provide standard Work and Private profile presets
        val workProfile = BrowserProfile(
            id = "work",
            name = "Work",
            accent = FamilyAccent.BLUE,
            iconName = "Work"
        )
        val privateProfile = BrowserProfile(
            id = "private",
            name = "Incognito",
            accent = FamilyAccent.VIOLET,
            iconName = "Security",
            isTemporary = true
        )
        _profiles.value = listOf(defaultProfile, workProfile, privateProfile)
    }

    fun switchProfile(profileId: String, onRequireUnlock: ((BrowserProfile) -> Unit)? = null): Boolean {
        val target = _profiles.value.find { it.id == profileId } ?: return false

        if (target.isLocked) {
            onRequireUnlock?.invoke(target)
            return false
        }

        // Check if leaving a temporary profile -> wipe its storage
        val current = _activeProfile.value
        if (current.isTemporary && current.id != target.id) {
            storageController.deleteProfileStorage(current.id)
        }

        _activeProfile.value = target
        return true
    }

    fun createProfile(
        name: String,
        accent: FamilyAccent,
        isTemporary: Boolean = false,
        isLocked: Boolean = false,
        pinHash: String? = null
    ): BrowserProfile {
        val newProfile = BrowserProfile(
            id = UUID.randomUUID().toString(),
            name = name,
            accent = accent,
            isTemporary = isTemporary,
            isLocked = isLocked,
            pinHash = pinHash
        )
        _profiles.value = _profiles.value + newProfile
        return newProfile
    }

    fun deleteProfile(profileId: String): Boolean {
        if (profileId == "default") return false // Cannot delete root default profile
        val profile = _profiles.value.find { it.id == profileId } ?: return false

        storageController.deleteProfileStorage(profileId)
        _profiles.value = _profiles.value - profile

        if (_activeProfile.value.id == profileId) {
            _activeProfile.value = defaultProfile
        }
        return true
    }

    fun unlockProfile(profileId: String, pinInput: String): Boolean {
        val target = _profiles.value.find { it.id == profileId } ?: return false
        // Simple hash check or verification
        if (target.pinHash == pinInput || target.pinHash == null) {
            _activeProfile.value = target
            return true
        }
        return false
    }
}
