package com.goreecloud.index.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.goreecloud.index.core.IndexProviderIssue
import com.goreecloud.index.core.IndexProviderIssueKind
import com.goreecloud.index.core.IndexResult
import com.goreecloud.index.core.IndexResultType
import com.goreecloud.index.core.IndexSearchSnapshot

@Composable
fun IndexRoot(
    initialQuery: String,
    onSearch: suspend (String) -> IndexSearchSnapshot,
    onOpenResult: (IndexResult) -> Unit,
) {
    var query by rememberSaveable(initialQuery) { mutableStateOf(initialQuery) }
    var snapshot by remember { mutableStateOf(IndexSearchSnapshot()) }
    var searching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(query) {
        searching = true
        try {
            snapshot = onSearch(query)
        } finally {
            searching = false
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboard?.show()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Text(
                text = "GoreeCloud Index",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = "Universal search, source by source",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(18.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                label = { Text("Search this device") },
                placeholder = { Text("Applications, Settings, and authorized sources") },
                keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
            )

            Spacer(Modifier.height(12.dp))

            SourceStatusCard()

            if (searching) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Searching authorized sources…",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            snapshot.providerIssues.forEach { issue ->
                Spacer(Modifier.height(12.dp))
                ProviderIssueCard(issue)
            }

            Spacer(Modifier.height(16.dp))

            val results = snapshot.results
            if (results.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = when {
                            searching -> "Searching…"
                            snapshot.providerIssues.any {
                                it.kind == IndexProviderIssueKind.FAILED ||
                                    it.kind == IndexProviderIssueKind.TIMED_OUT
                            } -> "Some search sources are temporarily unavailable"
                            query.isBlank() -> "Start typing to search authorized sources"
                            else -> "No matches in available sources"
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Text(
                    text = if (query.isBlank()) "Applications" else "Results",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = results,
                        key = { "${it.providerId}:${it.id}" },
                    ) { result ->
                        IndexResultRow(
                            result = result,
                            onClick = { onOpenResult(result) },
                        )
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SourceStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "Applications · On-device · Active",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Settings · On-device · Active navigation",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = "Contacts · On-device · Authority gated",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = "Settings results are a static navigation catalog and do not read setting values. Contacts cannot run until Android contact permission plus Privacy Shield and GoreeCloud Identity authority evidence are all available. Files, calendar, GoreeCloud services, optional third-party services, and web results remain separately gated provider work.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
private fun ProviderIssueCard(issue: IndexProviderIssue) {
    val authorizationRequired = issue.kind == IndexProviderIssueKind.AUTHORIZATION_REQUIRED
    val title = when (issue.kind) {
        IndexProviderIssueKind.FAILED -> "${issue.providerName} temporarily unavailable"
        IndexProviderIssueKind.TIMED_OUT -> "${issue.providerName} took too long"
        IndexProviderIssueKind.AUTHORIZATION_REQUIRED -> "${issue.providerName} not enabled"
    }
    val detail = when (issue.kind) {
        IndexProviderIssueKind.FAILED ->
            "Index isolated the provider failure and kept results from healthy providers."
        IndexProviderIssueKind.TIMED_OUT ->
            "Index stopped waiting at the provider's bounded timeout and kept results from healthy providers."
        IndexProviderIssueKind.AUTHORIZATION_REQUIRED ->
            "Required permission or platform authority evidence is incomplete, so Index did not send this provider the query."
    }
    val containerColor = if (authorizationRequired) {
        MaterialTheme.colorScheme.surfaceContainerHigh
    } else {
        MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = if (authorizationRequired) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onErrorContainer
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
private fun IndexResultRow(
    result: IndexResult,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clickable(enabled = result.action != null, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = result.title.firstOrNull()?.uppercaseChar()?.toString() ?: "•",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = result.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                )
                result.subtitle?.takeIf { it.isNotBlank() }?.let { subtitle ->
                    Text(
                        text = subtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Text(
                text = sourceLabel(result.type),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private fun sourceLabel(type: IndexResultType): String = when (type) {
    IndexResultType.APP -> "Apps · On-device"
    IndexResultType.ACTION -> "Action"
    IndexResultType.CONTACT -> "People · On-device"
    IndexResultType.FILE -> "Files"
    IndexResultType.CALENDAR -> "Calendar"
    IndexResultType.MEDIA -> "Media"
    IndexResultType.SETTING -> "Settings · On-device"
    IndexResultType.GOREECLOUD -> "GoreeCloud"
    IndexResultType.DEVICE -> "Device"
    IndexResultType.WEB -> "Web"
}
