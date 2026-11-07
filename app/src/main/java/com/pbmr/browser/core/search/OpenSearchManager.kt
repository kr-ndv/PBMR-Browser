package com.pbmr.browser.core.search

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.openSearchDataStore by preferencesDataStore(name = "pbmr_custom_search_engines")

data class CustomSearchEngine(
    val id: String,
    val name: String,
    val searchUrlTemplate: String,
    val faviconUrl: String? = null
)

/**
 * Discovers and saves OpenSearch engines from visited websites.
 */
class OpenSearchManager(private val context: Context) {

    private val KEY_CUSTOM_ENGINES = stringPreferencesKey("custom_engines_json")

    val customEnginesFlow: Flow<List<CustomSearchEngine>> = context.openSearchDataStore.data.map { prefs ->
        val jsonStr = prefs[KEY_CUSTOM_ENGINES] ?: "[]"
        parseEngines(jsonStr)
    }

    suspend fun saveEngine(engine: CustomSearchEngine) {
        context.openSearchDataStore.edit { prefs ->
            val list = parseEngines(prefs[KEY_CUSTOM_ENGINES] ?: "[]").toMutableList()
            list.removeAll { it.id == engine.id }
            list.add(engine)
            prefs[KEY_CUSTOM_ENGINES] = serializeEngines(list)
        }
    }

    suspend fun removeEngine(id: String) {
        context.openSearchDataStore.edit { prefs ->
            val list = parseEngines(prefs[KEY_CUSTOM_ENGINES] ?: "[]").toMutableList()
            list.removeAll { it.id == id }
            prefs[KEY_CUSTOM_ENGINES] = serializeEngines(list)
        }
    }

    /**
     * Inspects HTML or URL pattern to create an OpenSearch engine candidate.
     */
    fun detectCandidate(url: String, title: String): CustomSearchEngine? {
        val domain = try {
            java.net.URI(url).host ?: ""
        } catch (e: Exception) {
            ""
        }
        if (domain.isBlank()) return null

        val cleanDomain = domain.removePrefix("www.")
        val engineName = title.ifBlank { cleanDomain }
        val template = "https://$cleanDomain/search?q=%s"

        return CustomSearchEngine(
            id = cleanDomain,
            name = engineName,
            searchUrlTemplate = template,
            faviconUrl = "https://$cleanDomain/favicon.ico"
        )
    }

    private fun parseEngines(jsonStr: String): List<CustomSearchEngine> {
        return try {
            val arr = JSONArray(jsonStr)
            val result = mutableListOf<CustomSearchEngine>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                result.add(
                    CustomSearchEngine(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        searchUrlTemplate = obj.getString("searchUrlTemplate"),
                        faviconUrl = if (obj.has("faviconUrl")) obj.getString("faviconUrl") else null
                    )
                )
            }
            result
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun serializeEngines(engines: List<CustomSearchEngine>): String {
        val arr = JSONArray()
        for (e in engines) {
            val obj = JSONObject()
            obj.put("id", e.id)
            obj.put("name", e.name)
            obj.put("searchUrlTemplate", e.searchUrlTemplate)
            if (e.faviconUrl != null) obj.put("faviconUrl", e.faviconUrl)
            arr.put(obj)
        }
        return arr.toString()
    }
}
