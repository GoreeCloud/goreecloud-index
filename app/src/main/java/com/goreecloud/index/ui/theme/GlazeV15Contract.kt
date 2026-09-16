package com.goreecloud.index.ui.theme

/**
 * Repository-local native projection of the bounded GLAZE UI V1.5.0 Stable
 * presentation contract consumed by GoreeCloud Index.
 *
 * V1.5 inherits the reviewed V1.4.1 optical/material baseline and adds
 * context/capability-aware presentation semantics. Index supplies already-owned
 * capability state; Glaze never grants authorization or chooses provider authority.
 */
object GlazeV15Contract {
    const val VERSION = "1.5.0"
    const val PRODUCT_LABEL = "GLAZE UI V1.5 — Contextual + Capability Awareness"
    const val STABLE_RELEASE_REVISION = "b7fa8164bfdeaa1dc0acb21b770e7601120da04e"
    const val SOURCE_QUALIFICATION_ANCHOR = "ee1032a0822ab8e103f8afe48e5c1859fde65cc9"
    const val STABLE_RUNTIME_ENTRYPOINT = "js/glaze-v1.5.0.mjs"
    const val OPTICAL_BASELINE_VERSION = "1.4.1"
    const val OPTICAL_BASELINE_REVISION = "4fab9da0fad2e5c974e0e66ec88632c61745751c"
    const val ROLLBACK_BASELINE = "1.4.1"

    // V1.5 inherits the accepted V1.4.1 visual baseline for native consumer mappings.
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

    enum class CapabilityState {
        Available,
        TemporarilyUnavailable,
        Restricted,
        Unknown,
        Conflict,
    }

    data class CapabilityRecord(
        val id: String,
        val state: CapabilityState,
        val authority: String,
    )

    data class ActionPresentation(
        val enabled: Boolean,
        val state: CapabilityState,
        val reasonCodes: Set<String>,
        val automaticExecutionAllowed: Boolean = false,
        val authorizationInferred: Boolean = false,
        val providerPrecedenceInferred: Boolean = false,
    )

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

    fun resolveAction(
        requiredCapabilityIds: Set<String>,
        capabilities: Collection<CapabilityRecord>,
    ): ActionPresentation {
        if (requiredCapabilityIds.isEmpty()) {
            return ActionPresentation(
                enabled = true,
                state = CapabilityState.Available,
                reasonCodes = emptySet(),
            )
        }

        val recordsById = capabilities.groupBy { it.id }
        val reasons = linkedSetOf<String>()
        var resolved = CapabilityState.Available

        for (capabilityId in requiredCapabilityIds.sorted()) {
            val records = recordsById[capabilityId].orEmpty()
            val state = when {
                records.isEmpty() -> CapabilityState.Unknown
                records.size > 1 -> CapabilityState.Conflict
                else -> records.single().state
            }
            resolved = strongest(resolved, state)
            when (state) {
                CapabilityState.Available -> Unit
                CapabilityState.TemporarilyUnavailable -> reasons += "temporarily-unavailable:$capabilityId"
                CapabilityState.Restricted -> reasons += "restricted-by-authority:$capabilityId"
                CapabilityState.Unknown -> reasons += "capability-unknown:$capabilityId"
                CapabilityState.Conflict -> reasons += "capability-conflict:$capabilityId"
            }
        }

        return ActionPresentation(
            enabled = resolved == CapabilityState.Available,
            state = resolved,
            reasonCodes = reasons,
        )
    }

    private fun strongest(current: CapabilityState, candidate: CapabilityState): CapabilityState {
        val order = mapOf(
            CapabilityState.Available to 0,
            CapabilityState.TemporarilyUnavailable to 1,
            CapabilityState.Restricted to 2,
            CapabilityState.Unknown to 3,
            CapabilityState.Conflict to 4,
        )
        return if (order.getValue(candidate) > order.getValue(current)) candidate else current
    }
}
