package com.forgebound.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forgebound.ui.screen.*
import com.forgebound.ui.theme.ForgeBrand
import com.forgebound.ui.theme.ForgeTheme
import com.forgebound.ui.theme.ForgeTypography

/**
 * Main navigation shell (Section 6). Routes between all Section-6 screens
 * using Compose — no View-based placeholders anywhere in the UI.
 */
@Composable
fun MainNavigator() {
    var screen by remember { mutableStateOf<Screen>(Screen.TITLE) }

    ForgeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(ForgeBrand.Background)
        ) { innerPadding ->
            when (screen) {
                Screen.TITLE -> TitleScreen(
                    onNewGame = { screen = Screen.SAVE_SLOTS },
                    onContinue = { screen = Screen.HUB },
                    onSettings = { screen = Screen.SETTINGS }
                )
                Screen.SAVE_SLOTS -> SaveSlotsScreen(onBack = { screen = Screen.TITLE })
                Screen.HUB -> HubScreen(
                    onToBattle = { screen = Screen.BATTLE },
                    onToForging = { screen = Screen.FORGING },
                    onToInventory = { screen = Screen.INVENTORY_EQUIPMENT },
                    onToShop = { screen = Screen.SHOPS },
                    onToQuestLog = { screen = Screen.QUEST_LOG },
                    onToSettings = { screen = Screen.SETTINGS }
                )
                Screen.BATTLE -> BattleScreen(onBack = { screen = Screen.HUB })
                Screen.FORGING -> ForgeScreen(
                    onBack = { screen = Screen.HUB },
                    onForge = { },
                    onAutoForge = { }
                )
                Screen.INVENTORY_EQUIPMENT -> InventoryScreen(onBack = { screen = Screen.HUB })
                Screen.SHOPS -> ShopsScreen(onBack = { screen = Screen.HUB })
                Screen.QUEST_LOG -> QuestLogScreen(onBack = { screen = Screen.HUB })
                Screen.SETTINGS -> SettingsScreen(onBack = { screen = Screen.HUB })
                else -> PlaceholderScreen(screen.name, onBack = { screen = Screen.HUB })
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(
    label: String,
    onBack: () -> Unit
) {
    ForgeTheme {
        Scaffold(
            topBar = {
                androidx.compose.material3.TopAppBar(
                    title = { Text(label) },
                    navigationIcon = {
                        androidx.compose.material3.IconButton(onClick = onBack) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material3.Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) {
            Text(
                text = "$label — implementation in progress (M7–M13)",
                modifier = Modifier.padding(16.dp),
                style = ForgeTypography.Body
            )
        }
    }
}
