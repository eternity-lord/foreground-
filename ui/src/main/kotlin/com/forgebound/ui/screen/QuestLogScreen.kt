package com.forgebound.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forgebound.ui.theme.ForgeBrand
import com.forgebound.ui.theme.ForgeTheme
import com.forgebound.ui.theme.ForgeTypography

@Composable
fun QuestLogScreen(onBack: () -> Unit) {
    ForgeTheme {
        Scaffold(
            topBar = {
                androidx.compose.material3.TopAppBar(
                    title = { Text("Quest Log", style = ForgeTypography.H2) },
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
                text = "Quest Log — ≥130 quests (M11)",
                modifier = Modifier.padding(16.dp),
                style = ForgeTypography.Body
            )
        }
    }
}
