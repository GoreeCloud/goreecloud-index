#!/usr/bin/env python3
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BRANDING = ROOT / "BRANDING.md"
MANIFEST = ROOT / "app/src/main/AndroidManifest.xml"
ICON = ROOT / "app/src/main/res/drawable/goreecloud_index_icon.xml"

if not BRANDING.is_file():
    raise SystemExit("Missing mandatory BRANDING.md")
if not ICON.is_file():
    raise SystemExit("Missing approved Index Android icon derivative")

branding = BRANDING.read_text(encoding="utf-8")
for required in [
    "GoreeCloud/goreecloud-branding-assets",
    "products/index/app-icon.svg",
    "797cfbd9ae490e37b5a90efe02905159158a8e88",
    "app/src/main/res/drawable/goreecloud_index_icon.xml",
    'android:icon="@drawable/goreecloud_index_icon"',
    "approved canonical product artwork",
    "GoreeCloud Search",
    "GoreeCloud Launcher",
]:
    if required not in branding:
        raise SystemExit(f"BRANDING.md missing approved identity provenance: {required}")

manifest = MANIFEST.read_text(encoding="utf-8")
if 'android:icon="@drawable/goreecloud_index_icon"' not in manifest:
    raise SystemExit("Index manifest does not consume the approved canonical icon derivative")

icon = ICON.read_text(encoding="utf-8")
for required in [
    '#A855F7',
    '#4338CA',
    'android:viewportWidth="64"',
    'android:viewportHeight="64"',
    'M17,16H42L48,21V29H17',
    'M21,28H47',
    'M17,40H42L48,45V48H17',
    'M42,16V23H48M42,40V47H48',
]:
    if required not in icon:
        raise SystemExit(f"Index Android icon derivative drifted from approved identity: {required}")

print("GoreeCloud Index approved branding validation passed.")
