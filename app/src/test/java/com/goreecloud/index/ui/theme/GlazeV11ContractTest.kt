package com.goreecloud.index.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class GlazeV11ContractTest {
    @Test
    fun targetsCurrentStableRelease() {
        assertEquals("1.1.0", GlazeV11Contract.VERSION)
        assertEquals("v1.1.0", GlazeV11Contract.STABLE_TAG)
        assertEquals(
            "15cc76d2bcd4065552dc31c77145b63f34d9e7b2",
            GlazeV11Contract.STABLE_RELEASE_REVISION,
        )
        assertEquals(
            "8ea1f789bbabf943c3359514dc1506b24fa3c51b",
            GlazeV11Contract.APPROVED_VISUAL_SOURCE,
        )
    }

    @Test
    fun preservesAccessibilityAndMaterialBounds() {
        assertEquals(48, GlazeV11Contract.targetFloorDp(touchAssistance = false))
        assertEquals(56, GlazeV11Contract.targetFloorDp(touchAssistance = true))
        assertEquals(1, GlazeV11Contract.MAX_DOMINANT_GLAZE_PANELS)
        assertEquals(3, GlazeV11Contract.MAX_SMALL_FLOATING_GLAZE_CONTROLS)
        assertFalse(GlazeV11Contract.NESTED_BACKDROP_BLUR_ALLOWED)
    }

    @Test
    fun atmosphereCannotRequireEnvironmentalSampling() {
        assertEquals("upper-left", GlazeV11Contract.LIGHT_ORIGIN)
        assertFalse(GlazeV11Contract.ENVIRONMENTAL_COLOR_MEMORY_REQUIRED)
        assertFalse(GlazeV11Contract.REMOTE_COLOR_DERIVATION_ALLOWED)
    }
}
