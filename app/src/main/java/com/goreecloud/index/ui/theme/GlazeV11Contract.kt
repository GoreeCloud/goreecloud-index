package com.goreecloud.index.ui.theme

/** Repository-local GLAZE UI V1.1 source contract for GoreeCloud Index. */
object GlazeV11Contract {
    const val VERSION = "1.1.0"
    const val STABLE_TAG = "v1.1.0"
    const val STABLE_RELEASE_REVISION = "15cc76d2bcd4065552dc31c77145b63f34d9e7b2"
    const val APPROVED_VISUAL_SOURCE = "8ea1f789bbabf943c3359514dc1506b24fa3c51b"
    const val OPTICAL_CONTRACT = "contracts/v1.1/optical-refinement.json"

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

    const val LIGHT_ORIGIN = "upper-left"
    const val NESTED_BACKDROP_BLUR_ALLOWED = false
    const val ENVIRONMENTAL_COLOR_MEMORY_REQUIRED = false
    const val REMOTE_COLOR_DERIVATION_ALLOWED = false

    enum class Appearance {
        Light,
        Dark,
        DeepDark,
    }

    fun targetFloorDp(touchAssistance: Boolean): Int =
        if (touchAssistance) TOUCH_ASSISTANCE_TARGET_DP else GENERAL_TARGET_DP
}
