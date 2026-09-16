package com.goreecloud.index.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlazeV15ContractTest {
    @Test
    fun repositoryContractTargetsCurrentStableGlazeRelease() {
        assertEquals("1.5.0", GlazeV15Contract.VERSION)
        assertEquals(
            "b7fa8164bfdeaa1dc0acb21b770e7601120da04e",
            GlazeV15Contract.STABLE_RELEASE_REVISION,
        )
        assertEquals(
            "ee1032a0822ab8e103f8afe48e5c1859fde65cc9",
            GlazeV15Contract.SOURCE_QUALIFICATION_ANCHOR,
        )
        assertEquals("1.4.1", GlazeV15Contract.OPTICAL_BASELINE_VERSION)
        assertEquals("1.4.1", GlazeV15Contract.ROLLBACK_BASELINE)
        assertEquals("js/glaze-v1.5.0.mjs", GlazeV15Contract.STABLE_RUNTIME_ENTRYPOINT)
    }

    @Test
    fun inheritedOpticalContextDoesNotGrantRemoteOrSensorAuthority() {
        assertFalse(GlazeV15Contract.REMOTE_OPTICAL_CONTEXT_ALLOWED)
        assertFalse(GlazeV15Contract.TELEMETRY_REQUIRED_FOR_OPTICS)
        assertFalse(GlazeV15Contract.CAMERA_REQUIRED_FOR_OPTICS)
        assertEquals(0.08f, GlazeV15Contract.MAX_ENVIRONMENTAL_MEMORY_TINT_INFLUENCE)
    }

    @Test
    fun accessibilityStateForcesSolidOpticalMode() {
        assertEquals(
            GlazeV15Contract.OpticalMode.SolidAccessible,
            GlazeV15Contract.opticalMode(forcedColors = true, reducedTransparency = false),
        )
        assertEquals(
            GlazeV15Contract.OpticalMode.SolidAccessible,
            GlazeV15Contract.opticalMode(forcedColors = false, reducedTransparency = true),
        )
    }

    @Test
    fun duplicateSearchCapabilityFailsClosedWithoutProviderPrecedence() {
        val result = GlazeV15Contract.resolveAction(
            requiredCapabilityIds = setOf("service.search"),
            capabilities = listOf(
                GlazeV15Contract.CapabilityRecord(
                    id = "service.search",
                    state = GlazeV15Contract.CapabilityState.Available,
                    authority = "goreecloud-search",
                ),
                GlazeV15Contract.CapabilityRecord(
                    id = "service.search",
                    state = GlazeV15Contract.CapabilityState.Available,
                    authority = "duplicate-provider",
                ),
            ),
        )

        assertFalse(result.enabled)
        assertEquals(GlazeV15Contract.CapabilityState.Conflict, result.state)
        assertTrue(result.reasonCodes.contains("capability-conflict:service.search"))
        assertFalse(result.automaticExecutionAllowed)
        assertFalse(result.authorizationInferred)
        assertFalse(result.providerPrecedenceInferred)
    }

    @Test
    fun restrictedRemoteSearchPresentationCannotManufactureAuthorization() {
        val result = GlazeV15Contract.resolveAction(
            requiredCapabilityIds = setOf("authorization.remote-search"),
            capabilities = listOf(
                GlazeV15Contract.CapabilityRecord(
                    id = "authorization.remote-search",
                    state = GlazeV15Contract.CapabilityState.Restricted,
                    authority = "privacy-shield",
                ),
            ),
        )

        assertFalse(result.enabled)
        assertEquals(GlazeV15Contract.CapabilityState.Restricted, result.state)
        assertTrue(result.reasonCodes.contains("restricted-by-authority:authorization.remote-search"))
        assertFalse(result.authorizationInferred)
    }
}
