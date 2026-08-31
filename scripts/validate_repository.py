from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

required = [
    "README.md",
    "SPECIFICATIONS.md",
    "FEATURES.md",
    "BENEFITS.md",
    "COMPETITIVE-OBJECTIVES.md",
    "CAPABILITIES.md",
    "ARCHITECTURE.md",
    "CONFORMANCE.md",
    "USER-MANUAL.md",
    "app/build.gradle.kts",
    "app/src/main/AndroidManifest.xml",
    "app/src/main/java/com/goreecloud/index/MainActivity.kt",
    "app/src/main/java/com/goreecloud/index/core/IndexContract.kt",
    "app/src/main/java/com/goreecloud/index/provider/apps/InstalledAppsProvider.kt",
    "app/src/main/java/com/goreecloud/index/ui/IndexRoot.kt",
    "app/src/test/java/com/goreecloud/index/core/IndexQueryEngineTest.kt",
]

missing = [path for path in required if not (ROOT / path).is_file()]
if missing:
    raise SystemExit(f"Missing required repository files: {', '.join(missing)}")

manifest = (ROOT / "app/src/main/AndroidManifest.xml").read_text(encoding="utf-8")
if "android.permission.INTERNET" in manifest:
    raise SystemExit("Current GoreeCloud Index local-search slice must not request INTERNET permission")
if "android.permission.QUERY_ALL_PACKAGES" in manifest:
    raise SystemExit("GoreeCloud Index must not request QUERY_ALL_PACKAGES for installed-app discovery")
for expected in [
    "android.intent.action.MAIN",
    "android.intent.category.LAUNCHER",
    "com.goreecloud.index.action.SEARCH",
]:
    if expected not in manifest:
        raise SystemExit(f"Missing Android search/launcher contract: {expected}")

build = (ROOT / "app/build.gradle.kts").read_text(encoding="utf-8")
for expected in [
    'applicationId = "com.goreecloud.index"',
    'applicationIdSuffix = ".dev"',
    'compileSdk = 37',
    'targetSdk = 36',
    'minSdk = 26',
    'versionCode = 2',
    'versionName = "0.2.0-dev"',
    'kotlinx-coroutines-android:1.11.0',
    'kotlinx-coroutines-test:1.11.0',
]:
    if expected not in build:
        raise SystemExit(f"Missing Android/runtime build contract: {expected}")

core = (ROOT / "app/src/main/java/com/goreecloud/index/core/IndexContract.kt").read_text(encoding="utf-8")
for expected in [
    "IndexProcessingLocation",
    "IndexProviderIssueKind",
    "IndexExecutionContext",
    "allowedProviderIds",
    "localOnly",
    "processingLocation",
    "timeoutMillis",
    "suspend fun search",
    "supervisorScope",
    "async(providerDispatcher)",
    "withTimeout",
    "TimeoutCancellationException",
    "CancellationException",
    "throw cancellation",
    "IndexProviderIssueKind.TIMED_OUT",
    "IndexProviderIssueKind.FAILED",
    "sortedWith(ranking)",
    "distinctBy",
]:
    if expected not in core:
        raise SystemExit(f"Missing async query/provider resilience contract: {expected}")

if core.index("sortedWith(ranking)") > core.index("distinctBy"):
    raise SystemExit("Ranking must occur before provider-scoped deduplication")
if core.index("TimeoutCancellationException") > core.index("catch (cancellation: CancellationException)"):
    raise SystemExit("Timeout handling must occur before general cancellation propagation")

provider = (ROOT / "app/src/main/java/com/goreecloud/index/provider/apps/InstalledAppsProvider.kt").read_text(encoding="utf-8")
for expected in [
    'displayName: String = "Applications"',
    "IndexProcessingLocation.LOCAL",
    "timeoutMillis: Long = 500L",
    "override suspend fun search",
    "PackageManager.ResolveInfoFlags.of(0L)",
    "Intent.ACTION_MAIN",
    "Intent.CATEGORY_LAUNCHER",
    "flattenToString()",
]:
    if expected not in provider:
        raise SystemExit(f"Missing installed-app provider boundary: {expected}")

ui = (ROOT / "app/src/main/java/com/goreecloud/index/ui/IndexRoot.kt").read_text(encoding="utf-8")
for expected in [
    "suspend (String) -> IndexSearchSnapshot",
    "LaunchedEffect(query)",
    "Searching applications…",
    "IndexProviderIssueKind.TIMED_OUT",
    "took too long",
    "Applications · On-device",
    "WindowInsets.safeDrawing",
    "heightIn(min = 72.dp)",
]:
    if expected not in ui:
        raise SystemExit(f"Missing current async UI/degraded-state contract: {expected}")

