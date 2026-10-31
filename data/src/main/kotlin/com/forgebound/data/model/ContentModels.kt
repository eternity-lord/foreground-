package com.forgebound.data.model

// Content pack models (Section 3). All content lives in assets/content/*.json,
// not hard-coded, so balance can be tuned without code changes. Fields mirror
// the JSON produced by tools/gen_content.py and checked by
// tools/validate_content.py.

data class ContentItem(
    val id: String,
    val name: Map<String, String>, // "en" / "fa"
    val type: String,              // WEAPON | ARMOR | ACCESSORY | CONSUMABLE
    val slot: String?,
    val tier: Int,
    val rarity: String,
    val levelReq: Int,
    val baseStats: Map<String, Any>,
    val blueprint: Blueprint?,
    val zoneDrop: String?,
    val classes: List<String>,
)

data class Blueprint(
    val gold: Int,
    val forgeSkillMin: Int,
    val materials: List<MaterialReq>,
)

data class MaterialReq(val id: String, val qty: Int)

data class ContentMaterial(
    val id: String,
    val name: Map<String, String>,
    val type: String, // ORE | WOOD | GEM | MONSTER_PART | ESSENCE
    val tier: Int,
    val dropZones: List<String>,
    val sellPrice: Int,
)

data class ContentSkill(
    val id: String,
    val name: Map<String, String>,
    val `class`: String, // NOVICE | KNIGHT | ROGUE | MAGE | RANGER | VULCAN_HEIR | SHARED
    val type: String,    // ACTIVE | PASSIVE
    val levelReq: Int,
    val description: Map<String, String>,
    val effects: Map<String, Any>,
)

data class ContentEnemy(
    val id: String,
    val name: Map<String, String>,
    val type: String, // NORMAL | ELITE | CHAPTER_BOSS | RAID_BOSS | FINAL_BOSS | SECRET_BOSS
    val zone: String,
    val level: Int,
    val baseStats: Map<String, Any>,
    val skills: List<String>,
    val drops: List<MaterialReq>,
)

data class ContentZone(
    val id: String,
    val name: Map<String, String>,
    val levelRange: List<Int>,
    val description: Map<String, String>,
    val stages: Int,
    val boss: String,
    val materials: List<String>,
    val enemies: List<String>,
    val unlockQuest: String?,
    val parallaxLayers: Int = 4,
)

data class QuestObjective(val kind: String, val target: String, val qty: Int)

data class ContentQuest(
    val id: String,
    val name: Map<String, String>,
    val type: String, // STORY | DAILY | WEEKLY | SIDE
    val chapter: Int,
    val zone: String,
    val objectives: List<QuestObjective>,
    val rewards: Map<String, Any>,
    val dialogueScene: String?,
)

data class ContentShop(
    val id: String,
    val name: Map<String, String>,
    val sells: List<String>,
    val minTier: Int,
    val markup: Double,
    val buybackPct: Double,
)

data class ContentAchievement(
    val id: String,
    val name: Map<String, String>,
    val category: String,
    val condition: Map<String, Any>,
    val reward: Map<String, Any>,
)

data class ContentPet(
    val id: String,
    val name: Map<String, String>,
    val rarity: String,
    val bonus: Map<String, Any>,
)

data class ContentAlly(
    val id: String,
    val name: Map<String, String>,
    val `class`: String,
    val portrait: String,
    val skills: List<String>,
)

data class DialogueLine(
    val speaker: String,
    val text: Map<String, String>, // "en" / "fa"
    val expression: String = "neutral",
)

data class DialogueScene(
    val id: String,
    val chapter: Int,
    val title: Map<String, String>,
    val bg: String,
    val lines: List<DialogueLine>,
)
