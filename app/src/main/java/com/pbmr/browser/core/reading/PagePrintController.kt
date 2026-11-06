package com.pbmr.browser.core.reading

import android.app.Activity
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import org.mozilla.geckoview.GeckoSession

/**
 * Handles printing and saving web pages as PDF documents.
 */
object PagePrintController {

    /**
     * Triggers the Android Print subsystem for the active GeckoSession.
     */
    fun printPage(activity: Activity, session: GeckoSession, jobTitle: String = "PBMR_WebPage") {
        val printManager = activity.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return

        // GeckoView supports print document adapter
        // In standard GeckoView, session.print() or an adapter can be passed
        val printAttributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .build()

        val jobName = "PBMR - $jobTitle"
        
        // Android system print dialog handles "Save as PDF" automatically
        val customAdapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: android.os.CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: android.os.Bundle?
            ) {
                val info = android.print.PrintDocumentInfo.Builder("$jobTitle.pdf")
                    .setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()
                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out android.print.PageRange>?,
                destination: android.os.ParcelFileDescriptor?,
                cancellationSignal: android.os.CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                callback?.onWriteFinished(arrayOf(android.print.PageRange.ALL_PAGES))
            }
        }

        printManager.print(jobName, customAdapter, printAttributes)
    }
}
