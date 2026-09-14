package com.goreecloud.index.provider.search

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GoreeCloudSearchSensitiveRenderingTest {
    @Test
    fun searchRequestDebugRenderingRedactsQueryAndCapabilityReference() {
        val query = "private medical research query"
        val reference = "psc_sensitive_authority_reference"
        val rendered = GoreeCloudSearchRequest(
            query = query,
            limit = 5,
            privacyCapabilityReference = reference,
        ).toString()

        assertFalse(rendered.contains(query))
        assertFalse(rendered.contains(reference))
        assertTrue(rendered.contains("query=<redacted>"))
        assertTrue(rendered.contains("privacyCapabilityReference=<redacted>"))
    }

    @Test
    fun privacyAuthorizationDebugRenderingRedactsCapabilityReference() {
        val reference = "psc_sensitive_authority_reference"
        val rendered = GoreeCloudSearchPrivacyAuthorization(reference).toString()

        assertFalse(rendered.contains(reference))
        assertTrue(rendered.contains("capabilityTokenReference=<redacted>"))
    }
}
