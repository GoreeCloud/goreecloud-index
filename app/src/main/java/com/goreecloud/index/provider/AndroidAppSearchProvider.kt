package com.goreecloud.index.provider

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import com.goreecloud.index.model.ProcessingMode
import com.goreecloud.index.model.SearchAction
import com.goreecloud.index.model.SearchProviderDescriptor
import com.goreecloud.index.model.SearchQuery
import com.goreecloud.index.model.SearchResourceType
import com.goreecloud.index.model.SearchResult
import java.util.Locale

class AndroidAppSearchProvider(
    private val packageManager: PackageManager,
) : SearchProvider {
    override val descriptor = SearchProviderDescriptor(
        id = "android.launcher-apps",
        displayName = "Applications",
        resourceTypes = setOf(SearchResourceType.APPLICATION),
        processingMode = ProcessingMode.LOCAL,
    )

    override fun search(query: SearchQuery): List<SearchResult> {
        val needle = query.normalizedText.lowercase(Locale.ROOT)
        if (needle.isEmpty()) return emptyList()

        return queryLauncherActivities()
            .asSequence()
            .mapNotNull { resolveInfo ->
                val packageName = resolveInfo.activityInfo?.packageName ?: return@mapNotNull null
                val label = resolveInfo.loadLabel(packageManager)?.toString()?.trim().orEmpty()
                if (label.isEmpty()) return@mapNotNull null

                val labelKey = label.lowercase(Locale.ROOT)
                val packageKey = packageName.lowercase(Locale.ROOT)
                val score = when {
                    labelKey == needle -> 1.0
                    labelKey.startsWith(needle) -> 0.95
                    labelKey.contains(needle) -> 0.85
                    packageKey.startsWith(needle) -> 0.75
                    packageKey.contains(needle) -> 0.65
                    else -> return@mapNotNull null
                }

                SearchResult(
                    id = packageName,
                    providerId = descriptor.id,
                    resourceType = SearchResourceType.APPLICATION,
                    title = label,
                    subtitle = packageName,
                    score = score,
                    action = SearchAction.LaunchApplication(packageName),
                )
            }
            .distinctBy { it.id }
            .take(MAX_RESULTS)
            .toList()
    }

    @Suppress("DEPRECATION")
    private fun queryLauncherActivities(): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0L))
        } else {
            packageManager.queryIntentActivities(intent, 0)
        }
    }

    private companion object {
        const val MAX_RESULTS = 50
    }
}
