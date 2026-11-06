package com.pbmr.browser

import com.pbmr.browser.core.download.DownloadQueue
import com.pbmr.browser.core.download.DownloadStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class DownloadQueueTest {

    @Test
    fun testDownloadQueueFlow() {
        val queue = DownloadQueue()
        val task = queue.enqueueDownload("geckoview.zip", "https://example.com/geckoview.zip", 5000L)

        assertEquals("geckoview.zip", task.fileName)
        assertEquals(DownloadStatus.DOWNLOADING, task.status)
        assertEquals(1, queue.tasks.value.size)

        queue.pauseDownload(task.id)
        val paused = queue.tasks.value.find { it.id == task.id }!!
        assertEquals(DownloadStatus.PAUSED, paused.status)

        queue.resumeDownload(task.id)
        val resumed = queue.tasks.value.find { it.id == task.id }!!
        assertEquals(DownloadStatus.DOWNLOADING, resumed.status)

        queue.cancelDownload(task.id)
        assertEquals(0, queue.tasks.value.size)
    }
}
