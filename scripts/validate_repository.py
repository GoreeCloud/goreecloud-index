from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

required = [
    "README.md", "SPECIFICATIONS.md", "FEATURES.md", "BENEFITS.md",
    "COMPETITIVE-OBJECTIVES.md", "CAPABILITIES.md", "ARCHITECTURE.md",
    "CONFORMANCE.md", "USER-MANUAL.md", "app/build.gradle.kts",
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
    raise SystemExit("Current local provider slice must not request INTERNET")
if "android.permission.QUERY_ALL_PACKAGES" in manifest:
    raise SystemExit("Applications provider must not request QUERY_ALL_PACKAGES")
for expected in ["android.intent.action.MAIN", "android.intent.category.LAUNCHER", "com.goreecloud.index.action.SEARCH"]:
    if expected not in manifest:
        raise SystemExit(f"Missing Android contract: {expected}")

build = (ROOT / "app/build.gradle.kts").read_text(encoding="utf-8")
for expected in [
    'applicationId = "com.goreecloud.index"', 'applicationIdSuffix = ".dev"',
    'compileSdk = 37', 'targetSdk = 36', 'minSdk = 26', 'versionCode = 2',
    'versionName = "0.2.0-dev"', 'kotlinx-coroutines-android:1.11.0',
    'kotlinx-coroutines-test:1.11.0',
]:
    if expected not in build:
        raise SystemExit(f"Missing Android/runtime build contract: {expected}")

core = (ROOT / "app/src/main/java/com/goreecloud/index/core/IndexContract.kt").read_text(encoding="utf-8")
for expected in [
    "IndexProcessingLocation", "IndexProviderIssueKind", "IndexExecutionContext",
    "allowedProviderIds", "localOnly", "processingLocation", "timeoutMillis",
    "suspend fun search", "supervisorScope", "async(providerDispatcher)", "withTimeout",
    "catch (_: TimeoutCancellationException)", "catch (cancellation: CancellationException)",
    "throw cancellation", "IndexProviderIssueKind.TIMED_OUT", "IndexProviderIssueKind.FAILED",
    "sortedWith(ranking)", ".distinctBy { result ->",
]:
    if expected not in core:
        raise SystemExit(f"Missing async query/provider contract: {expected}")
if core.index("sortedWith(ranking)") > core.index(".distinctBy { result ->"):
    raise SystemExit("Ranking must occur before provider-scoped deduplication")
if core.index("catch (_: TimeoutCancellationException)") > core.index("catch (cancellation: CancellationException)"):
    raise SystemExit("Timeout handling must occur before general cancellation propagation")

provider = (ROOT / "app/src/main/java/com/goreecloud/index/provider/apps/InstalledAppsProvider.kt").read_text(encoding="utf-8")
for expected in [
    'displayName: String = "Applications"', "IndexProcessingLocation.LOCAL",
    "timeoutMillis: Long = 500L", "override suspend fun search",
    "PackageManager.ResolveInfoFlags.of(0L)", "Intent.ACTION_MAIN",
    "Intent.CATEGORY_LAUNCHER", "flattenToString()",
]:
    if expected not in provider:
        raise SystemExit(f"Missing Applications provider boundary: {expected}")

main_activity = (ROOT / "app/src/main/java/com/goreecloud/index/MainActivity.kt").read_text(encoding="utf-8")
for expected in [
    "IndexExecutionContext", "allowedProviderIds = setOf(GoreeCloudIndexContract.PROVIDER_APPS)",
    "localOnly = true", "executionContext = executionContext", "Unable to open this application.",
]:
    if expected not in main_activity:
        raise SystemExit(f"MainActivity missing execution/action boundary: {expected}")

ui = (ROOT / "app/src/main/java/com/goreecloud/index/ui/IndexRoot.kt").read_text(encoding="utf-8")
for expected in [
    "suspend (String) -> IndexSearchSnapshot", "LaunchedEffect(query)",
    "Searching applications…", "IndexProviderIssueKind.TIMED_OUT", "took too long",
    "Applications · On-device", "WindowInsets.safeDrawing", "heightIn(min = 72.dp)",
]:
    if expected not in ui:
        raise SystemExit(f"Missing async UI/degraded-state contract: {expected}")

tests = (ROOT / "app/src/test/java/com/goreecloud/index/core/IndexQueryEngineTest.kt").read_text(encoding="utf-8")
for expected in [
    "providerTimeoutIsReportedWithoutSuppressingHealthyResults", "providersExecuteConcurrently",
    "cancellationPropagatesInsteadOfBecomingProviderIssue",
    "localOnlyExecutionContextFailsClosedForRemoteProviders", "disallowedProviderIsNotDispatched",
    "resultsAreRankedBeforeProviderScopedDuplicatesAreCollapsed", "resultLimitRemainsBounded",
    "StandardTestDispatcher", "runTest",
]:
    if expected not in tests:
        raise SystemExit(f"Missing async runtime test: {expected}")

accepted_main = "e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9"
accepted_run = "33429792389"
accepted_apk = "a2605bb1e993dd027afc68b0900b60f2fcf9567ec59399e5a02a178af1cc815f"
accepted_artifact = "9772201920"

documents = [
    "README.md", "SPECIFICATIONS.md", "FEATURES.md", "CAPABILITIES.md",
    "ARCHITECTURE.md", "CONFORMANCE.md", "USER-MANUAL.md", "BENEFITS.md",
    "COMPETITIVE-OBJECTIVES.md",
]
for document in documents:
    text = (ROOT / document).read_text(encoding="utf-8")
    normalized = text.lower().replace("*", "")
    if "release lifecycle" not in normalized or "development" not in normalized:
        raise SystemExit(f"{document} missing Development lifecycle state")
    if accepted_main not in text:
        raise SystemExit(f"{document} missing accepted async main revision")

for document in ["README.md", "SPECIFICATIONS.md", "CAPABILITIES.md", "ARCHITECTURE.md", "CONFORMANCE.md", "USER-MANUAL.md", "BENEFITS.md"]:
    text = (ROOT / document).read_text(encoding="utf-8")
    for expected in [accepted_run, accepted_apk, accepted_artifact]:
        if expected not in text:
            raise SystemExit(f"{document} missing exact-main evidence: {expected}")

stale_phrases = [
    "asynchronous provider-runtime candidate",
    "asynchronous-provider-runtime work described below is current branch source",
    "accepted main baseline before this branch",
    "requires exact-candidate ci and merge acceptance before it becomes accepted-main evidence",
    "the 0.2.0-dev branch requires its own exact-candidate validation",
    "before this asynchronous runtime may be merged",
]
for document in documents:
    text = (ROOT / document).read_text(encoding="utf-8").lower()
    for stale in stale_phrases:
        if stale in text:
            raise SystemExit(f"Stale pre-merge claim in {document}: {stale}")

readme = (ROOT / "README.md").read_text(encoding="utf-8")
for prohibited in [
    "Privacy Shield integration is accepted", "Wardveil Security integration is accepted",
    "GoreeCloud Identity integration is accepted", "production accepted and Stable",
]:
    if prohibited.lower() in readme.lower():
        raise SystemExit(f"Unsupported positive integration/release claim: {prohibited}")

architecture = (ROOT / "ARCHITECTURE.md").read_text(encoding="utf-8")
for expected in [
    "GoreeCloud Search remains authoritative", "Index remains the universal-search/indexing authority",
    "structured concurrent provider execution", "superseded-query cancellation",
    "bounded provider timeouts", "Privacy Shield runtime integration", "Wardveil runtime evidence",
]:
    if expected not in architecture:
        raise SystemExit(f"ARCHITECTURE.md missing authority/runtime boundary: {expected}")

print("GoreeCloud Index accepted asynchronous-main repository validation passed")
