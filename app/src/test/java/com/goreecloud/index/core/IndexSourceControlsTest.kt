package com.goreecloud.index.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IndexSourceControlsTest {
    @Test
    fun developmentSourcePolicyAllowsOnlyIntegratedLocalProviders() {
        val requested = setOf(
            GoreeCloudIndexContract.PROVIDER_APPS,
            GoreeCloudIndexContract.PROVIDER_SETTINGS,
            GoreeCloudIndexContract.PROVIDER_CONTACTS,
            GoreeCloudIndexContract.PROVIDER_SEARCH,
            "unreviewed-provider",
        )

        val enabled = IndexDevelopmentSourcePolicy.sanitizeEnabledProviderIds(requested)

        assertEquals(IndexDevelopmentSourcePolicy.selectableProviderIds, enabled)
        assertFalse(GoreeCloudIndexContract.PROVIDER_SEARCH in enabled)
        assertFalse("unreviewed-provider" in enabled)
    }

    @Test
    fun developmentSourcePolicyAlwaysEnforcesLocalOnlyExecution() {
        val context = IndexDevelopmentSourcePolicy.executionContext(
            requestedProviderIds = setOf(GoreeCloudIndexContract.PROVIDER_APPS),
        )

        assertTrue(context.localOnly)
        assertEquals(setOf(GoreeCloudIndexContract.PROVIDER_APPS), context.allowedProviderIds)
    }

    @Test
    fun developmentSourcePolicyPreservesAuthorityEvidenceWithoutGrantingNewScope() {
        val contactsAuthority = IndexProviderAuthority(
            privacyShield = IndexAuthorityEvidence(
                outcome = IndexAuthorityOutcome.ALLOW,
                reference = "privacy-reference",
            ),
        )

        val context = IndexDevelopmentSourcePolicy.executionContext(
            requestedProviderIds = setOf(
                GoreeCloudIndexContract.PROVIDER_CONTACTS,
                GoreeCloudIndexContract.PROVIDER_SEARCH,
            ),
            providerAuthorities = mapOf(
                GoreeCloudIndexContract.PROVIDER_CONTACTS to contactsAuthority,
            ),
        )

        assertEquals(setOf(GoreeCloudIndexContract.PROVIDER_CONTACTS), context.allowedProviderIds)
        assertEquals(contactsAuthority, context.providerAuthorities[GoreeCloudIndexContract.PROVIDER_CONTACTS])
        assertFalse(GoreeCloudIndexContract.PROVIDER_SEARCH in context.allowedProviderIds)
    }
}
