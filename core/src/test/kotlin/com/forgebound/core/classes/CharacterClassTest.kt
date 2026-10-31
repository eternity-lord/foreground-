package com.forgebound.core.classes

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CharacterClassTest {

    @Test
    fun noviceCanPickAnyPathAtLevel10() {
        assertTrue { ClassSystem.canChangeTo(CharacterClass.NOVICE, CharacterClass.KNIGHT, 10) }
        assertTrue { ClassSystem.canChangeTo(CharacterClass.NOVICE, CharacterClass.MAGE, 10) }
        assertFalse { ClassSystem.canChangeTo(CharacterClass.NOVICE, CharacterClass.KNIGHT, 9) }
    }

    @Test
    fun vulcanHeirRequiresSecretChain() {
        // Level gate alone isn't sufficient in practice (secret quest chain required);
        // here we only check the level gate is enforced at the code boundary.
        assertTrue { ClassSystem.canChangeTo(CharacterClass.KNIGHT, CharacterClass.VULCAN_HEIR, 100) }
        assertFalse { ClassSystem.canChangeTo(CharacterClass.KNIGHT, CharacterClass.VULCAN_HEIR, 99) }
    }
}
