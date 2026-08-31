from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

required = [
    "README.md",
    "SPECIFICATIONS.md",
    "FEATURES.md",
    "BENEFITS.md",
    "COMPETITIVE-OBJECTIVES.md",
    "CAPABILITIES.md",
    "app/build.gradle.kts",
    "app/src/main/AndroidManifest.xml",
]

missing = [path for path in required if not (ROOT / path).is_file()]
if missing:
    raise SystemExit(f"Missing required repository files: {', '.join(missing)}")

manifest = (ROOT / "app/src/main/AndroidManifest.xml").read_text(encoding="utf-8")
if "android.permission.INTERNET" in manifest:
    raise SystemExit("Initial GoreeCloud Index local-search slice must not request INTERNET permission")
if "android.permission.QUERY_ALL_PACKAGES" in manifest:
    raise SystemExit("GoreeCloud Index must not request QUERY_ALL_PACKAGES for installed-app discovery")
if "android.intent.category.LAUNCHER" not in manifest:
    raise SystemExit("Scoped launcher-activity discovery contract is missing")
if "com.goreecloud.index.action.SEARCH" not in manifest:
    raise SystemExit("Launcher-to-Index search handoff action is missing")

build = (ROOT / "app/build.gradle.kts").read_text(encoding="utf-8")
for expected in [
    'applicationId = "com.goreecloud.index"',
    'applicationIdSuffix = ".dev"',
    'compileSdk = 37',
    'targetSdk = 36',
]:
    if expected not in build:
        raise SystemExit(f"Missing Android build contract: {expected}")

print("GoreeCloud Index repository contract validation passed")
