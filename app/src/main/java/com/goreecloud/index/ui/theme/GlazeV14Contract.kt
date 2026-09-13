package com.goreecloud.index.ui.theme

/**
 * Repository-local native projection of the current GLAZE UI V1.4 Stable authority.
 *
 * V1.4 preserves the V1.3 token/component baseline and adds bounded Optical Intelligence.
 * Index does not treat this object as authorization to collect contextual data or to claim
 * human/physical-device qualification; those remain separate consumer acceptance gates.
 */
object GlazeV14Contract {
    const val VERSION = "1.4.0"
    const val PRODUCT_LABEL = "GLAZE UI V1.4 — Optical Intelligence"
    const val STABLE_RELEASE_REVISION = "84cb3db4884042f0fa25ed6d475a127fb110f596"
    const val SOURCE_INTEGRATION_ANCHOR = "a20374734dae6a119b28448f5e6b3232253b6da7"
    const val STABLE_WEB_ENTRYPOINT = "css/glaze-v1.4.0.css"
    const val STABLE_RUNTIME_ENTRYPOINT = "js/glaze-v1.4.0.mjs"
    const val OPTICAL_ENGINE = "js/glaze-v1.4-optical-engine.mjs"
    const val ROLLBACK_BASELINE = "1.3.0"
    const val DEFERRED_HUMAN_QUALIFICATION = "1.4.1"

    // V1.4 inherits the V1.3 neutral/frosted semantic palette for consumer mappings.
    const val DEEP_TEAL = 0xFF0F6B6F
    const val MINERAL_TEAL = 0xFF1C8A8D
    const val SOFT_AQUA = 0xFF8FD6D2
    const val FROST_WHITE = 0xFFF4F8FA
    const val CRYSTAL_WHITE = 0xFFFBFDFE
    const val ICE_BLUE = 0xFFDCECF6
    const val CLOUD_GRAY = 0xFFDCE3E8
    const val SLATE_GRAY = 0xFF7E8D99
    const val COOL_GRAPHITE = 0xFF151C22
    const val DEEP_GRAPHITE = 0xFF0E1419
    const val BLUE_BLACK = 0xFF070C11

    const val GENERAL_TARGET_DP = 48
    const val TOUCH_ASSISTANCE_TARGET_DP = 56
    const val MAX_ENVIRONMENTAL_MEMORY_TINT_INFLUENCE = 0.08f

    const val REMOTE_OPTICAL_CONTEXT_ALLOWED = false
    const val TELEMETRY_REQUIRED_FOR_OPTICS = false
    const val CAMERA_REQUIRED_FOR_OPTICS = false

    enum class OpticalMode {
        AdaptiveOptical,
        SolidAccessible,
    }

    fun opticalMode(
        forcedColors: Boolean,
        reducedTransparency: Boolean,
    ): OpticalMode = if (forcedColors || reducedTransparency) {
        OpticalMode.SolidAccessible
    } else {
        OpticalMode.AdaptiveOptical
    }

    fun targetFloorDp(touchAssistance: Boolean): Int =
        if (touchAssistance) TOUCH_ASSISTANCE_TARGET_DP else GENERAL_TARGET_DP
}
