#!/usr/bin/env python3
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]

required_root = [
    "README.md",
    "SPECIFICATIONS.md",
    "FEATURES.md",
    "BENEFITS.md",
    "COMPETITIVE-OBJECTIVES.md",
    "CAPABILITIES.md",
    "ARCHITECTURE.md",
    "CONFORMANCE.md",
    "USER-MANUAL.md",
]

required_source = [
    "app/src/main/AndroidManifest.xml",
    "app/src/main/java/com/goreecloud/index/MainActivity.kt",
    "app/src/main/java/com/goreecloud/index/model/SearchModels.kt",
    "app/src/main/java/com/goreecloud/index/provider/SearchProvider.kt",
    "app/src/main/java/com/goreecloud/index/provider/AndroidAppSearchProvider.kt",
    "app/src/main/java/com/goreecloud/index/engine/SearchEngine.kt",
    "app/src/main/java/com/goreecloud/index/ui/GoreeCloudIndexApp.kt",
    "app/src/test/java/com/goreecloud/index/engine/SearchEngineTest.kt",
]

errors = []
for relative in required_root + required_source:
    if not (ROOT / relative).is_file():
        errors.append(f"missing required file: {relative}")

readme = (ROOT / "README.md").read_text(encoding="utf-8")
for required_text in [
    "Glaze UI 2.1.0",
    "Wardveil Security",
    "Privacy Shield",
    "Everkeep",
    "not Stable or production accepted",
    "Android launcher applications",
]:
    if required_text not in readme:
        errors.append(f"README missing required current-state text: {required_text!r}")

for prohibited in [
    "production ready",
    "fully integrated",
    "all providers implemented",
]:
    if prohibited.lower() in readme.lower():
        errors.append(f"README contains prohibited/unverified broad claim: {prohibited!r}")

manifest = (ROOT / "app/src/main/AndroidManifest.xml").read_text(encoding="utf-8")
if "android.permission.QUERY_ALL_PACKAGES" in manifest:
    errors.append("manifest must not request unrestricted QUERY_ALL_PACKAGES visibility")
for required_text in [
    "android.intent.action.MAIN",
    "android.intent.category.LAUNCHER",
]:
    if required_text not in manifest:
        errors.append(f"manifest missing narrow launcher visibility declaration: {required_text!r}")

architecture = (ROOT / "ARCHITECTURE.md").read_text(encoding="utf-8")
for required_text in [
    "Source applications and services remain authoritative",
    "Provider failures are isolated",
    "Privacy Shield integration remains pending",
    "Wardveil Security runtime integration remains pending",
]:
    if required_text not in architecture:
        errors.append(f"ARCHITECTURE.md missing authority invariant: {required_text!r}")

models = (ROOT / "app/src/main/java/com/goreecloud/index/model/SearchModels.kt").read_text(encoding="utf-8")
for token in [
    "SearchQuery",
    "SearchProviderDescriptor",
    "SearchResult",
    "SearchAction",
    "ProviderIssue",
    "SearchSnapshot",
]:
    if token not in models:
        errors.append(f"search model missing {token}")

provider = (ROOT / "app/src/main/java/com/goreecloud/index/provider/AndroidAppSearchProvider.kt").read_text(encoding="utf-8")
for required_text in [
    "Intent.ACTION_MAIN",
    "Intent.CATEGORY_LAUNCHER",
    "ProcessingMode.LOCAL",
    "MAX_RESULTS = 50",
]:
    if required_text not in provider:
        errors.append(f"Android application provider missing required boundary: {required_text!r}")

conformance = (ROOT / "CONFORMANCE.md").read_text(encoding="utf-8")
for required_text in [
    "Production acceptance and Stable qualification remain false",
    "No current-Stable Glaze UI conformance claim is made yet",
    "Approved Privacy Shield runtime contract integration",
    "Approved Wardveil runtime provider-trust/security evidence integration",
]:
    if required_text not in conformance:
        errors.append(f"CONFORMANCE.md missing acceptance boundary: {required_text!r}")

manual = (ROOT / "USER-MANUAL.md").read_text(encoding="utf-8")
for required_text in [
    "Searching Applications",
    "Applications · On-device",
    "not Stable or production accepted",
    "com.goreecloud.index.dev",
]:
    if required_text not in manual:
        errors.append(f"USER-MANUAL.md missing current behavior: {required_text!r}")

if errors:
    for error in errors:
        print(f"ERROR: {error}", file=sys.stderr)
    raise SystemExit(1)

print("GoreeCloud Index repository validation passed.")
