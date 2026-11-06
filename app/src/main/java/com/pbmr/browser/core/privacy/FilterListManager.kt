package com.pbmr.browser.core.privacy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FilterListSubscription(
    val id: String,
    val name: String,
    val url: String,
    val isEnabled: Boolean = true,
    val ruleCount: Int = 0,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Manages ad-blocking and privacy filter list subscriptions (EasyList, EasyPrivacy, uBlock syntax).
 */
class FilterListManager {

    private val defaultSubscriptions = listOf(
        FilterListSubscription("easylist", "EasyList (Primary Adblock)", "https://easylist.to/easylist/easylist.txt", ruleCount = 68420),
        FilterListSubscription("easyprivacy", "EasyPrivacy (Trackers & Telemetry)", "https://easylist.to/easylist/easyprivacy.txt", ruleCount = 34120),
        FilterListSubscription("annoyances", "uBlock Annoyances & Cookie Banners", "https://filters.adtidy.org/extension/ublock/filters/14.txt", ruleCount = 28900),
        FilterListSubscription("cryptominers", "NoCoin Cryptomining Filter", "https://raw.githubusercontent.com/hoshsadiq/adblock-nocoin-list/master/nocoin.txt", ruleCount = 1850)
    )

    private val _subscriptions = MutableStateFlow(defaultSubscriptions)
    val subscriptions: StateFlow<List<FilterListSubscription>> = _subscriptions.asStateFlow()

    fun toggleSubscription(id: String, enabled: Boolean) {
        _subscriptions.value = _subscriptions.value.map { sub ->
            if (sub.id == id) sub.copy(isEnabled = enabled) else sub
        }
    }

    fun addCustomSubscription(name: String, url: String) {
        val newSub = FilterListSubscription(
            id = "custom_${System.currentTimeMillis()}",
            name = name,
            url = url,
            ruleCount = 0
        )
        _subscriptions.value = _subscriptions.value + newSub
    }

    fun removeSubscription(id: String) {
        _subscriptions.value = _subscriptions.value.filter { it.id != id }
    }
}
