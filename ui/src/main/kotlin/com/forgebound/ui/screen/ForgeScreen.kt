package com.forgebound.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forgebound.ui.theme.ForgeBrand
import com.forgebound.ui.theme.ForgeTheme
import com.forgebound.ui.theme.ForgeTypography

/**
 * Forging screen (Section 5.4 / Section 6): Heat → Hammer → Shape → Quench → Polish
 * minigame, quality 0-100 → grade mapping, Auto-Forge option.
 */
@Composable
fun ForgeScreen(
    onBack: () -> Unit,
    onForge: () -> Unit,
    onAutoForge: () -> Unit
) {
    ForgeTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Forging", style = ForgeTypography.H2) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ForgeBrand.Surface,
                        contentColor = ForgeBrand.TextPrimary
                    )
                )
            },
            modifier = Modifier.fillMaxSize().background(ForgeBrand.Background)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Heat · Hammer · Shape · Quench · Polish",
                    style = ForgeTypography.BodySmall.copy(
                        color = ForgeBrand.TextSecondary
                    )
                )
                Text(
                    text = "Quality: 0 / 100",
                    style = ForgeTypography.H2.copy(
                        color = ForgeBrand.MoltenGold
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onForge,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeBrand.EmberOrange,
                        contentColor = Color(0xFFFFFFFF)
                    )
                ) {
                    Text("Forge Weapon", style = ForgeTypography.Button)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onAutoForge,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeBrand.SurfaceVariant,
                        contentColor = ForgeBrand.TextPrimary
                    )
                ) {
                    Text("Auto-Forge (lower avg quality)", style = ForgeTypography.Button)
                }
            }
        }
    }
}
