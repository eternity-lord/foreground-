package com.forgebound.game.progression

import com.forgebound.data.model.ContentQuest
import com.forgebound.data.model.QuestObjective
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProgressionTest {

    @Test
    fun killObjectiveCompletesQuest() {
        val quest = ContentQuest(
            id = "quest_test", name = mapOf("en" to "q", "fa" to "سوال"), type = "STORY",
            chapter = 1, zone = "zone_01",
            objectives = listOf(QuestObjective("DEFEAT", "enemy_4000", 3)),
            rewards = mapOf("gold" to 100), dialogueScene = null,
        )
        val prog = Progression.evaluateQuest(quest, kills = mapOf("enemy_4000" to 3),
                                             gathered = emptyMap(), alreadyCompleted = emptySet())
        assertTrue { prog.completed }
        assertTrue { prog.newlyClaimed }
    }

    @Test
    fun alreadyCompletedIsNotNewlyClaimed() {
        val quest = ContentQuest(
            id = "quest_test", name = mapOf("en" to "q", "fa" to "سوال"), type = "STORY",
            chapter = 1, zone = "zone_01",
            objectives = listOf(QuestObjective("DEFEAT", "enemy_4000", 1)),
            rewards = emptyMap(), dialogueScene = null,
        )
        val prog = Progression.evaluateQuest(quest, kills = mapOf("enemy_4000" to 1),
                                             gathered = emptyMap(), alreadyCompleted = setOf("quest_test"))
        assertTrue { prog.completed }
        assertFalse { prog.newlyClaimed }
    }

    @Test
    fun levelForXpCapsAt120() {
        assertEquals(120, Progression.levelForXp(Long.MAX_VALUE))
        assertEquals(1, Progression.levelForXp(0))
    }
}
