package com.goreecloud.index.ui.theme

/** Repository-local GLAZE UI V1.3 source contract for GoreeCloud Index. */
object GlazeV13Contract {
    const val VERSION = "1.3.0"
    const val PRODUCT_LABEL = "GLAZE UI V1.3 — Adaptive Resonance"
    const val STABLE_SOURCE_INTEGRATION_ANCHOR = "fc7cc91d2eace8da2371371c2855c24cbcb326a1"
    const val STABLE_WEB_ENTRYPOINT = "css/glaze-v1.3.0.css"
    const val STABLE_RUNTIME_ENTRYPOINT = "js/glaze-v1.3.0.mjs"
    const val ROLLBACK_BASELINE = "1.2.0"

    // V1.3 inherits the verified V1.2 neutral/frosted rendering foundation.
    const val DEEP_TEAL = 0xFF0F6B6F
    const val MINERAL_TEAL = 0xFF1C8A8D
    const val SOFT_AQUA = 0xFF8FD6D2
    const val SOFT_AMBER = 0xFFD9A35F
    const val CHAMPAGNE_GOLD = 0xFFE7C78A
    const val CANVAS_BLACK = 0xFF081016
    const val DEEP_GRAPHITE = 0xFF101A20
    const val SLATE_GRAPHITE = 0xFF18252B

    const val GENERAL_TARGET_DP = 48
    const val TOUCH_ASSISTANCE_TARGET_DP = 56
    const val MAX_DOMINANT_GLAZE_PANELS = 1
    const val MAX_SMALL_FLOATING_GLAZE_CONTROLS = 3

    const val NESTED_BACKDROP_BLUR_ALLOWED = false
    const val REMOTE_COLOR_DERIVATION_ALLOWED = false

    enum class Appearance {
        Light,
        Dark,
        DeepDark,
    }

    fun targetFloorDp(touchAssistance: Boolean): Int =
        if (touchAssistance) TOUCH_ASSISTANCE_TARGET_DP else GENERAL_TARGET_DP
}
