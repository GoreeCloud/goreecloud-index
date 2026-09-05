package com.goreecloud.index.provider.settings

import com.goreecloud.index.core.GoreeCloudIndexContract
import com.goreecloud.index.core.IndexAction
import com.goreecloud.index.core.IndexProcessingLocation
import com.goreecloud.index.core.IndexProvider
import com.goreecloud.index.core.IndexQuery
import com.goreecloud.index.core.IndexResult
import com.goreecloud.index.core.IndexResultType
import com.goreecloud.index.core.IndexTextMatcher
import java.util.Locale

internal data class SystemSettingDestination(
    val id: String,
    val title: String,
    val keywords: String,
    val action: String,
)

class SystemSettingsProvider : IndexProvider {
    override val providerId: String = GoreeCloudIndexContract.PROVIDER_SETTINGS
    override val displayName: String = "Settings · On-device"
    override val processingLocation: IndexProcessingLocation = IndexProcessingLocation.LOCAL
    override val timeoutMillis: Long = 250L
    override val supportsEmptyQuery: Boolean = false

    override suspend fun search(query: IndexQuery): List<IndexResult> {
        if (query.text.isBlank()) return emptyList()

        return DESTINATIONS
            .asSequence()
            .mapNotNull { destination ->
                val score = IndexTextMatcher.score(
                    query = query.text,
                    title = destination.title,
                    secondary = destination.keywords,
                ) ?: return@mapNotNull null

                IndexResult(
                    id = destination.id,
                    providerId = providerId,
                    type = IndexResultType.SETTING,
                    title = destination.title,
                    subtitle = "Android Settings · On-device",
                    score = score,
                    action = IndexAction.OpenSystemSetting(destination.action),
                )
            }
            .sortedWith(
                compareByDescending<IndexResult> { it.score }
                    .thenBy { it.title.lowercase(Locale.ROOT) }
                    .thenBy { it.id }
            )
            .take(query.maxResults.coerceAtMost(DESTINATIONS.size))
            .toList()
    }

    companion object {
        const val ACTION_SETTINGS = "android.settings.SETTINGS"
        const val ACTION_WIFI_SETTINGS = "android.settings.WIFI_SETTINGS"
        const val ACTION_BLUETOOTH_SETTINGS = "android.settings.BLUETOOTH_SETTINGS"
        const val ACTION_DISPLAY_SETTINGS = "android.settings.DISPLAY_SETTINGS"
        const val ACTION_SOUND_SETTINGS = "android.settings.SOUND_SETTINGS"
        const val ACTION_ACCESSIBILITY_SETTINGS = "android.settings.ACCESSIBILITY_SETTINGS"
        const val ACTION_LOCATION_SOURCE_SETTINGS = "android.settings.LOCATION_SOURCE_SETTINGS"
        const val ACTION_SECURITY_SETTINGS = "android.settings.SECURITY_SETTINGS"
        const val ACTION_MANAGE_APPLICATIONS_SETTINGS = "android.settings.MANAGE_APPLICATIONS_SETTINGS"
        const val ACTION_BATTERY_SAVER_SETTINGS = "android.settings.BATTERY_SAVER_SETTINGS"

        private val DESTINATIONS = listOf(
            SystemSettingDestination(
                id = "settings-root",
                title = "Settings",
                keywords = "Android system preferences device configuration",
                action = ACTION_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-wifi",
                title = "Wi-Fi",
                keywords = "wifi network internet wireless connections",
                action = ACTION_WIFI_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-bluetooth",
                title = "Bluetooth",
                keywords = "connected devices wireless accessories",
                action = ACTION_BLUETOOTH_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-display",
                title = "Display",
                keywords = "screen brightness appearance",
                action = ACTION_DISPLAY_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-sound",
                title = "Sound",
                keywords = "audio volume ringtone notification",
                action = ACTION_SOUND_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-accessibility",
                title = "Accessibility",
                keywords = "assistive services vision hearing interaction",
                action = ACTION_ACCESSIBILITY_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-location",
                title = "Location",
                keywords = "device location permission services",
                action = ACTION_LOCATION_SOURCE_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-security",
                title = "Security",
                keywords = "device security credentials lock",
                action = ACTION_SECURITY_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-apps",
                title = "Apps",
                keywords = "installed applications app management",
                action = ACTION_MANAGE_APPLICATIONS_SETTINGS,
            ),
            SystemSettingDestination(
                id = "settings-battery-saver",
                title = "Battery Saver",
                keywords = "battery power energy saver",
                action = ACTION_BATTERY_SAVER_SETTINGS,
            ),
        )

        private val ALLOWED_ACTIONS: Set<String> = DESTINATIONS
            .mapTo(linkedSetOf()) { it.action }

        fun isAllowedAction(action: String): Boolean = action in ALLOWED_ACTIONS

        internal fun destinationsForTest(): List<SystemSettingDestination> = DESTINATIONS.toList()
    }
}
