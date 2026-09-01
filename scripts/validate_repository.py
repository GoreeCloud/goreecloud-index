from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

required = [
    "README.md", "SPECIFICATIONS.md", "FEATURES.md", "BENEFITS.md",
    "COMPETITIVE-OBJECTIVES.md", "CAPABILITIES.md", "ARCHITECTURE.md",
    "CONFORMANCE.md", "USER-MANUAL.md", "app/build.gradle.kts",
    "app/src/main/AndroidManifest.xml",
    "app/src/main/java/com/goreecloud/index/MainActivity.kt",
    "app/src/main/java/com/goreecloud/index/core/IndexAuthority.kt",
    "app/src/main/java/com/goreecloud/index/core/IndexContract.kt",
    "app/src/main/java/com/goreecloud/index/core/PlatformAuthorityAdapters.kt",
    "app/src/main/java/com/goreecloud/index/provider/apps/InstalledAppsProvider.kt",
    "app/src/main/java/com/goreecloud/index/provider/contacts/ContactsProvider.kt",
    "app/src/main/java/com/goreecloud/index/ui/IndexRoot.kt",
    "app/src/test/java/com/goreecloud/index/core/IndexQueryEngineTest.kt",
    "app/src/test/java/com/goreecloud/index/core/PlatformAuthorityAdaptersTest.kt",
]
missing = [path for path in required if not (ROOT / path).is_file()]
if missing:
    raise SystemExit(f"Missing required repository files: {', '.join(missing)}")

manifest = (ROOT / "app/src/main/AndroidManifest.xml").read_text(encoding="utf-8")
if "android.permission.INTERNET" in manifest:
    raise SystemExit("Current local provider slice must not request INTERNET")
if "android.permission.QUERY_ALL_PACKAGES" in manifest:
    raise SystemExit("Applications provider must not request QUERY_ALL_PACKAGES")
for expected in [
    "android.permission.READ_CONTACTS",
    "android.intent.action.MAIN",
    "android.intent.category.LAUNCHER",
    "com.goreecloud.index.action.SEARCH",
]:
    if expected not in manifest:
        raise SystemExit(f"Missing Android contract: {expected}")

build = (ROOT / "app/build.gradle.kts").read_text(encoding="utf-8")
for expected in [
    'applicationId = "com.goreecloud.index"', 'applicationIdSuffix = ".dev"',
    'compileSdk = 37', 'targetSdk = 36', 'minSdk = 26', 'versionCode = 3',
    'versionName = "0.3.0-dev"', 'kotlinx-coroutines-android:1.11.0',
    'kotlinx-coroutines-test:1.11.0',
]:
    if expected not in build:
        raise SystemExit(f"Missing Android/runtime build contract: {expected}")

authority = (ROOT / "app/src/main/java/com/goreecloud/index/core/IndexAuthority.kt").read_text(encoding="utf-8")
for expected in [
    "ANDROID_RUNTIME_PERMISSION", "PRIVACY_SHIELD", "GOREECLOUD_IDENTITY",
    "ALLOW_WITH_CONSTRAINTS", "REQUIRE_USER_DECISION", "UNAVAILABLE",
    "outcome == IndexAuthorityOutcome.ALLOW", "!reference.isNullOrBlank()",
    "satisfiesAll",
]:
    if expected not in authority:
        raise SystemExit(f"Missing fail-closed authority contract: {expected}")

platform_authority = (
    ROOT / "app/src/main/java/com/goreecloud/index/core/PlatformAuthorityAdapters.kt"
).read_text(encoding="utf-8")
for expected in [
    "PrivacyShieldDecisionOutcome", "ALLOW_WITH_CONSTRAINTS", "REQUIRE_USER_DECISION",
    "decision.requestId != expectedRequestId", "decision.expiresAt?.isAfter(now) == false",
    'IDENTITY_EVIDENCE_CONTRACT = "goreecloud.identity-evidence.v1"',
    'AUTHORIZATION_ASSERTION = "authorization-decision"',
    "containsReusableCredentials", "containsRawProfileAttributes",
    "IdentityAuthorizationOutcomeInterpreter", "?: return IndexAuthorityEvidence.unavailable()",
    "IndexPlatformAuthoritySnapshot", "IndexPlatformAuthorityGateway",
    "UnavailableIndexPlatformAuthorityGateway", "ContactsAuthorityProjection",
]:
    if expected not in platform_authority:
        raise SystemExit(f"Missing platform authority adapter boundary: {expected}")

