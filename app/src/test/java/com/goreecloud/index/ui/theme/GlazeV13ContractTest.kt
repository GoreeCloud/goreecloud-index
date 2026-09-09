package com.goreecloud.index.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class GlazeV13ContractTest {
    @Test
    fun targetsCurrentStableSourceAuthority() {
        assertEquals("1.3.0", GlazeV13Contract.VERSION)
        assertEquals("GLAZE UI V1.3 — Adaptive Resonance", GlazeV13Contract.PRODUCT_LABEL)
        assertEquals(
            "fc7cc91d2eace8da2371371c2855c24cbcb326a1",
            GlazeV13Contract.STABLE_SOURCE_INTEGRATION_ANCHOR,
        )
        assertEquals("css/glaze-v1.3.0.css", GlazeV13Contract.STABLE_WEB_ENTRYPOINT)
        assertEquals("js/glaze-v1.3.0.mjs", GlazeV13Contract.STABLE_RUNTIME_ENTRYPOINT)
        assertEquals("1.2.0", GlazeV13Contract.ROLLBACK_BASELINE)
    }

    @Test
    fun preservesAccessibilityAndMaterialBounds() {
        assertEquals(48, GlazeV13Contract.targetFloorDp(touchAssistance = false))
        assertEquals(56, GlazeV13Contract.targetFloorDp(touchAssistance = true))
        assertEquals(1, GlazeV13Contract.MAX_DOMINANT_GLAZE_PANELS)
        assertEquals(3, GlazeV13Contract.MAX_SMALL_FLOATING_GLAZE_CONTROLS)
        assertFalse(GlazeV13Contract.NESTED_BACKDROP_BLUR_ALLOWED)
    }

    @Test
    fun atmosphereCannotDependOnRemoteColorDerivation() {
        assertFalse(GlazeV13Contract.REMOTE_COLOR_DERIVATION_ALLOWED)
    }
}
