package com.pbmr.browser.core.privacy

import android.net.Uri

/**
 * Strips common tracking and analytics parameters from URLs.
 */
object TrackingParamStripper {

    private val trackingParameters = setOf(
        "utm_source",
        "utm_medium",
        "utm_campaign",
        "utm_term",
        "utm_content",
        "utm_id",
        "fbclid",
        "gclid",
        "gclsrc",
        "dclid",
        "msclkid",
        "mc_eid",
        "igshid",
        "twclid",
        "_hsenc",
        "_hsmi",
        "mkt_tok"
    )

    fun stripTrackingParameters(url: String): String {
        return try {
            val uri = Uri.parse(url)
            if (!uri.isHierarchical || uri.queryParameterNames.isEmpty()) {
                return url
            }

            val queryParams = uri.queryParameterNames
            val hasTrackingParams = queryParams.any { it.lowercase() in trackingParameters }

            if (!hasTrackingParams) return url

            val builder = uri.buildUpon().clearQuery()
            for (param in queryParams) {
                if (param.lowercase() !in trackingParameters) {
                    val values = uri.getQueryParameters(param)
                    values.forEach { v ->
                        builder.appendQueryParameter(param, v)
                    }
                }
            }
            builder.build().toString()
        } catch (e: Exception) {
            url
        }
    }
}
