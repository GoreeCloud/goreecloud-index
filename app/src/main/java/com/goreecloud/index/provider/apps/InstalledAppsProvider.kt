package com.goreecloud.index.provider.apps

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import com.goreecloud.index.core.GoreeCloudIndexContract
import com.goreecloud.index.core.IndexAction
import com.goreecloud.index.core.IndexProvider
import com.goreecloud.index.core.IndexQuery
import com.goreecloud.index.core.IndexResult
import com.goreecloud.index.core.IndexResultType
import com.goreecloud.index.core.IndexTextMatcher

internal data class InstalledAppEntry(
    val label: String,
    val packageName: String,
    val className: String,
) {
    val componentName: ComponentName
        get() = ComponentName(packageName, className)
}

class InstalledAppsProvider(
    private val context: Context,
) : IndexProvider {
    override val providerId: String = GoreeCloudIndexContract.PROVIDER_APPS

    @Volatile
    private var entries: List<InstalledAppEntry> = discoverApps()

    fun refresh() {
        entries = discoverApps()
    }

    override fun search(query: IndexQuery): List<IndexResult> = entries
        .asSequence()
        .mapNotNull { app ->
            val score = IndexTextMatcher.score(
                query = query.text,
                title = app.label,
                secondary = app.packageName,
            ) ?: return@mapNotNull null

            IndexResult(
                id = app.componentName.flattenToString(),
                providerId = providerId,
                type = IndexResultType.APP,
                title = app.label,
                subtitle = app.packageName,
                score = score,
                action = IndexAction.LaunchActivity(
                    packageName = app.packageName,
                    className = app.className,
                ),
            )
        }
        .sortedWith(
            compareByDescending<IndexResult> { it.score }
                .thenBy(String.CASE_INSENSITIVE_ORDER) { it.title }
        )
        .take(query.maxResults)
        .toList()

    private fun discoverApps(): List<InstalledAppEntry> {
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return context.packageManager
            .queryIntentActivities(launcherIntent, 0)
            .asSequence()
            .mapNotNull(::toEntry)
            .filterNot { it.packageName == context.packageName }
            .distinctBy { it.componentName.flattenToString() }
            .sortedWith(
                compareBy<InstalledAppEntry>(String.CASE_INSENSITIVE_ORDER) { it.label }
                    .thenBy { it.packageName }
            )
            .toList()
    }

    private fun toEntry(resolveInfo: ResolveInfo): InstalledAppEntry? {
        val activityInfo = resolveInfo.activityInfo ?: return null
        val label = resolveInfo.loadLabel(context.packageManager)?.toString()?.trim().orEmpty()
        if (label.isEmpty()) return null

        return InstalledAppEntry(
            label = label,
            packageName = activityInfo.packageName,
            className = activityInfo.name,
        )
    }
}
