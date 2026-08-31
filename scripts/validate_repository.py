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
    'versionName = "0.1.0-dev"',
]:
    if expected not in build:
        raise SystemExit(f"Missing Android build contract: {expected}")

core = (ROOT / "app/src/main/java/com/goreecloud/index/core/IndexContract.kt").read_text(encoding="utf-8")
for expected in [
    "IndexProviderIssue",
    "IndexSearchSnapshot",
    "providerIssues",
    "displayName",
    "distinctBy",
]:
    if expected not in core:
        raise SystemExit(f"Missing query/provider resilience contract: {expected}")

provider = (ROOT / "app/src/main/java/com/goreecloud/index/provider/apps/InstalledAppsProvider.kt").read_text(encoding="utf-8")
for expected in [
    'displayName: String = "Applications"',
    "PackageManager.ResolveInfoFlags.of(0L)",
    "Intent.ACTION_MAIN",
    "Intent.CATEGORY_LAUNCHER",
    "flattenToString()",
]:
    if expected not in provider:
        raise SystemExit(f"Missing installed-app provider boundary: {expected}")

ui = (ROOT / "app/src/main/java/com/goreecloud/index/ui/IndexRoot.kt").read_text(encoding="utf-8")
for expected in [
    "Applications · On-device",
    "temporarily unavailable",
    "WindowInsets.safeDrawing",
    "heightIn(min = 72.dp)",
]:
    if expected not in ui:
        raise SystemExit(f"Missing current UI/degraded-state contract: {expected}")

main_activity = (ROOT / "app/src/main/java/com/goreecloud/index/MainActivity.kt").read_text(encoding="utf-8")
if "Unable to open this application." not in main_activity:
    raise SystemExit("Application launch failure must remain user-visible")

readme = (ROOT / "README.md").read_text(encoding="utf-8")
for expected in [
    "Active development",
    "not Stable or production accepted",
    "Glaze UI 2.1.0",
    "Launcher→Index",
    "Wardveil Security",
    "Privacy Shield",
    "Everkeep",
]:
    if expected not in readme:
        raise SystemExit(f"README missing required current-state boundary: {expected}")

for stale_claim in [
    "Pre-implementation / specification phase",
    "No end-user search capability is currently implemented",
    "No GoreeCloud Index runtime is currently implemented",
]:
    for document in ["README.md", "SPECIFICATIONS.md", "FEATURES.md", "CAPABILITIES.md", "BENEFITS.md"]:
        if stale_claim.lower() in (ROOT / document).read_text(encoding="utf-8").lower():
            raise SystemExit(f"Stale implementation claim in {document}: {stale_claim}")

architecture = (ROOT / "ARCHITECTURE.md").read_text(encoding="utf-8")
for expected in [
    "GoreeCloud Search remains authoritative",
    "Index remains the universal-search/indexing authority",
    "Privacy Shield runtime integration",
    "Wardveil Security runtime evidence",
]:
    if expected not in architecture:
        raise SystemExit(f"ARCHITECTURE.md missing authority boundary: {expected}")

conformance = (ROOT / "CONFORMANCE.md").read_text(encoding="utf-8")
for expected in [
    "Production acceptance and Stable qualification remain false",
    "No formal Glaze UI consumer-conformance claim is made yet",
    "331e97507a7b3b7ca3d930771915f1026bf2d4a8",
]:
    if expected not in conformance:
        raise SystemExit(f"CONFORMANCE.md missing acceptance boundary/evidence: {expected}")

print("GoreeCloud Index repository contract validation passed")
