package com.pbmr.browser.core.download

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

enum class DownloadStatus {
    QUEUED, DOWNLOADING, PAUSED, COMPLETED, FAILED
}

data class DownloadTask(
    val id: String,
    val fileName: String,
    val url: String,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val status: DownloadStatus = DownloadStatus.QUEUED,
    val profileId: String = "default",
    val mimeType: String? = null
) {
    val progress: Float
        get() = if (totalBytes > 0) (downloadedBytes.toFloat() / totalBytes).coerceIn(0f, 1f) else 0f
}

/**
 * Manages the multi-task download queue with pause/resume capabilities.
 */
class DownloadQueue {

    private val _tasks = MutableStateFlow<List<DownloadTask>>(emptyList())
    val tasks: StateFlow<List<DownloadTask>> = _tasks.asStateFlow()

    fun enqueueDownload(fileName: String, url: String, totalBytes: Long = 0L, profileId: String = "default"): DownloadTask {
        val task = DownloadTask(
            id = UUID.randomUUID().toString(),
            fileName = fileName,
            url = url,
            totalBytes = totalBytes,
            profileId = profileId,
            status = DownloadStatus.DOWNLOADING
        )
        _tasks.value = listOf(task) + _tasks.value
        return task
    }

    fun pauseDownload(taskId: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(status = DownloadStatus.PAUSED) else it
        }
    }

    fun resumeDownload(taskId: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(status = DownloadStatus.DOWNLOADING) else it
        }
    }

    fun cancelDownload(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }
}