core = (ROOT / "app/src/main/java/com/goreecloud/index/core/IndexContract.kt").read_text(encoding="utf-8")
for expected in [
    "PROVIDER_CONTACTS", "IndexProcessingLocation", "IndexProviderIssueKind",
    "IndexProviderIssueKind.AUTHORIZATION_REQUIRED", "IndexExecutionContext",
    "providerAuthorities", "authorityRequirements", "supportsEmptyQuery",
    "authorizationIssue", "allowedProviderIds", "localOnly", "processingLocation",
    "timeoutMillis", "suspend fun search", "supervisorScope", "async(providerDispatcher)",
    "withTimeout", "catch (_: TimeoutCancellationException)",
    "catch (cancellation: CancellationException)", "throw cancellation",
    "IndexProviderIssueKind.TIMED_OUT", "IndexProviderIssueKind.FAILED",
    "sortedWith(ranking)", ".distinctBy { result ->",
]:
    if expected not in core:
        raise SystemExit(f"Missing query/provider authority contract: {expected}")
if core.index("sortedWith(ranking)") > core.index(".distinctBy { result ->"):
    raise SystemExit("Ranking must occur before provider-scoped deduplication")
if core.index("catch (_: TimeoutCancellationException)") > core.index("catch (cancellation: CancellationException)"):
    raise SystemExit("Timeout handling must occur before general cancellation propagation")

apps = (ROOT / "app/src/main/java/com/goreecloud/index/provider/apps/InstalledAppsProvider.kt").read_text(encoding="utf-8")
for expected in [
    'displayName: String = "Applications"', "IndexProcessingLocation.LOCAL",
    "timeoutMillis: Long = 500L", "override suspend fun search",
    "PackageManager.ResolveInfoFlags.of(0L)", "Intent.ACTION_MAIN",
    "Intent.CATEGORY_LAUNCHER", "flattenToString()",
]:
    if expected not in apps:
        raise SystemExit(f"Missing Applications provider boundary: {expected}")

contacts = (ROOT / "app/src/main/java/com/goreecloud/index/provider/contacts/ContactsProvider.kt").read_text(encoding="utf-8")
for expected in [
    'displayName: String = "Contacts"', "IndexProcessingLocation.LOCAL",
    "timeoutMillis: Long = 750L", "supportsEmptyQuery: Boolean = false",
    "ANDROID_RUNTIME_PERMISSION", "PRIVACY_SHIELD", "GOREECLOUD_IDENTITY",
    "ContactsContract.Contacts.CONTENT_FILTER_URI", "ContactsContract.Contacts._ID",
    "ContactsContract.Contacts.LOOKUP_KEY", "ContactsContract.Contacts.DISPLAY_NAME_PRIMARY",
    "ContactsContract.Contacts.getLookupUri", "IndexResultType.CONTACT",
    "IndexAction.ViewContact", "MAX_CONTACT_RESULTS = 100",
]:
    if expected not in contacts:
        raise SystemExit(f"Missing Contacts provider boundary: {expected}")
for prohibited in [
    "CommonDataKinds.Phone", "CommonDataKinds.Email", "Phone.NUMBER", "Email.ADDRESS",
]:
    if prohibited in contacts:
        raise SystemExit(f"Contacts provider exceeds minimized display-data slice: {prohibited}")

main_activity = (ROOT / "app/src/main/java/com/goreecloud/index/MainActivity.kt").read_text(encoding="utf-8")
for expected in [
    "ContactsProvider", "PROVIDER_APPS", "PROVIDER_CONTACTS", "providerAuthorities",
    "Manifest.permission.READ_CONTACTS", "IndexPlatformAuthorityGateway",
    "UnavailableIndexPlatformAuthorityGateway", "ContactsAuthorityProjection.project",
    "platformAuthorityGateway.contactsSnapshot()", "IndexAction.ViewContact",
    "ContactsContract.AUTHORITY", 'uri.scheme == "content"',
    'uri.pathSegments.firstOrNull() == "contacts"', "Unable to open this application.",
    "Unable to open this contact.",
]:
    if expected not in main_activity:
        raise SystemExit(f"MainActivity missing provider/authority/action boundary: {expected}")

