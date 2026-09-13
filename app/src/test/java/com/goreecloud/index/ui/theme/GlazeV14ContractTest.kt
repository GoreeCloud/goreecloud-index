package com.goreecloud.index.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class GlazeV14ContractTest {
    @Test
    fun repositoryContractTargetsCurrentStableGlazeRelease() {
        assertEquals("1.4.0", GlazeV14Contract.VERSION)
        assertEquals("1.3.0", GlazeV14Contract.ROLLBACK_BASELINE)
        assertEquals("1.4.1", GlazeV14Contract.DEFERRED_HUMAN_QUALIFICATION)
        assertEquals("css/glaze-v1.4.0.css", GlazeV14Contract.STABLE_WEB_ENTRYPOINT)
        assertEquals("js/glaze-v1.4.0.mjs", GlazeV14Contract.STABLE_RUNTIME_ENTRYPOINT)
        assertEquals("js/glaze-v1.4-optical-engine.mjs", GlazeV14Contract.OPTICAL_ENGINE)
    }

    @Test
    fun opticalContextDoesNotGrantRemoteOrSensorAuthority() {
        assertFalse(GlazeV14Contract.REMOTE_OPTICAL_CONTEXT_ALLOWED)
        assertFalse(GlazeV14Contract.TELEMETRY_REQUIRED_FOR_OPTICS)
        assertFalse(GlazeV14Contract.CAMERA_REQUIRED_FOR_OPTICS)
        assertEquals(0.08f, GlazeV14Contract.MAX_ENVIRONMENTAL_MEMORY_TINT_INFLUENCE)
    }

    @Test
    fun accessibilityStateForcesSolidOpticalMode() {
        assertEquals(
            GlazeV14Contract.OpticalMode.SolidAccessible,
            GlazeV14Contract.opticalMode(forcedColors = true, reducedTransparency = false),
        )
        assertEquals(
            GlazeV14Contract.OpticalMode.SolidAccessible,
            GlazeV14Contract.opticalMode(forcedColors = false, reducedTransparency = true),
        )
        assertEquals(
            GlazeV14Contract.OpticalMode.AdaptiveOptical,
            GlazeV14Contract.opticalMode(forcedColors = false, reducedTransparency = false),
        )
    }

    @Test
    fun touchTargetFloorRemainsExplicit() {
        assertEquals(48, GlazeV14Contract.targetFloorDp(touchAssistance = false))
        assertEquals(56, GlazeV14Contract.targetFloorDp(touchAssistance = true))
    }
}
