package com.goreecloud.index

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.goreecloud.index.core.GoreeCloudIndexContract
import com.goreecloud.index.core.IndexAction
import com.goreecloud.index.core.IndexAuthorityEvidence
import com.goreecloud.index.core.IndexExecutionContext
import com.goreecloud.index.core.IndexProviderAuthority
import com.goreecloud.index.core.IndexQueryEngine
import com.goreecloud.index.core.IndexResult
import com.goreecloud.index.provider.apps.InstalledAppsProvider
import com.goreecloud.index.provider.contacts.ContactsProvider
import com.goreecloud.index.ui.IndexRoot
import com.goreecloud.index.ui.theme.GoreeCloudIndexTheme

class MainActivity : ComponentActivity() {
    private lateinit var appsProvider: InstalledAppsProvider
    private lateinit var contactsProvider: ContactsProvider
    private lateinit var queryEngine: IndexQueryEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appsProvider = InstalledAppsProvider(this)
        contactsProvider = ContactsProvider(this)
        queryEngine = IndexQueryEngine(listOf(appsProvider, contactsProvider))

        setContent {
            GoreeCloudIndexTheme {
                IndexRoot(
                    initialQuery = intent.getStringExtra(GoreeCloudIndexContract.EXTRA_QUERY).orEmpty(),
                    onSearch = { query ->
                        queryEngine.search(
                            rawQuery = query,
                            executionContext = executionContext(),
                        )
                    },
                    onOpenResult = ::openResult,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::appsProvider.isInitialized) {
            appsProvider.refresh()
        }
    }

    private fun executionContext(): IndexExecutionContext = IndexExecutionContext(
        allowedProviderIds = setOf(
            GoreeCloudIndexContract.PROVIDER_APPS,
            GoreeCloudIndexContract.PROVIDER_CONTACTS,
        ),
        localOnly = true,
        providerAuthorities = mapOf(
            GoreeCloudIndexContract.PROVIDER_CONTACTS to IndexProviderAuthority(
                androidPermissionGranted = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS,
                ) == PackageManager.PERMISSION_GRANTED,
                privacyShield = IndexAuthorityEvidence.unavailable(),
                identity = IndexAuthorityEvidence.unavailable(),
            ),
        ),
    )

    private fun openResult(result: IndexResult) {
        when (val action = result.action) {
            is IndexAction.LaunchActivity -> openApplication(action)
            is IndexAction.ViewContact -> openContact(action)
            null -> Unit
        }
    }

    private fun openApplication(action: IndexAction.LaunchActivity) {
        val launchIntent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setComponent(ComponentName(action.packageName, action.className))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        runCatching { startActivity(launchIntent) }
            .onFailure {
                Toast.makeText(
                    this,
                    "Unable to open this application.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    private fun openContact(action: IndexAction.ViewContact) {
        val uri = Uri.parse(action.uri)
        val validContactUri = uri.scheme == "content" &&
            uri.authority == ContactsContract.AUTHORITY &&
            uri.pathSegments.firstOrNull() == "contacts"
        if (!validContactUri) {
            Toast.makeText(this, "Unable to open this contact.", Toast.LENGTH_SHORT).show()
            return
        }

        runCatching {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }.onFailure {
            Toast.makeText(this, "Unable to open this contact.", Toast.LENGTH_SHORT).show()
        }
    }
}