ui = (ROOT / "app/src/main/java/com/goreecloud/index/ui/IndexRoot.kt").read_text(encoding="utf-8")
for expected in [
    "suspend (String) -> IndexSearchSnapshot", "LaunchedEffect(query)",
    "Searching authorized sources…", "IndexProviderIssueKind.TIMED_OUT",
    "IndexProviderIssueKind.AUTHORIZATION_REQUIRED", "Contacts · On-device · Authority gated",
    "Required permission or platform authority evidence is incomplete",
    "WindowInsets.safeDrawing", "heightIn(min = 72.dp)", "People · On-device",
]:
    if expected not in ui:
        raise SystemExit(f"Missing multi-source/degraded UI contract: {expected}")

tests = (ROOT / "app/src/test/java/com/goreecloud/index/core/IndexQueryEngineTest.kt").read_text(encoding="utf-8")
for expected in [
    "providerTimeoutIsReportedWithoutSuppressingHealthyResults", "providersExecuteConcurrently",
    "cancellationPropagatesInsteadOfBecomingProviderIssue",
    "localOnlyExecutionContextFailsClosedForRemoteProviders", "disallowedProviderIsNotDispatched",
    "authorityGatedProviderIsNotDispatchedWithoutEvidence",
    "authorityGatedProviderRunsWithUnconstrainedEvidence",
    "constrainedAuthorityFailsClosedUntilObligationsAreSupported",
    "nonBrowsingProviderDoesNotRequestAuthorityForBlankQuery",
    "resultsAreRankedBeforeProviderScopedDuplicatesAreCollapsed", "resultLimitRemainsBounded",
    "StandardTestDispatcher", "runTest",
]:
    if expected not in tests:
        raise SystemExit(f"Missing async/authority runtime test: {expected}")

platform_tests = (
    ROOT / "app/src/test/java/com/goreecloud/index/core/PlatformAuthorityAdaptersTest.kt"
).read_text(encoding="utf-8")
for expected in [
    "privacyShieldUnconstrainedAllowProjectsAllow", "privacyShieldObligationsRemainConstrained",
    "privacyShieldStaleEvidenceFailsClosed", "identityAuthorizationUsesIdentityOwnedInterpreter",
    "identityUnknownOutcomeFailsClosed", "identityMinimizationViolationFailsClosed",
    "unavailableGatewayKeepsContactsBlocked", "acceptedSnapshotCanSatisfyContactsAuthorities",
]:
    if expected not in platform_tests:
        raise SystemExit(f"Missing platform authority adapter test: {expected}")

accepted_main = "cc3cc21d6e11dad026253c3371c3b67663d3b726"
accepted_run = "33431294298"
accepted_apk = "54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f"
accepted_artifact = "9772740479"

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
        raise SystemExit(f"{document} missing accepted main revision")
    for expected in [accepted_run, accepted_apk, accepted_artifact]:
        if expected not in text:
            raise SystemExit(f"{document} missing exact-main evidence: {expected}")

for document in documents:
    text = (ROOT / document).read_text(encoding="utf-8").lower()
    for prohibited in [
        "contacts integration is accepted",
        "privacy shield integration is accepted",
        "goreecloud identity integration is accepted",
        "production accepted and stable",
        "contacts provider is enabled",
    ]:
        if prohibited in text:
            raise SystemExit(f"Unsupported provider/integration/release claim in {document}: {prohibited}")

architecture = (ROOT / "ARCHITECTURE.md").read_text(encoding="utf-8")
for expected in [
    "GoreeCloud Search remains authoritative", "Index remains the universal-search/indexing authority",
    "Privacy Shield remains authoritative", "GoreeCloud Identity remains authoritative",
    "ALLOW_WITH_CONSTRAINTS", "ContactsProvider", "AUTHORIZATION_REQUIRED",
]:
    if expected not in architecture:
        raise SystemExit(f"ARCHITECTURE.md missing authority/runtime boundary: {expected}")

print("GoreeCloud Index platform authority adapter repository validation passed")
