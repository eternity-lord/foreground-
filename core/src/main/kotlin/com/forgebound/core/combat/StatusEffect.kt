package com.forgebound.core.combat

/**
 * Status effects (Section 5.3): burn, freeze, shock, poison, bleed, stun, rust
 * (special) plus buffs/debuffs with stacking rules.
 */
enum class StatusEffect(val id: String, val stacks: Boolean, val maxStacks: Int) {
    BURN("burn", true, 5),
    FREEZE("freeze", false, 1),
    SHOCK("shock", true, 3),
    POISON("poison", true, 5),
    BLEED("bleed", true, 5),
    STUN("stun", false, 1),
    RUST("rust", true, 10), // special: reduces DEF over time, Section 8/9 flavor
    BUFF_ATK("atk_up", true, 5),
    DEBUFF_ATK("atk_down", true, 3),
    BUFF_DEF("def_up", true, 3),
    DEBUFF_DEF("def_down", true, 3),
}

data class ActiveStatus(
    val effect: StatusEffect,
    var remainingSeconds: Double,
    var stacks: Int = 1,
    val sourceLevel: Int = 1,
)
