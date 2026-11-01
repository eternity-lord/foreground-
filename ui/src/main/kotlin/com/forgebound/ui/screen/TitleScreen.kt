package com.forgebound.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.forgebound.ui.theme.ForgeBrand
import com.forgebound.ui.theme.ForgeTheme
import com.forgebound.ui.theme.ForgeTypography

/**
 * Title screen (Section 6): ForgeBound key art, "New Game / Continue / Settings".
 * Displays original key art (assets/art/keyart_title.png) and brand buttons.
 */
@Composable
fun TitleScreen(
    onNewGame: () -> Unit,
    onContinue: () -> Unit,
    onSettings: () -> Unit
) {
    ForgeTheme {
        Box(modifier = Modifier.fillMaxSize().background(ForgeBrand.Background)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Key art placeholder (brand text; actual art loaded via Image in M13)
                Text(
                    text = "FORGEBOUND",
                    style = ForgeTypography.H1.copy(
                        color = ForgeBrand.EmberOrange,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "An anime-style blacksmith RPG — crafted entirely offline.",
                    style = ForgeTypography.BodySmall.copy(
                        color = ForgeBrand.TextSecondary
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onNewGame,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeBrand.EmberOrange,
                            contentColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Text("New Game", style = ForgeTypography.Button)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onContinue,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeBrand.SurfaceVariant,
                            contentColor = ForgeBrand.TextPrimary
                        )
                    ) {
                        Text("Continue", style = ForgeTypography.Button)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onSettings,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeBrand.SurfaceVariant,
                            contentColor = ForgeBrand.TextPrimary
                        )
                    ) {
                        Text("Settings", style = ForgeTypography.Button)
                    }
                }
            }
        }
    }
}
