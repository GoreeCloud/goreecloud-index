package com.goreecloud.index.provider.contacts

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.goreecloud.index.core.GoreeCloudIndexContract
import com.goreecloud.index.core.IndexAction
import com.goreecloud.index.core.IndexAuthorityRequirement
import com.goreecloud.index.core.IndexProcessingLocation
import com.goreecloud.index.core.IndexProvider
import com.goreecloud.index.core.IndexQuery
import com.goreecloud.index.core.IndexResult
import com.goreecloud.index.core.IndexResultType
import com.goreecloud.index.core.IndexTextMatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class ContactsProvider(
    private val context: Context,
) : IndexProvider {
    override val providerId: String = GoreeCloudIndexContract.PROVIDER_CONTACTS
    override val displayName: String = "Contacts"
    override val processingLocation: IndexProcessingLocation = IndexProcessingLocation.LOCAL
    override val timeoutMillis: Long = 750L
    override val supportsEmptyQuery: Boolean = false
    override val authorityRequirements: Set<IndexAuthorityRequirement> = setOf(
        IndexAuthorityRequirement.ANDROID_RUNTIME_PERMISSION,
        IndexAuthorityRequirement.PRIVACY_SHIELD,
        IndexAuthorityRequirement.GOREECLOUD_IDENTITY,
    )

    override suspend fun search(query: IndexQuery): List<IndexResult> {
        val normalizedQuery = query.text.trim()
        if (normalizedQuery.isEmpty()) return emptyList()

        return withContext(Dispatchers.IO) {
            val filterUri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_FILTER_URI,
                Uri.encode(normalizedQuery),
            )
            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            )
            val results = mutableListOf<IndexResult>()
            val limit = query.maxResults.coerceIn(1, MAX_CONTACT_RESULTS)

            context.contentResolver.query(
                filterUri,
                projection,
                null,
                null,
                null,
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                val lookupColumn = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY)
                val nameColumn = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

                while (cursor.moveToNext() && results.size < limit) {
                    val contactId = cursor.getLong(idColumn)
                    val lookupKey = cursor.getString(lookupColumn)?.trim().orEmpty()
                    val displayName = cursor.getString(nameColumn)?.trim().orEmpty()
                    if (lookupKey.isEmpty() || displayName.isEmpty()) continue

                    val score = IndexTextMatcher.score(
                        query = normalizedQuery,
                        title = displayName,
                    ) ?: continue
                    val lookupUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey)
                        ?: continue

                    results += IndexResult(
                        id = lookupKey,
                        providerId = providerId,
                        type = IndexResultType.CONTACT,
                        title = displayName,
                        subtitle = "Contact · On-device",
                        score = score,
                        action = IndexAction.ViewContact(lookupUri.toString()),
                    )
                }
            }

            results.sortedWith(
                compareByDescending<IndexResult> { it.score }
                    .thenBy { it.title.lowercase(Locale.ROOT) }
                    .thenBy { it.id }
            )
        }
    }

    private companion object {
        const val MAX_CONTACT_RESULTS = 100
    }
}
