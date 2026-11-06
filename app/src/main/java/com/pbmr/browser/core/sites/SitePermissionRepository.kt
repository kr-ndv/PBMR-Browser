package com.pbmr.browser.core.sites

enum class SitePermissionType(val displayName: String) {
    CAMERA("Camera"),
    MICROPHONE("Microphone"),
    LOCATION("Location"),
    NOTIFICATIONS("Notifications"),
    DRM_MEDIA("Protected Media (DRM)")
}

data class SitePermissionRecord(
    val domain: String,
    val permissionType: SitePermissionType,
    val isGranted: Boolean,
    val lastRequestedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Tracks and persists per-domain permissions and audit history.
 */
class SitePermissionRepository {

    private val permissions = mutableListOf<SitePermissionRecord>()

    fun recordPermission(domain: String, type: SitePermissionType, isGranted: Boolean) {
        permissions.removeAll { it.domain == domain && it.permissionType == type }
        permissions.add(SitePermissionRecord(domain, type, isGranted))
    }

    fun getPermissionsForDomain(domain: String): List<SitePermissionRecord> {
        return permissions.filter { it.domain == domain }
    }

    fun getAllPermissions(): List<SitePermissionRecord> = permissions.toList()

    fun resetPermissionsForDomain(domain: String) {
        permissions.removeAll { it.domain == domain }
    }
}
