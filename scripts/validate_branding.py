#!/usr/bin/env python3
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BRANDING = ROOT / "BRANDING.md"
MANIFEST = ROOT / "app/src/main/AndroidManifest.xml"

if not BRANDING.is_file():
    raise SystemExit("Missing mandatory BRANDING.md")

branding = BRANDING.read_text(encoding="utf-8")
for required in [
    "GoreeCloud/goreecloud-branding-assets",
    "products/index/app-icon.svg",
    "concepts/product-identity-round-1/index.svg",
    "does **not yet have approved canonical product artwork**",
    "GoreeCloud Search",
    "GoreeCloud Launcher",
    "android:icon",
]:
    if required not in branding:
        raise SystemExit(f"BRANDING.md missing required current identity boundary: {required}")

manifest = MANIFEST.read_text(encoding="utf-8")
if "android:icon=" in manifest:
    raise SystemExit(
        "Index declares an Android icon before BRANDING.md records an approved canonical identity and derivative"
    )

print("GoreeCloud Index branding candidate-state validation passed.")
