package com.goreecloud.index.provider.search

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class GoreeCloudSearchPrivacyAuthorizationIntentTest {
    @Test
    fun defaultIntentContainsCanonicalPrivacyShieldDecisionFields() {
        val request = GoreeCloudSearchPrivacyAuthorizationRequest()

        assertEquals("goreecloud-index", request.requesterId)
        assertEquals("application", request.requesterType)
        assertEquals("goreecloud.search.query", request.resourceId)
        assertEquals("query_text", request.resourceClassification)
        assertEquals("search.query", request.operation)
        assertEquals("internet_search", request.purpose)
        assertEquals("private_goreecloud", request.processingZone)
        assertEquals("https://search.goreecloud.com", request.destination)
        assertEquals("none", request.retentionMode)
        assertFalse(request.externalDisclosure)
    }
}
