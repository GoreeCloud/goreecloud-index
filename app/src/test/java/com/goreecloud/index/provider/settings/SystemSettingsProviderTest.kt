package com.goreecloud.index.provider.settings

import com.goreecloud.index.core.GoreeCloudIndexContract
import com.goreecloud.index.core.IndexAction
import com.goreecloud.index.core.IndexProcessingLocation
import com.goreecloud.index.core.IndexQuery
import com.goreecloud.index.core.IndexResultType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SystemSettingsProviderTest {
    private val provider = SystemSettingsProvider()

    @Test
    fun providerIsLocalNonBrowsingAndPermissionFree() {
        assertEquals(GoreeCloudIndexContract.PROVIDER_SETTINGS, provider.providerId)
        assertEquals(IndexProcessingLocation.LOCAL, provider.processingLocation)
        assertFalse(provider.supportsEmptyQuery)
        assertTrue(provider.authorityRequirements.isEmpty())
    }

    @Test
    fun blankQueryReturnsNoSettingsEnumeration() = runTest {
        assertTrue(provider.search(IndexQuery("   ")).isEmpty())
    }

    @Test
    fun wifiQueryReturnsTypedWhitelistedDestination() = runTest {
        val result = provider.search(IndexQuery("wifi"))
            .single { it.title == "Wi-Fi" }

        assertEquals(IndexResultType.SETTING, result.type)
        assertEquals("Android Settings · On-device", result.subtitle)
        val action = result.action as IndexAction.OpenSystemSetting
        assertEquals(SystemSettingsProvider.ACTION_WIFI_SETTINGS, action.action)
        assertTrue(SystemSettingsProvider.isAllowedAction(action.action))
    }

    @Test
    fun keywordSearchCanFindDestinationWithoutReadingSettingValues() = runTest {
        val titles = provider.search(IndexQuery("network")).map { it.title }
        assertTrue("Wi-Fi" in titles)
    }

    @Test
    fun unknownQueryReturnsNoResults() = runTest {
        assertTrue(provider.search(IndexQuery("goreecloud-provider-nonsense")).isEmpty())
    }

    @Test
    fun destinationCatalogUsesUniqueIdsAndWhitelistedActions() {
        val destinations = SystemSettingsProvider.destinationsForTest()

        assertEquals(destinations.size, destinations.map { it.id }.toSet().size)
        assertEquals(destinations.size, destinations.map { it.action }.toSet().size)
        assertTrue(destinations.all { SystemSettingsProvider.isAllowedAction(it.action) })
        assertFalse(SystemSettingsProvider.isAllowedAction("android.intent.action.VIEW"))
        assertFalse(SystemSettingsProvider.isAllowedAction("https://example.com"))
    }

    @Test
    fun maxResultsRemainsBoundedByQuery() = runTest {
        val results = provider.search(IndexQuery(text = "settings", maxResults = 1))
        assertTrue(results.size <= 1)
    }
}