main_activity = (ROOT / "app/src/main/java/com/goreecloud/index/MainActivity.kt").read_text(encoding="utf-8")
for expected in [
    "IndexExecutionContext",
    "allowedProviderIds = setOf(GoreeCloudIndexContract.PROVIDER_APPS)",
    "localOnly = true",
    "executionContext = executionContext",
    "Unable to open this application.",
]:
    if expected not in main_activity:
        raise SystemExit(f"MainActivity missing execution/action boundary: {expected}")

tests = (ROOT / "app/src/test/java/com/goreecloud/index/core/IndexQueryEngineTest.kt").read_text(encoding="utf-8")
for expected in [
    "providerTimeoutIsReportedWithoutSuppressingHealthyResults",
    "providersExecuteConcurrently",
    "cancellationPropagatesInsteadOfBecomingProviderIssue",
    "localOnlyExecutionContextFailsClosedForRemoteProviders",
    "disallowedProviderIsNotDispatched",
    "resultsAreRankedBeforeProviderScopedDuplicatesAreCollapsed",
    "resultLimitRemainsBounded",
    "StandardTestDispatcher",
    "runTest",
]:
    if expected not in tests:
        raise SystemExit(f"Missing async provider runtime test: {expected}")

readme = (ROOT / "README.md").read_text(encoding="utf-8")
for expected in [
    "Release lifecycle: Development",
    "Active development",
    "not Stable or production accepted",
    "0.2.0-dev",
    "asynchronous provider-runtime",
    "IndexExecutionContext",
    "not a claim of accepted Privacy Shield",
    "Glaze UI 2.1.0",
    "Launcher→Index",
    "Wardveil Security",
    "Everkeep",
    "19737c11c59a30a94ee8b6dad8855b449c011eca",
]:
    if expected not in readme:
        raise SystemExit(f"README missing required current-state boundary: {expected}")

for stale_claim in [
    "Pre-implementation / specification phase",
    "No end-user search capability is currently implemented",
    "No GoreeCloud Index runtime is currently implemented",
    "provider contract is synchronous",
    "Async provider execution, cancellation, and timeouts",
    "Multi-provider asynchronous execution, cancellation, or timeouts",
]:
    for document in [
        "README.md",
        "SPECIFICATIONS.md",
        "FEATURES.md",
        "CAPABILITIES.md",
        "ARCHITECTURE.md",
        "CONFORMANCE.md",
        "USER-MANUAL.md",
        "BENEFITS.md",
    ]:
        if stale_claim.lower() in (ROOT / document).read_text(encoding="utf-8").lower():
            raise SystemExit(f"Stale implementation claim in {document}: {stale_claim}")

architecture = (ROOT / "ARCHITECTURE.md").read_text(encoding="utf-8")
for expected in [
    "GoreeCloud Search remains authoritative",
    "Index remains the universal-search/indexing authority",
    "structured concurrent provider execution",
    "superseded-query cancellation",
    "bounded provider timeouts",
    "Privacy Shield runtime integration",
    "Wardveil Security runtime evidence",
]:
    if expected not in architecture:
        raise SystemExit(f"ARCHITECTURE.md missing authority/runtime boundary: {expected}")

conformance = (ROOT / "CONFORMANCE.md").read_text(encoding="utf-8")
for expected in [
    "Release lifecycle: Development",
    "Production acceptance and Stable qualification remain false",
    "No formal Glaze UI consumer-conformance claim is made yet",
    "19737c11c59a30a94ee8b6dad8855b449c011eca",
    "0.2.0-dev",
    "CancellationException",
    "TIMED_OUT",
]:
    if expected not in conformance:
        raise SystemExit(f"CONFORMANCE.md missing acceptance/runtime boundary: {expected}")

specifications = (ROOT / "SPECIFICATIONS.md").read_text(encoding="utf-8")
for expected in [
    "Release lifecycle:** Development",
    "Asynchronous Query Runtime",
    "Superseded-Query Cancellation",
    "Execution Eligibility and Authorization Boundary",
    "not constitute accepted Privacy Shield",
    "0.2.0-dev",
]:
    if expected not in specifications:
        raise SystemExit(f"SPECIFICATIONS.md missing current async milestone: {expected}")

print("GoreeCloud Index asynchronous provider runtime repository validation passed")
