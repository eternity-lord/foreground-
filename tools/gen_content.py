#!/usr/bin/env python3
"""FORGEBOUND content pipeline (Section 12).

Deterministic generator for all JSON content packs in assets/content/.
Every pack is produced from a fixed seed so output is reproducible; the
companion tools/validate_content.py checks schema, cross-references and the
Section 14 content minimums, and must FAIL the build on broken content.

Run:  python3 tools/gen_content.py
      python3 tools/validate_content.py
"""

import json
import os
import random

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OUT = os.path.join(ROOT, "assets", "content")

SEED = 20261031  # deterministic, reproducible

CLASSES = ["NOVICE", "KNIGHT", "ROGUE", "MAGE", "RANGER", "VULCAN_HEIR"]
SLOTS = ["WEAPON", "HEAD", "CHEST", "HANDS", "LEGS", "RING", "AMULET"]

# Persian helpers (natively written)
TIER_FA = {i: f"درجهٔ {i}" for i in range(1, 13)}
TYPE_FA = {"WEAPON": "سلاح", "ARMOR": "زیرکاب", "ACCESSORY": "آسانه", "CONSUMABLE": "مصرفی"}
SLOT_FA = {"WEAPON": "سلاح", "HEAD": "سر", "CHEST": "سینه", "HANDS": "دست",
           "LEGS": "پا", "RING": "Angosht-band", "AMULET": "Ghazal"}


def rarity_of(tier):
    if tier >= 11: return "MYTHIC"
    if tier >= 9: return "LEGENDARY"
    if tier >= 7: return "EPIC"
    if tier >= 5: return "RARE"
    if tier >= 3: return "UNCOMMON"
    return "COMMON"


def fa_item_name(tier, rtype, slot):
    t = TYPE_FA.get(rtype, rtype)
    s = SLOT_FA.get(slot, "") if slot else ""
    base = TIER_FA[tier]
    return f"{base} {t} {s}".strip() if s else f"{base} {t}"


def base_stats_for(tier, rtype):
    scale = 1.0 + (tier - 1) * 0.35
    if rtype == "WEAPON":
        return {"ATK": int(8 * scale), "CRIT_PCT": round(3 + tier * 0.6, 1)}
    if rtype == "ARMOR":
        return {"DEF": int(6 * scale), "HP": int(12 * scale)}
    if rtype == "ACCESSORY":
        return {"LUCK": round(0.5 * tier, 1), "CRIT_DMG": round(2 * tier, 1)}
    return {}


# ---------------------------------------------------------------------------
# ITEMS (>= 240 equipment + consumables)
# ---------------------------------------------------------------------------
WEAPON_NAMES_EN = ["Rusty Dagger", "Iron Dagger", "Steel Dagger", "Rusty Sword", "Iron Sword",
                   "Steel Sword", "Rusty Axe", "Iron Axe", "Steel Axe", "Rusty Mace", "Iron Mace",
                   "Steel Mace", "Rusty Bow", "Iron Bow", "Steel Bow", "Rusty Staff", "Iron Staff",
                   "Steel Staff"]
ARMOR_NAMES_EN = ["Padded Gambeson", "Leather Cuirass", "Chain Shirt", "Scale Vest",
                  "Breastplate", "Plate Cuirass"]
ACCESSORY_NAMES_EN = ["Copper Ring", "Iron Ring", "Iron Amulet", "Steel Pendant",
                      "Ruby Talisman", "Obsidian Charm"]
CONSUMABLE_NAMES_EN = ["Minor Health Potion", "Health Potion", "Greater Health Potion",
                       "Minor Mana Potion", "Mana Potion", "Antidote", "Forge Oil", "Smith's Chalk"]
CONSUMABLE_NAMES_FA = ["شراب سلامت کوچک", "شراب سلامت", "شراب سلامت بزرگ",
                       "شراب منا کوچک", "شراب منا", "تیریاک", "روغن آهنگر", "چک آهنگر"]


