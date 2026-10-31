#!/usr/bin/env python3
"""FORGEBOUND content validator (Sections 3, 12, 14).

Fails the build (exit non-zero) on:
  * malformed JSON packs
  * missing/broken cross-references (zone -> materials/enemies, item recipes,
    quest zones, dialogue backgrounds)
  * content below the Section 14 minimums
  * missing EN/FA translations on named entries

Run:  python3 tools/validate_content.py
"""

import json
import os
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CONTENT = os.path.join(ROOT, "assets", "content")

PACKS = [
    "items.json", "materials.json", "skills.json", "enemies.json", "zones.json",
    "quests.json", "shops.json", "achievements.json", "pets.json", "allies.json",
    "dialogue.json",
]

# Section 14 content minimums
MINIMUMS = {
    "items.json": 240,        # equipment + consumables
    "materials.json": 60,
    "skills.json": 110,
    "enemies.json": 65,       # including bosses
    "zones.json": 11,         # 10 story zones + secret
    "quests.json": 130,
    "shops.json": 8,
    "achievements.json": 120,
    "pets.json": 6,
    "allies.json": 5,
    "dialogue.json": 80,      # dialogue scenes
}

REQUIRED_TOP = {
    "items.json":     ["id", "name", "type", "tier", "rarity", "blueprint"],
    "materials.json": ["id", "name", "type", "tier"],
    "skills.json":    ["id", "name", "class", "type", "levelReq"],
    "enemies.json":   ["id", "name", "type", "zone", "level", "baseStats"],
    "zones.json":     ["id", "name", "levelRange", "stages", "boss"],
    "quests.json":    ["id", "name", "type", "chapter", "objectives", "rewards"],
    "shops.json":     ["id", "name", "sells"],
    "achievements.json": ["id", "name", "category", "condition", "reward"],
    "pets.json":      ["id", "name", "rarity"],
    "allies.json":    ["id", "name", "class"],
    "dialogue.json":  ["id", "chapter", "title", "lines"],
}

errors = []
warnings = []


def fail(msg):
    errors.append(msg)


def load_pack(name):
    path = os.path.join(CONTENT, name)
    if not os.path.exists(path):
        fail(f"missing content pack: {path}")
        return None
    try:
        with open(path, encoding="utf-8") as f:
            data = json.load(f)
    except json.JSONDecodeError as exc:
        fail(f"{name}: invalid JSON: {exc}")
        return None
    if not isinstance(data, list):
        fail(f"{name}: top-level must be a JSON array")
        return None
    return data


def check_names(entry, eid, pack):
    named = entry.get("name") or entry.get("title")
    if not isinstance(named, dict):
        fail(f"{pack} [{eid}]: 'name' (or 'title' for dialogue) must be an object")
        return
    for lang in ("en", "fa"):
        if lang not in named or not named[lang]:
            fail(f"{pack} [{eid}]: missing name.{lang}")


def main():
    os.chdir(ROOT)
    packs = {name: load_pack(name) for name in PACKS}
    if any(v is None for v in packs.values()):
        for m in errors:
            print("ERROR:", m)
        sys.exit(1)

    items, materials, skills, enemies, zones = (
        packs["items.json"], packs["materials.json"], packs["skills.json"],
        packs["enemies.json"], packs["zones.json"],
    )
    quests, shops, achievements, pets, allies, dialogue = (
        packs["quests.json"], packs["shops.json"], packs["achievements.json"],
        packs["pets.json"], packs["allies.json"], packs["dialogue.json"],
    )

    zone_ids = {z["id"] for z in zones}
    material_ids = {m["id"] for m in materials}
    item_ids = {i["id"] for i in items}
    skill_ids = {s["id"] for s in skills}
    enemy_ids = {e["id"] for e in enemies}

    # --- per-entry schema + names ---
    for pack_name, entries in packs.items():
        for entry in entries:
            eid = entry.get("id", "<no-id>")
            if not isinstance(eid, str) or not eid:
                fail(f"{pack_name}: entry missing string id: {entry!r}")
            for field in REQUIRED_TOP.get(pack_name, []):
                if field not in entry:
                    fail(f"{pack_name} [{eid}]: missing required field '{field}'")
            check_names(entry, eid, pack_name)

    # --- cross-references ---
    for it in items:
        eid = it["id"]
        for m in it.get("blueprint", {}).get("materials", []):
            if m["id"] not in material_ids:
                fail(f"items.json [{eid}]: recipe references unknown material {m['id']}")
        zd = it.get("zoneDrop")
        if zd and zd not in zone_ids:
            fail(f"items.json [{eid}]: zoneDrop references unknown zone {zd}")

    for en in enemies:
        eid = en["id"]
        if en["zone"] not in zone_ids:
            fail(f"enemies.json [{eid}]: unknown zone {en['zone']}")
        for d in en.get("drops", []):
            if d["id"] not in material_ids and d["id"] not in item_ids:
                fail(f"enemies.json [{eid}]: drop references unknown id {d['id']}")

    for q in quests:
        qid = q["id"]
        if q["zone"] not in zone_ids:
            fail(f"quests.json [{qid}]: unknown zone {q['zone']}")

    for dlg in dialogue:
        did = dlg["id"]
        if dlg.get("bg") and dlg["bg"] not in zone_ids:
            fail(f"dialogue.json [{did}]: unknown background zone {dlg['bg']}")

    # --- Section 14 minimums ---
    counts = {
        "items.json": len(items), "materials.json": len(materials),
        "skills.json": len(skills), "enemies.json": len(enemies),
        "zones.json": len(zones), "quests.json": len(quests),
        "shops.json": len(shops), "achievements.json": len(achievements),
        "pets.json": len(pets), "allies.json": len(allies),
        "dialogue.json": len(dialogue),
    }
    for name, minimum in MINIMUMS.items():
        got = counts[name]
        status = "OK" if got >= minimum else "BELOW MINIMUM"
        print(f"  {name}: {got} (minimum {minimum}) -> {status}")
        if got < minimum:
            fail(f"Section 14: {name} has {got} entries, minimum {minimum}")

    # enemy breakdown (normal/elite/boss)
    kinds = {}
    for e in enemies:
        kinds[e["type"]] = kinds.get(e["type"], 0) + 1
    print("  enemies by type:", dict(sorted(kinds.items())))
    bosses = sum(v for k, v in kinds.items() if "BOSS" in k)
    print(f"  bosses total: {bosses}")

    if errors:
        print(f"\nCONTENT VALIDATION FAILED ({len(errors)} error(s)):")
        for m in errors:
            print("  -", m)
        sys.exit(1)

    print(f"\nCONTENT VALIDATION PASSED — {sum(counts.values())} total entries across {len(PACKS)} packs.")


if __name__ == "__main__":
    main()
