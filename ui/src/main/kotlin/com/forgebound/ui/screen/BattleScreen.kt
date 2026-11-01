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
fun BattleScreen(onBack: () -> Unit) {
    ForgeTheme {
        Scaffold(
            topBar = {
                androidx.compose.material3.TopAppBar(
                    title = { Text("Zone 1 — Whispering Plains", style = ForgeTypography.H2) },
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
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Stage 1 of 10",
                    style = ForgeTypography.BodySmall.copy(
                        color = ForgeBrand.TextSecondary
                    )
                )
                Text(
                    text = "Wild Beast Lv. 3",
                    style = ForgeTypography.H2.copy(
                        color = ForgeBrand.TextPrimary
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = ForgeBrand.EmberOrange,
                        contentColor = Color(0xFFFFFFFF)
                    )
                ) {
                    Text("Attack", style = ForgeTypography.Button)
                }
            }
        }
    }
}
