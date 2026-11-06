package com.pbmr.browser.core.profile

data class DomainProfileRule(
    val id: String,
    val pattern: String, // e.g. "github.com", "*.google.com"
    val targetProfileId: String,
    val isEnabled: Boolean = true
)

/**
 * Evaluates navigation requests against domain routing rules to automatically
 * route websites to their designated browser profile.
 */
class ProfileRulesEngine {

    private val rules = mutableListOf<DomainProfileRule>()

    fun addRule(rule: DomainProfileRule) {
        rules.removeAll { it.id == rule.id }
        rules.add(rule)
    }

    fun removeRule(ruleId: String) {
        rules.removeAll { it.id == ruleId }
    }

    fun getAllRules(): List<DomainProfileRule> = rules.toList()

    /**
     * Resolves the target profile ID for a given URL, or returns null if no rule matches.
     */
    fun resolveTargetProfile(url: String): String? {
        val host = extractHost(url).lowercase()
        for (rule in rules) {
            if (!rule.isEnabled) continue
            val pattern = rule.pattern.lowercase().trim()
            if (matchesPattern(host, pattern)) {
                return rule.targetProfileId
            }
        }
        return null
    }

    private fun matchesPattern(host: String, pattern: String): Boolean {
        if (pattern.startsWith("*.")) {
            val suffix = pattern.substring(2)
            return host == suffix || host.endsWith(".$suffix")
        }
        return host == pattern || host.endsWith(".$pattern")
    }

    private fun extractHost(url: String): String {
        return try {
            val uri = java.net.URI(url)
            uri.host ?: url
        } catch (e: Exception) {
            url
        }
    }
}
