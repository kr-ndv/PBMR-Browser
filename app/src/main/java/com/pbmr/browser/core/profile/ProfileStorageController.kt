package com.pbmr.browser.core.profile

import android.content.Context
import java.io.File

/**
 * Manages the physical storage directories and isolation boundaries for profiles.
 * Files for each profile reside under `context.filesDir/profiles/<profile_id>`.
 */
class ProfileStorageController(private val context: Context) {

    private val profilesRootDir: File
        get() = File(context.filesDir, "profiles").apply { if (!exists()) mkdirs() }

    fun getProfileDir(profileId: String): File {
        return File(profilesRootDir, profileId).apply { if (!exists()) mkdirs() }
    }

    fun deleteProfileStorage(profileId: String): Boolean {
        val dir = File(profilesRootDir, profileId)
        return if (dir.exists()) {
            dir.deleteRecursively()
        } else {
            true
        }
    }

    fun calculateProfileDiskUsage(profileId: String): Long {
        val dir = File(profilesRootDir, profileId)
        if (!dir.exists()) return 0L
        return getFolderSize(dir)
    }

    private fun getFolderSize(file: File): Long {
        if (!file.exists()) return 0L
        if (file.isFile) return file.length()
        var size = 0L
        file.listFiles()?.forEach { child ->
            size += getFolderSize(child)
        }
        return size
    }
}
