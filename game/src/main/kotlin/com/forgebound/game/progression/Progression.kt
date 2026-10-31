package com.forgebound.game.progression

import com.forgebound.core.stats.PlayerStats
import com.forgebound.data.model.ContentQuest

/**
 * Quest & achievement progression (Section 5, 6).
 * Pure evaluation logic: given player state and content, returns completed
 * objectives and newly unlocked rewards. UI layer presents it.
 */
data class QuestProgress(
    val questId: String,
    val completed: Boolean,
    val newlyClaimed: Boolean,
)

object Progression {
    fun evaluateQuest(quest: ContentQuest,
                      kills: Map<String, Int>,
                      gathered: Map<String, Int>,
                      alreadyCompleted: Set<String>): QuestProgress {
        if (quest.id in alreadyCompleted) {
            return QuestProgress(quest.id, completed = true, newlyClaimed = false)
        }
        val completed = quest.objectives.all { obj ->
            when (obj.kind) {
                "DEFEAT" -> (kills[obj.target] ?: 0) >= obj.qty
                "GATHER" -> (gathered[obj.target] ?: 0) >= obj.qty
                else -> false
            }
        }
        return QuestProgress(quest.id, completed = completed, newlyClaimed = completed)
    }

    fun levelForXp(xp: Long): Int {
        var level = 1
        while (level < 120 && xp >= PlayerStats.xpForLevel(level + 1)) {
            level++
        }
        return level.coerceIn(1, 120)
    }
}