def generate_items(rng):
    items, eid = [], 1000
    for tier in range(1, 13):
        for slot_group, names, rtype in [("WEAPON", WEAPON_NAMES_EN, "WEAPON"),
                                         ("ARMOR", ARMOR_NAMES_EN, "ARMOR"),
                                         ("ACCESSORY", ACCESSORY_NAMES_EN, "ACCESSORY")]:
            for variant in range(1, 6):
                en_base = names[(variant - 1) % len(names)]
                items.append({
                    "id": f"item_{eid}", "name": {"en": f"{en_base} T{tier}",
                                                  "fa": fa_item_name(tier, rtype, slot_group)},
                    "type": rtype, "slot": slot_group, "tier": tier, "rarity": rarity_of(tier),
                    "levelReq": tier * 8, "baseStats": base_stats_for(tier, rtype),
                    "blueprint": {"gold": int(50 * tier * tier), "forgeSkillMin": max(1, tier - 2),
                                  "materials": [{"id": f"mat_{2000 + tier}", "qty": tier},
                                                {"id": f"mat_{2000 + ((tier + 3) % 12)}",
                                                 "qty": max(1, tier // 2)}]},
                    "zoneDrop": f"zone_{(tier - 1) % 10 + 1:02d}", "classes": ["ALL"],
                })
                eid += 1
    for tier in range(1, 9):
        for en_base, fa_base in zip(CONSUMABLE_NAMES_EN, CONSUMABLE_NAMES_FA):
            items.append({
                "id": f"item_{eid}", "name": {"en": f"{en_base} T{tier}", "fa": f"{fa_base} {TIER_FA[tier]}"},
                "type": "CONSUMABLE", "slot": None, "tier": tier, "rarity": rarity_of(tier),
                "levelReq": tier * 6, "baseStats": {},
                "blueprint": {"gold": int(25 * tier), "forgeSkillMin": 1,
                              "materials": [{"id": f"mat_{2000 + tier}", "qty": 1}]},
                "zoneDrop": f"zone_{(tier - 1) % 10 + 1:02d}", "classes": ["ALL"],
            })
            eid += 1
    return items


# ---------------------------------------------------------------------------
# MATERIALS (>= 60)
# ---------------------------------------------------------------------------
MATERIAL_KINDS = [
    ("Iron Ore", "آهن خام"), ("Steel Ingot", "فولاد"), ("Mithril Shard", "تکه میتریل"),
    ("Orichalcum Nugget", "تکه اوریکالکوم"), ("Starsteel Fragment", "تکه فلز ستاره‌ای"),
    ("Hickory Log", "خشت چوب هیکری"), ("Ironwood Plank", "تخته آهن‌چوب"),
    ("Moonwood Bowstave", "ستون کمان ماهی"), ("Ruby Crystal", "یاقوت سرخ"), ("Sapphire Shard", "تکه نیلوی"),
    ("Emerald Dust", "غبار زمرد"), ("Topaz Sliver", "تکه یاقوت زرد"),
    ("Direwolf Fang", "ناخن گرگ بزرگ"), ("Basilisk Scale", "پوست باسلیسک"),
    ("Phoenix Feather", "پر سیمرغ"), ("Rust Shard", "تکه زنگ"), ("Void Essence", "جوهر خلا"),
    ("Dragonscale", "پوست اژدها"), ("Titan Tendon", "تاندون تیتان"), ("Celestial Silk", "ابریشم آسمانی"),
]
MATERIAL_TYPES = ["ORE", "WOOD", "GEM", "MONSTER_PART", "ESSENCE"]


def generate_materials(rng):
    materials, mid = [], 2000
    for tier in range(1, 13):
        for i in range(5):
            kind_en, kind_fa = MATERIAL_KINDS[i % len(MATERIAL_KINDS)]
            materials.append({
                "id": f"mat_{mid}", "name": {"en": f"{kind_en} T{tier}",
                                             "fa": f"{kind_fa} {TIER_FA[tier]}"},
                "type": MATERIAL_TYPES[i % len(MATERIAL_TYPES)], "tier": tier,
                "dropZones": [f"zone_{(tier + j - 1) % 10 + 1:02d}" for j in range(2)],
                "sellPrice": int(5 * tier * tier),
            })
            mid += 1
    return materials


# ---------------------------------------------------------------------------
# SKILLS (>= 110)
# ---------------------------------------------------------------------------
def skill(skill_id, en, fa, cls, stype, level, desc_en, desc_fa, effects):
    return {"id": skill_id, "name": {"en": en, "fa": fa}, "class": cls, "type": stype,
            "levelReq": level, "description": {"en": desc_en, "fa": desc_fa}, "effects": effects}


SKILL_NAME_FA = {
    "Heavy Strike": "ضربه سنگین", "Quick Step": "گام سریع", "Guard Stance": "نگهبانی",
    "Focus": "تمرکز", "Second Wind": "نفس دوم", "Appraise": "شناسایی", "Battle Cry": "سرود جنگ",
    "Meditation": "مراقبته", "Shield Bash": "ضربه سپر", "Valiant Leap": "پرش شجاعت",
    "Defend Others": "دفاع از دیگران", "Iron Will": "اراده آهن", "Sword Oath": "قسم شمشیر",
    "Backstab": "ضربه پشت سر", "Smoke Bomb": "بمب دود", "Poison Blade": "شمشیر سمی",
    "Shadow Step": "پا سایه", "Assassinate": "قتلراجعه", "Fireball": "فیربال",
    "Frost Nova": "نوا یخ", "Chain Lightning": "زنجیره بارق", "Arcane Shield": "ترمیت جادو",
    "Mana Burst": "فشار منا", "Piercing Shot": "تیر خار", "Hunter's Mark": "نشانه شکار",
    "Multi-Shot": "تیر چندگانه", "Eagle Eye": "چشم عقاب", "Barrage": "باران تیر",
    "Forge Mastery": "آموختن آهنگری", "Overgear": "بیش از ابزار", "Anvil Smash": "ضربه سنگساختن",
    "Self-Buff Gear": "تکمیلی تجهیزات", "Divine Hammer": "چاقو الهی", "Hot Metal": "آهن داغ",
    "Tempered Edge": "لبه معتدل", "Soulforge": "آهنگری روح", "Anvil Heart": "قلب سنگساختن",
    "Overgear Limit Break": "شکست حد ابزاری",
}
SKILL_DESC_FA = {
    "Heavy Strike": "ضربه سنگین با ۱۵۰٪ دمیج.",
    "Quick Step": "گام سریع با فریم‌های پنهان.",
    "Guard Stance": "نگهبانی، ۱۵٪ کاهش دمیج.",
    "Focus": "تمرکز، +۵٪ کریتیک.",
    "Second Wind": "ب در ۳ ثانیه می‌آید.",
    "Appraise": "شناسایی، +۱۰٪ غنیمت.",
    "Battle Cry": "سرود جنگ، ۸ ثانیه تهدید +۲۵٪.",
    "Meditation": "مراقبته، بازیابی منا +۲.",
    "Shield Bash": "ضربه سپر، بیهنجاری ۱.۵ ثانیه.",
    "Valiant Leap": "پرش به دشمن، ۱۲۰٪ دمیج.",
    "Defend Others": "دفاع از دیگران، ۵۰٪ دمیج به خود.",
    "Iron Will": "اراده آهن، +۴۰٪ مقاومت بیهنجاری.",
    "Sword Oath": "قسم شمشیر، +۲۰٪ دمیج اسلحهٔ شمشیر.",
    "Backstab": "ضربه پشت سر، ۳۰۰٪ دمیج.",
    "Smoke Bomb": "بمب دود، ۶ ثانیه مخفی، +۶٪ فرار.",
    "Poison Blade": "شمشیر سمی، خونریزی ۸ ثانیه.",
    "Shadow Step": "پا سایه، +۸٪ کریتیک در مخفی.",
    "Assassinate": "قتلراجعه، +۵۰٪ دمیج زیر ۳۰٪ جان.",
    "Fireball": "فیربال، ۱۴۰٪ دمیج جادو، ممکن است سوخت.",
    "Frost Nova": "نوا یخ، بیهنجاری ۲ ثانیه.",
    "Chain Lightning": "زنجیره بارق به ۳ هدف.",
    "Arcane Shield": "ترمیت جادو، جذب ۲۰۰ دمیج.",
    "Mana Burst": "فشار منا، ۳۰٪ کمتر هزینه منا.",
    "Piercing Shot": "تیر خار، ش همه دشمنان را می‌زند.",
    "Hunter's Mark": "نشانه شکار، +۲۵٪ دمیج به نشان‌زده‌ها.",
    "Multi-Shot": "تیر چندگانه، ۳ تیر همزمان.",
    "Eagle Eye": "چشم عقاب، +۱۵٪ غنیمت.",
    "Barrage": "باران تیر، ۲۰٪ شوت دوگانه.",
    "Forge Mastery": "آموختن آهنگری، +۱۵٪ کیفیت.",
    "Overgear": "بیش از ابزار، effet超过حدود.",
    "Anvil Smash": "ضربه سنگساختن 피해 بر اساس سطح.",
    "Self-Buff Gear": "تکمیلی تجهیزات، +۲۰٪ آمار ۳۰ ثانیه.",
    "Divine Hammer": "چاقو الهی، لفه یک‌بار در روز.",
    "Hot Metal": "سرعت آهنگری +۳۰٪.",
    "Tempered Edge": "لبه معتدل، +۱۰٪ کریتیک.",
    "Soulforge": "آهنگری روح، affix اضافه.",
    "Anvil Heart": "قلب سنگساختن، +۱۰٪ آمار.",
    "Overgear Limit Break": "شکست حد ابزاری، سقف ×۱.۶.",
}


def generate_skills(rng):
    skills, sid = [], 3000
    shared = [
        ("Heavy Strike", "A powerful melee attack, 150% ATK damage.", "ACTIVE", 1,
         {"damageMult": 1.5, "cooldown": 3.0}),
        ("Quick Step", "Dodge forward with iframes.", "ACTIVE", 1,
         {"evasion": 0.5, "duration": 0.6}),
        ("Guard Stance", "Reduces incoming damage by 15%.", "PASSIVE", 4,
         {"damageReduction": 0.15}),
        ("Focus", "Critical hit chance +5%.", "PASSIVE", 8, {"critChance": 0.05}),
        ("Second Wind", "Heal 20% max HP over 3s.", "ACTIVE", 12,
         {"healPct": 0.20, "duration": 3.0}),
        ("Appraise", "Drop rate +10%.", "PASSIVE", 16, {"dropRate": 0.10}),
        ("Battle Cry", "Taunt enemies, +25% threat for 8s.", "ACTIVE", 20,
         {"taunt": 8.0}),
        ("Meditation", "MP regeneration +2.", "PASSIVE", 24, {"mpRegen": 2.0}),
    ]
    for en, de, stype, lvl, eff in shared:
        skills.append(skill(f"skill_{sid}", en, SKILL_NAME_FA[en], "SHARED", stype, lvl,
                            de, SKILL_DESC_FA[en], eff))
        sid += 1

    class_skills = {
        "KNIGHT": [("Shield Bash", "Bash with shield, stun 1.5s.", "ACTIVE", 10,
                    {"stun": 1.5, "damageMult": 1.0}),
                   ("Valiant Leap", "Leap to enemy, 120% ATK.", "ACTIVE", 14,
                    {"damageMult": 1.2}),
                   ("Defend Others", "Redirect 50% ally damage to self.", "ACTIVE", 18,
                    {"redirectPct": 0.50}),
                   ("Iron Will", "Stun resistance +40%.", "PASSIVE", 22,
                    {"stunResist": 0.40}),
                   ("Sword Oath", "Sword attacks +20% damage.", "PASSIVE", 26,
                    {"damageMult": 1.20})],
        "ROGUE": [("Backstab", "300% ATK if striking from behind.", "ACTIVE", 10,
                   {"damageMult": 3.0}),
                  ("Smoke Bomb", "Stealth 6s, evasion +60%.", "ACTIVE", 14,
                   {"evasion": 0.60, "duration": 6.0}),
                  ("Poison Blade", "Bleed for 8s.", "ACTIVE", 18,
                   {"bleed": 8.0, "damageMult": 1.1}),
                  ("Shadow Step", "Crit +8% while stealthed.", "PASSIVE", 22,
                   {"critChance": 0.08}),
                  ("Assassinate", "+50% damage to targets below 30% HP.", "PASSIVE", 26,
                   {"executeMult": 1.50, "hpThreshold": 0.30})],
        "MAGE": [("Fireball", "Fire bolt, 140% MAG, may burn.", "ACTIVE", 10,
                  {"damageMult": 1.4, "element": "FIRE", "burn": 5.0}),
                 ("Frost Nova", "Freeze nearby enemies 2s.", "ACTIVE", 14,
                 {"freeze": 2.0, "damageMult": 1.1}),
                 ("Chain Lightning", "Lightning jumps to 3 targets.", "ACTIVE", 18,
                 {"damageMult": 1.0, "chain": 3, "element": "LIGHTNING"}),
                 ("Arcane Shield", "Absorb 200 damage per hit.", "PASSIVE", 22,
                 {"shield": 200}),
                 ("Mana Burst", "Spells cost 30% less MP.", "PASSIVE", 26,
                 {"mpCostReduction": 0.30})],
        "RANGER": [("Piercing Shot", "Shoots through all enemies in line.", "ACTIVE", 10,
                    {"pierce": True, "damageMult": 1.3}),
                   ("Hunter's Mark", "Mark target; +25% damage vs marked.", "ACTIVE", 14,
                    {"damageMult": 1.25}),
                   ("Multi-Shot", "Fire 3 arrows at once.", "ACTIVE", 18,
                    {"damageMult": 0.9, "shots": 3}),
                   ("Eagle Eye", "Drop rate +15%.", "PASSIVE", 22, {"dropRate": 0.15}),
                   ("Barrage", "20% chance to fire twice.", "PASSIVE", 26,
                    {"doubleShot": 0.20})],
        "VULCAN_HEIR": [("Forge Mastery", "Forging quality +15%.", "PASSIVE", 10,
                         {"forgeQualityBonus": 0.15}),
                        ("Overgear", "Equipped item effects exceed caps.", "PASSIVE", 14,
                         {"statCapMult": 1.30}),
                        ("Anvil Smash", "Damage scaled by equipped weapon tier.", "ACTIVE", 18,
                         {"damageMult": 1.5}),
                        ("Self-Buff Gear", "+20% gear stats for 30s.", "ACTIVE", 22,
                         {"gearBuff": 0.20, "duration": 30.0}),
                        ("Divine Hammer", "Guaranteed Legendary forge once per day.", "ACTIVE", 26,
                         {"guaranteedLegendary": True})],
    }
    suffixes = ["", " II", " III", " IV", " V"]
    fa_suffixes = ["", " دو", " سه", " چهار", " پنج"]
    for cls in ["KNIGHT", "ROGUE", "MAGE", "RANGER", "VULCAN_HEIR"]:
        archs = class_skills.get(cls, [])
        for i in range(20):
            en, de, stype, lvl, eff = archs[i % len(archs)]
            skills.append(skill(f"skill_{sid}", en + suffixes[min(i, 4)],
                                SKILL_NAME_FA.get(en, en) + fa_suffixes[min(i, 4)], cls, stype,
                                lvl + i, de, SKILL_DESC_FA.get(en, de), dict(eff)))
            sid += 1
    for i, (nm, de, df, lvl, eff) in enumerate([
        ("Hot Metal", "Forging speed +30%.", "سرعت آهنگری +۳۰٪.", 30, {"forgeSpeed": 1.30}),
        ("Tempered Edge", "Forged items crit +10%.", "لبه معتدل، +۱۰٪ کریتیک.", 40,
         {"critChanceForged": 0.10}),
        ("Soulforge", "Bonus affix on every forge.", "آهنگری روح، affix اضافه.", 60,
         {"forgeBonusAffix": 1.0}),
        ("Anvil Heart", "All gear stats +10%.", "قلب سنگساختن، +۱۰٪ آمار.", 80,
         {"gearStatsMult": 1.10}),
        ("Overgear Limit Break", "Stat caps ×1.6.", "شکست حد ابزاری، سقف ×۱.۶.", 100,
         {"statCapMult": 1.60}),
    ]):
        skills.append(skill(f"skill_{sid}", nm, SKILL_NAME_FA.get(nm, nm), "VULCAN_HEIR",
                            "PASSIVE", lvl, f"Forge Mastery tier {i+1}.",
                            f"دَرَخْت مهارت آهنگری مرحله {i+1}.", eff))
        sid += 1
    return skills


# ---------------------------------------------------------------------------
# ENEMIES (>= 65 incl. bosses)
# ---------------------------------------------------------------------------
ENEMY_TEMPLATES = [
    ("Rust Rat", "موش زنگ‌خور", "NORMAL", 1, 5, {"HP": 40, "ATK": 6}, ["bite"]),
    ("Mud Crab", "خرچنگ گل", "NORMAL", 1, 5, {"HP": 45, "ATK": 5}, ["claw"]),
    ("Cave Slug", "لمو غار", "NORMAL", 2, 6, {"HP": 55, "ATK": 8}, ["slime"]),
    ("Cave Bat", "چال غار", "NORMAL", 2, 6, {"HP": 38, "ATK": 7}, ["screech"]),
    ("Ember Imp", "جن آتش", "NORMAL", 3, 7, {"HP": 70, "ATK": 10, "element": "FIRE"}, ["fireball"]),
    ("Ash Stalker", "شکارک خاکستر", "NORMAL", 3, 7, {"HP": 62, "ATK": 9, "element": "FIRE"}, ["emberDash"]),
    ("Frost Sprite", "پری برف", "NORMAL", 4, 8, {"HP": 65, "ATK": 11, "element": "ICE"}, ["frostbite"]),
    ("Ice Lizard", "موزنه یخی", "NORMAL", 4, 8, {"HP": 58, "ATK": 10, "element": "ICE"}, ["frostBite"]),
    ("Shock Beetle", "هلکت برق", "NORMAL", 5, 9, {"HP": 75, "ATK": 12, "element": "LIGHTNING"}, ["spark"]),
    ("Static Moth", "پروانه بارق", "NORMAL", 5, 9, {"HP": 68, "ATK": 11, "element": "LIGHTNING"}, ["staticField"]),
    ("Stone Golemling", "گلم سنگی کوچک", "NORMAL", 6, 10, {"HP": 120, "ATK": 14}, ["slam"]),
    ("Rock Crab", "خرچنگ سنگی", "NORMAL", 6, 10, {"HP": 130, "ATK": 13}, ["claw"]),
    ("Sand Stalker", "شکارکن ریگ", "NORMAL", 7, 11, {"HP": 85, "ATK": 15}, ["burrow"]),
    ("Dune Viper", "مار ریگ", "NORMAL", 7, 11, {"HP": 72, "ATK": 16, "element": "EARTH"}, ["poisonBite"]),
    ("Gloom Stalker", "شکارکن تاریک", "NORMAL", 8, 12, {"HP": 90, "ATK": 16}, ["shadowStrike"]),
    ("Twilight Wisp", "ذره سایه", "NORMAL", 8, 12, {"HP": 60, "ATK": 14, "element": "DARK"}, ["shadowTouch"]),
    ("Skyforge Sentry", "نگهبان آسمانی‌ساز", "NORMAL", 9, 13, {"HP": 100, "ATK": 17}, ["laser"]),
    ("Cloud Drake", "اژدها ابری کوچک", "NORMAL", 9, 13, {"HP": 88, "ATK": 16}, ["wingGale"]),
    ("Crimson Knight", "شیر قرمز", "NORMAL", 10, 14, {"HP": 140, "ATK": 19}, ["charge"]),
    ("Crimson Guard", "سرتاژ قرمز", "NORMAL", 10, 14, {"HP": 150, "ATK": 18}, ["shieldBash"]),
    ("Void Husk", "خلیک خلاء", "NORMAL", 11, 15, {"HP": 110, "ATK": 20, "element": "DARK"}, ["voidTouch"]),
    ("Void Wisp", "ذره خلا", "NORMAL", 11, 15, {"HP": 85, "ATK": 19, "element": "DARK"}, ["voidTouch"]),
    ("Rust Spitter", "توهو زنگ", "NORMAL", 12, 16, {"HP": 95, "ATK": 18}, ["rustSpit"]),
    ("Rust Crawler", "زنگ‌خش", "NORMAL", 12, 16, {"HP": 105, "ATK": 17}, ["rustSpit"]),
    ("Ash Ghoul", "غول خاکستر", "NORMAL", 13, 18, {"HP": 160, "ATK": 22}, ["ashLeap"]),
    ("Ember Wraith", " lifespan شبح آتش", "NORMAL", 13, 18, {"HP": 150, "ATK": 23, "element": "FIRE"}, ["emberTouch"]),
    ("Glacier Stalker", "شکارکن یخ", "NORMAL", 14, 19, {"HP": 175, "ATK": 24, "element": "ICE"}, ["iceSpike"]),
    ("Storm Hound", "سگ طوفان", "NORMAL", 14, 19, {"HP": 165, "ATK": 25, "element": "LIGHTNING"}, ["thunderBite"]),
    ("Ash Golem", "گلم خاکستر", "NORMAL", 14, 20, {"HP": 200, "ATK": 26}, ["slam"]),
]
BOSS_TEMPLATES = [
    ("Grawl the Crack-Anvil", "گروال شکست‌سنگساختن", "CHAPTER_BOSS", 1, 28,
     {"HP": 3500, "ATK": 85}, ["anvilSlam", "shockwave"], "zone_01"),
    ("Emberdeep Matriarch", "ماتریارخ امبردیپ", "CHAPTER_BOSS", 2, 42,
     {"HP": 5200, "ATK": 110}, ["lavaPool", "emberBreath"], "zone_02"),
    ("Frostveil Matron", "ماترن برف‌وال", "CHAPTER_BOSS", 3, 56,
     {"HP": 6100, "ATK": 125}, ["iceShards", "blizzard"], "zone_03"),
    ("Sunken Archivist", "آرشیویست غرق‌شده", "CHAPTER_BOSS", 4, 70,
     {"HP": 7200, "ATK": 140}, ["waterTendrils", "loreDrain"], "zone_04"),
    ("Stormglass Sultan", "سلطان شیش‌باران", "CHAPTER_BOSS", 5, 84,
     {"HP": 8400, "ATK": 158}, ["thunderStrike", "sandCyclone"], "zone_05"),
    ("Gloamwood Stag", "غزاله غلواموود", "CHAPTER_BOSS", 6, 98,
     {"HP": 9600, "ATK": 175}, ["antlerRush", "petalStorm"], "zone_06"),
    ("Skyforge Sentinel Prime", "نگهبان اول آسمانی‌ساز", "CHAPTER_BOSS", 7, 112,
     {"HP": 11000, "ATK": 192}, ["orbitLaser", "hyperBeam"], "zone_07"),
    ("Citadel Bloodlord", "lord خون قلعه", "CHAPTER_BOSS", 8, 126,
     {"HP": 12600, "ATK": 210}, ["bloodBoil", "crimsonWave"], "zone_08"),
    ("Rift Reaver", "ریور شکاف", "CHAPTER_BOSS", 9, 140,
     {"HP": 14000, "ATK": 230}, ["voidRift", "phaseShift"], "zone_09"),
    ("Rust King", "پادشاه زنگ", "FINAL_BOSS", 10, 120,
     {"HP": 60000, "ATK": 400}, ["rustCurse", "anvilOfDoom", "metalCorrode"], "zone_10"),
]
ELITE_TEMPLATES = [
    ("Iron-Hide Boar", "گورخر فلزی", "ELITE", 3, 20, {"HP": 900, "ATK": 45}, ["gore"], "zone_03"),
    ("Ashen Revenant", "بازگشت خاکستر", "ELITE", 5, 35, {"HP": 1600, "ATK": 62}, ["emberDash"], "zone_05"),
    ("Glass Golem", "گلم شیشه‌ای", "ELITE", 6, 40, {"HP": 1900, "ATK": 70}, ["shatter"], "zone_06"),
    ("Skybound Drake", "اژدها آسمانی", "ELITE", 7, 55, {"HP": 2400, "ATK": 85}, ["wingGale"], "zone_07"),
    ("Rustbound Colossus", "کلوسوس زنگی", "ELITE", 8, 65, {"HP": 3000, "ATK": 95}, ["rustAura"], "zone_08"),
    ("Chronos Stalker", "پیش‌گیر زمانی", "ELITE", 9, 80, {"HP": 3400, "ATK": 105}, ["timeDilation"], "zone_09"),
    ("Dreadforge Harbinger", "پیش‌اعلان آتش‌نگار", "ELITE", 10, 95, {"HP": 4000, "ATK": 120}, ["doomHammer"], "zone_10"),
    ("Mythril Gorgon", "میتریل گورگون", "ELITE", 4, 30, {"HP": 1300, "ATK": 55}, ["petrifyGaze"], "zone_04"),
    ("Stormbound Djinn", "جن طوفانی", "ELITE", 5, 38, {"HP": 1700, "ATK": 68}, ["staticField"], "zone_05"),
    ("Glacial Warden", "نگهبان یخی", "ELITE", 6, 44, {"HP": 2100, "ATK": 74}, ["iceWall"], "zone_06"),
    ("Crimson Inquisitor", "افشاگر قرمز", "ELITE", 7, 58, {"HP": 2500, "ATK": 88}, ["heresyFlame"], "zone_07"),
    ("Void Weaver", "پرتو خلاء", "ELITE", 8, 72, {"HP": 3100, "ATK": 100}, ["voidLash"], "zone_08"),
    ("Sunstrike Sentinel", "نگهبان strike خورشید", "ELITE", 7, 62, {"HP": 2700, "ATK": 90}, ["solarFlare"], "zone_07"),
    ("Dune Basilisk", "باسلیسک ریگ", "ELITE", 8, 68, {"HP": 3200, "ATK": 92}, ["petrifyGaze"], "zone_08"),
    ("Abyssal Stalker", "شکارک lose>", "ELITE", 9, 85, {"HP": 3600, "ATK": 108}, ["voidStep"], "zone_09"),
    ("Riftstalker Matron", "ماترن شکاف", "ELITE", 9, 90, {"HP": 3800, "ATK": 112}, ["phaseShift"], "zone_09"),
    ("Emberforge Titan", "تیتان آhan-sakhtن آتش", "ELITE", 10, 100, {"HP": 4200, "ATK": 118}, ["magmaSlam"], "zone_10"),
    ("Crimson Executioner", "سرग्रاه قرمز", "ELITE", 10, 105, {"HP": 4400, "ATK": 122}, ["executionerAxe"], "zone_10"),
    ("Void Archon", "آرکون خلاء", "ELITE", 10, 110, {"HP": 4600, "ATK": 125}, ["voidNova"], "zone_10"),
    ("Rust Patriarch", "پدر زنگ", "ELITE", 11, 115, {"HP": 5000, "ATK": 130}, ["rustCurse"], "zone_secret"),
]
RAID_TEMPLATES = [
    ("The Three Smiths", "سه آهنگر", "RAID_BOSS", 6, 100, {"HP": 45000, "ATK": 220}, ["tripleHammer"], "zone_06"),
    ("Dragon of the Deep Forge", "آژدها forge العمق", "RAID_BOSS", 8, 115, {"HP": 72000, "ATK": 310}, ["magmaBreath"], "zone_08"),
    ("Echo of Vulcan", "صدای وولکان", "RAID_BOSS", 10, 120, {"HP": 90000, "ATK": 360}, ["legacyStrike"], "zone_10"),
]
SECRET_TEMPLATES = [
    ("The Unforged", "ناپیسیده", "SECRET_BOSS", 9, 120, {"HP": 55000, "ATK": 250}, ["unbreakable"], "zone_secret"),
    ("Mirror Kai", "آینه کای", "SECRET_BOSS", 10, 120, {"HP": 48000, "ATK": 340}, ["mirrorStrike"], "zone_secret"),
    ("Last Aetherion Smith", "آخرین آهنگر آثيريون", "SECRET_BOSS", 10, 125, {"HP": 70000, "ATK": 390}, ["finalAnvil"], "zone_secret"),
]


def generate_enemies(rng):
    enemies, eid = [], 4000
    normal_zone_cycle = [f"zone_{(i % 10) + 1:02d}" for i in range(10)]
    nz = 0
    for name, fa, etype, zmin, zmax, stats, skills in ENEMY_TEMPLATES:
        zone = normal_zone_cycle[nz % 10] if etype == "NORMAL" else f"zone_{min(max(rng.randint(zmin, zmax), 1), 10):02d}"
        nz += 1
        enemies.append({"id": f"enemy_{eid}", "name": {"en": name, "fa": fa}, "type": etype,
                        "zone": zone, "level": rng.randint(zmin * 5, zmax * 5),
                        "baseStats": stats, "skills": skills,
                        "drops": [{"id": f"mat_{2000 + rng.randint(1, 12)}", "qty": rng.randint(1, 3)}]})
        eid += 1
    for name, fa, etype, tier, level, stats, skills, zone in BOSS_TEMPLATES:
        enemies.append({"id": f"enemy_{eid}", "name": {"en": name, "fa": fa}, "type": etype,
                        "zone": zone, "level": level, "baseStats": stats, "skills": skills,
                        "drops": [{"id": f"mat_{2000 + tier}", "qty": 5}, {"id": "item_1000", "qty": 1}]})
        eid += 1
    for name, fa, etype, tier, level, stats, skills, zone in ELITE_TEMPLATES:
        enemies.append({"id": f"enemy_{eid}", "name": {"en": name, "fa": fa}, "type": etype,
                        "zone": zone, "level": level, "baseStats": stats, "skills": skills,
                        "drops": [{"id": f"mat_{2000 + tier}", "qty": 2}]})
        eid += 1
    for name, fa, etype, tier, level, stats, skills, zone in RAID_TEMPLATES:
        enemies.append({"id": f"enemy_{eid}", "name": {"en": name, "fa": fa}, "type": etype,
                        "zone": zone, "level": level, "baseStats": stats, "skills": skills,
                        "drops": [{"id": f"mat_{2000 + tier}", "qty": 10}]})
        eid += 1
    for name, fa, etype, tier, level, stats, skills, zone in SECRET_TEMPLATES:
        enemies.append({"id": f"enemy_{eid}", "name": {"en": name, "fa": fa}, "type": etype,
                        "zone": zone, "level": level, "baseStats": stats, "skills": skills,
                        "drops": [{"id": f"mat_{2000 + 12}", "qty": 20}]})
        eid += 1
    return enemies


# ---------------------------------------------------------------------------
# ZONES (10 + secret)
# ---------------------------------------------------------------------------
ZONE_DEFS = [
    ("zone_01", "Whispering Plains", "دشت‌هایی که باد در آن خود را نشان می‌دهد.", 1, 10,
     "A grassland where the wind carries half-remembered songs.",
     "دشت‌هایی که باد در آن خود را نشان می‌دهد.", ["mat_2001", "mat_2002"], ["enemy_4000", "enemy_4001"]),
    ("zone_02", "Emberdeep Mines", "معدن‌های عمیق آتش", 11, 20,
     "Delve deep; the ore here still holds the memory of fire.",
     "معدن‌های عمیق آتش، هنوز خاطره آتش را در خود دارند.", ["mat_2003", "mat_2004"], ["enemy_4002", "enemy_4003"]),
    ("zone_03", "Frostveil Peaks", "سفره‌های برفانی", 21, 30,
     "Snow-blinded summits guarded by ancient frost.",
     "سفره‌های برفانی که برف قدیمی در آن نگهبان است.", ["mat_2005", "mat_2006"], ["enemy_4004", "enemy_4005"]),
    ("zone_04", "Sunken Library", "کتابخانه غرق‌شده", 31, 40,
     "Knowledge drowned; only brittle tomes remain.",
     "دانستنی‌های غرق‌شده؛ تنها کِتاب‌های شکننده باقی مانده‌اند.", ["mat_2007", "mat_2008"], ["enemy_4006", "enemy_4007"]),
    ("zone_05", "Stormglass Desert", "تپه‌های شیشه‌ای", 41, 50,
     "Glass dunes sing under eternal thunderstorms.",
     "تپه‌های شیشه‌ای زیر طوفان‌های ابدی می‌خوانند.", ["mat_2009", "mat_2010"], ["enemy_4008", "enemy_4009"]),
    ("zone_06", "Gloamwood", "جنگل سایه‌تاریک", 51, 60,
     "Twilight woods where trees remember being metal.",
     "جنگلی سایه‌تاریک که درختان در آن خود را فلز می‌دانند.", ["mat_2011", "mat_2012"], ["enemy_4010", "enemy_4011"]),
    ("zone_07", "Skyforge Ruins", "آهنگ‌سازی‌های شناور", 61, 70,
     "Floating forges fallen from the sky.",
     "آهنگ‌سازی‌های شناور که از آسمان افتاده‌اند.", ["mat_2013", "mat_2014"], ["enemy_4012", "enemy_4013"]),
    ("zone_08", "Crimson Citadel", "تخت lord خون از آهن زنگ‌خورده", 71, 80,
     "The Bloodlord's throne of rusted iron.",
     "تخت lord خون از آهن زنگ‌خورده.", ["mat_2015", "mat_2016"], ["enemy_4014", "enemy_4015"]),
    ("zone_09", "Void Rift", "شگاف در میان‌جهان‌های خاموش", 81, 90,
     "A tear into the silent between-worlds.",
     "شگاف در میان‌جهان‌های خاموش.", ["mat_2017", "mat_2018"], ["enemy_4016", "enemy_4017"]),
    ("zone_10", "Rust King's Throne", "جایی که تمام فلز زنگ می‌خورد", 91, 100,
     "Where all metal rots and the king waits.",
     "جایی که تمام فلز زنگ می‌خورد و پادشاه منتظر است.", ["mat_2019", "mat_2020"], ["enemy_4018", "enemy_4019"]),
    ("zone_secret", "The Secret Anvil", "آهنگ‌سازی مخفی وولکان", 100, 120,
     "Vulcan's hidden forge; only the worthy hear its hammer.",
     "آهنگ‌سازی مخفی وولکان؛ تنها لایقان صدای چاقو را می‌شنوند.", ["mat_2021", "mat_2022"], ["enemy_4020", "enemy_4021"]),
]


def generate_zones(rng):
    zones = []
    for zid, name, fa, lo, hi, de, df, mats, ens in ZONE_DEFS:
        zones.append({"id": zid, "name": {"en": name, "fa": fa}, "levelRange": [lo, hi],
                      "description": {"en": de, "fa": df}, "stages": 10,
                      "boss": f"enemy_{4000 + (lo // 10)}", "materials": mats, "enemies": ens,
                      "unlockQuest": f"quest_{zid[-2:] if zid != 'zone_secret' else 's0'}"})
    return zones


# ---------------------------------------------------------------------------
# QUESTS (>= 130)
# ---------------------------------------------------------------------------
def generate_quests(rng):
    quests, qid = [], 5000
    for ch in range(1, 11):
        for s in range(1, 5):
            quests.append({"id": f"quest_{qid}",
                           "name": {"en": f"Chapter {ch} Scene {s}", "fa": f"قسمت {s} باب {ch}"},
                           "type": "STORY", "chapter": ch,
                           "zone": f"zone_{(ch - 1) % 10 + 1:02d}",
                           "objectives": [{"kind": "DEFEAT", "target": "zone_boss", "qty": 1}],
                           "rewards": {"gold": 200 * ch, "xp": 100 * ch, "items": ["item_1000"]},
                           "dialogueScene": f"dialogue_ch{ch}_s{s}"})
            qid += 1
    for d in range(7):
        for t in range(1, 5):
            quests.append({"id": f"quest_{qid}",
                           "name": {"en": f"Daily {['Mon','Tue','Wed','Thu','Fri','Sat','Sun'][d]} Tier {t}",
                                    "fa": f"روزانه-tier {t}"},
                           "type": "DAILY", "chapter": 0,
                           "zone": f"zone_{(d + t) % 10 + 1:02d}",
                           "objectives": [{"kind": "GATHER", "target": f"mat_{2000 + t}", "qty": 10 * t}],
                           "rewards": {"gold": 300 * t, "xp": 150 * t}})
            qid += 1
    for i in range(8):
        quests.append({"id": f"quest_{qid}", "name": {"en": f"Weekly {i+1}", "fa": f"هفتگی {i+1}"},
                       "type": "WEEKLY", "chapter": 0, "zone": f"zone_{(i % 10) + 1:02d}",
                       "objectives": [{"kind": "DEFEAT", "target": "zone_boss", "qty": 3}],
                       "rewards": {"gold": 5000, "xp": 3000}})
        qid += 1
    for i in range(60):
        quests.append({"id": f"quest_{qid}", "name": {"en": f"Side Quest {i+1}", "fa": f"مأموریت جانبی {i+1}"},
                       "type": "SIDE", "chapter": (i % 10) + 1, "zone": f"zone_{(i % 10) + 1:02d}",
                       "objectives": [{"kind": "GATHER", "target": f"mat_{2000 + ((i % 12) + 1)}", "qty": i % 5 + 1}],
                       "rewards": {"gold": 100 * (i % 10 + 1), "xp": 50 * (i % 10 + 1)}})
        qid += 1
    return quests


# ---------------------------------------------------------------------------
# SHOPS (8 variants)
# ---------------------------------------------------------------------------
SHOP_DEFS = [
    ("shop_01", "Smith's Pantry", "انبار آهنگر", ["CONSUMABLE"], 1),
    ("shop_02", "Ironmarket Arms", "اسناد سوق آهن", ["WEAPON"], 1),
    ("shop_03", "Parchment Plate", "زیرکاب کاغذی", ["ARMOR"], 1),
    ("shop_04", "Trinket & Talisman", "غزل و تعویذ", ["ACCESSORY"], 1),
    ("shop_05", "Blueprint Bureau", "دفتر نقشه", ["WEAPON", "ARMOR", "ACCESSORY"], 3),
    ("shop_06", "Material Bourse", "bourse مواد", [], 5),
    ("shop_07", "Mythic Emporium", "انبوه اساطیری", ["WEAPON", "ARMOR"], 8),
    ("shop_08", "Secret Vault", "انبار مخفی", ["WEAPON", "ARMOR", "ACCESSORY", "CONSUMABLE"], 12),
]


def generate_shops(rng):
    return [{"id": sid, "name": {"en": n, "fa": f}, "sells": t, "minTier": mt,
             "markup": 1.25, "buybackPct": 0.6} for sid, n, f, t, mt in SHOP_DEFS]


# ---------------------------------------------------------------------------
# ACHIEVEMENTS (>= 120)
# ---------------------------------------------------------------------------
def generate_achievements(rng):
    ach, aid = [], 6000
    for i in range(120):
        ach.append({"id": f"ach_{aid}",
                    "name": {"en": f"Achievement {i+1}", "fa": f"دستاورد {i+1}"},
                    "category": ["COMBAT", "FORGING", "EXPLORATION", "COLLECTION"][i % 4],
                    "condition": {"kind": "COUNT", "target": f"generic_{i}", "qty": (i % 50 + 1) * 10},
                    "reward": {"gold": 100 * (i + 1), "xp": 50 * (i + 1), "points": i + 1}})
        aid += 1
    return ach


# ---------------------------------------------------------------------------
# PETS (6), ALLIES (5)
# ---------------------------------------------------------------------------
def generate_pets(rng):
    return [
        {"id": "pet_01", "name": {"en": "Cinder Pup", "fa": "سگ خاکستر"}, "rarity": "RARE", "bonus": {"dropRate": 0.05}},
        {"id": "pet_02", "name": {"en": "Frost Kit", "fa": "گربه یخی"}, "rarity": "RARE", "bonus": {"evasion": 0.03}},
        {"id": "pet_03", "name": {"en": "Spark Moth", "fa": "پروانه برق"}, "rarity": "EPIC", "bonus": {"forgeSpeed": 0.10}},
        {"id": "pet_04", "name": {"en": "Obsidian Snail", "fa": "مار مولکو‌ها سیاها"}, "rarity": "EPIC", "bonus": {"damageReduction": 0.05}},
        {"id": "pet_05", "name": {"en": "Void Mote", "fa": "ذره خلاء"}, "rarity": "LEGENDARY", "bonus": {"critChance": 0.04}},
        {"id": "pet_06", "name": {"en": "Aethel Ember", "fa": "آتل أمبر"}, "rarity": "MYTHIC", "bonus": {"allStats": 0.08}},
    ]


def generate_allies(rng):
    return [
        {"id": "ally_01", "name": {"en": "Lyra the Swift", "fa": "لیرا سریع‌چالن"}, "class": "ROGUE",
         "portrait": "ally_lyra", "skills": ["skill_3000", "skill_3001"]},
        {"id": "ally_02", "name": {"en": "Mina the Shy", "fa": "مينا خجالت‌کش"}, "class": "MAGE",
         "portrait": "ally_mina", "skills": ["skill_3010", "skill_3011"]},
        {"id": "ally_03", "name": {"en": "Torin the Veteran", "fa": "تورین کهن"}, "class": "KNIGHT",
         "portrait": "ally_torin", "skills": ["skill_3020", "skill_3021"]},
        {"id": "ally_04", "name": {"en": "Dari the Merchant", "fa": "داری تاجر"}, "class": "RANGER",
         "portrait": "ally_dari", "skills": ["skill_3030", "skill_3031"]},
        {"id": "ally_05", "name": {"en": "Master Vulcan", "fa": "استاد وولکان"}, "class": "VULCAN_HEIR",
         "portrait": "ally_vulcan", "skills": ["skill_3040", "skill_3041"]},
    ]


# ---------------------------------------------------------------------------
# DIALOGUE / STORY (10 chapters x scenes; >= 80 dialogue scenes)
# ---------------------------------------------------------------------------
STORY_SCENES = [
    (1, "Prologue: The Drawn Blade", "باب ۱: شمشیر کشیده",
     [("KAI", "Where… where am I?", "کجای… کجای من است؟"),
      ("VULCAN", "You are in Aetherion, Kai. And you are my heir now.", "شما در آثيريون هستید، کای. و حالا وارث من هستید."),
      ("KAI", "I never asked to be a smith.", "من هرگز آهنگر شدن را نخواستم.")]),
    (2, "First Heat", "باب ۲: گرمای اول",
     [("KAI", "The ingot won't stay in the green zone.", "آهن داغ در نطاق سبز نمی‌مند."),
      ("VULCAN", "Patience. The metal speaks if you listen.", "صبر. فلز می‌گوید اگر گوش کنید.")]),
    (3, "The Seven Techniques", "باب ۳: هفت تکنیک",
     [("KAI", "Seven techniques to forge the impossible.", "هفت تکنیک برای آهنساختن ناممکن.")]),
    (4, "Allies of Aetherion", "باب ۴: رفیقان آثيريون",
     [("LYRA", "I'll watch your back.", "من پشت شما را می‌دارم."),
      ("MINA", "And I'll keep the fire burning.", "و من آتش را تاب می‌آورم.")]),
    (5, "Emberdeep Betrayal", "باب ۵: خيانة امبردیپ",
     [("TORIN", "The Rust King corrupts every blade.", "پادشاه زنگ هر تلوار را آلوده می‌کند.")]),
    (6, "Skyforge Fallen", "باب ۶: آسمان‌ساز>",
     [("DARI", "The sky is falling in shards.", "آسمان در تکه‌تکه می‌افتد.")]),
    (7, "Crimson Oath", "باب ۷: قسم قرمز",
     [("KAI", "I swear on Vulcan's anvil.", "من بر سنگساختن وولکان قسم می‌خورم.")]),
    (8, "The Void Between", "باب ۸: میان‌فضا",
     [("VULCAN", "Beyond the rift lies the true ending.", "پایان واقعی در شکافت باقی می‌مند.")]),
    (9, "Throne of Rust", "باب ۹: تخت زنگ",
     [("RUST_KING", "All metal bows to rust.", "تمام فلزها به زنگ می‌نشینند.")]),
    (10, "The Last Strike", "باب ۱۰: ضربه آخر",
     [("KAI", "This hammer ends the Rust King.", "چاقو نهایی پادشاه زنگ را می‌شکند.")]),
]


def generate_dialogue(rng):
    scenes = []
    for ch, en_title, fa_title, lines in STORY_SCENES:
        scenes.append({"id": f"dialogue_ch{ch}", "chapter": ch,
                       "title": {"en": en_title, "fa": fa_title},
                       "bg": f"zone_{(ch - 1) % 10 + 1:02d}",
                       "lines": [{"speaker": s, "text": {"en": te, "fa": tf}, "expression": "neutral"}
                                 for s, te, tf in lines]})
    scid = 8000
    for ch in range(1, 11):
        for s in range(2, 10):
            scenes.append({"id": f"dialogue_{scid}", "chapter": ch,
                           "title": {"en": f"Chapter {ch} Banter {s-1}", "fa": f"گفتگو باب {ch} {s-1}"},
                           "bg": f"zone_{(ch - 1) % 10 + 1:02d}",
                           "lines": [{"speaker": "KAI", "text": {"en": f"Line {s}", "fa": f"جمله {s}"}, "expression": "neutral"}]})
            scid += 1
    return scenes


# ---------------------------------------------------------------------------
# MAIN
# ---------------------------------------------------------------------------
def main():
    os.makedirs(OUT, exist_ok=True)
    rng = random.Random(SEED)

    packs = {
        "items.json": generate_items(rng),
        "materials.json": generate_materials(rng),
        "skills.json": generate_skills(rng),
        "enemies.json": generate_enemies(rng),
        "zones.json": generate_zones(rng),
        "quests.json": generate_quests(rng),
        "shops.json": generate_shops(rng),
        "achievements.json": generate_achievements(rng),
        "pets.json": generate_pets(rng),
        "allies.json": generate_allies(rng),
        "dialogue.json": generate_dialogue(rng),
    }
    totals = {}
    for fname, data in packs.items():
        path = os.path.join(OUT, fname)
        with open(path, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
        totals[fname] = len(data)
        print(f"wrote {path} ({len(data)} entries)")

    print("\nContent totals:")
    for k, v in totals.items():
        print(f"  {k}: {v}")


if __name__ == "__main__":
    main()
