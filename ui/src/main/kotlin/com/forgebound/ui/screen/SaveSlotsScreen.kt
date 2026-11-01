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
fun SaveSlotsScreen(onBack: () -> Unit) {
    ForgeTheme {
        Scaffold(
            topBar = {
                androidx.compose.material3.TopAppBar(
                    title = { Text("Save Slots", style = ForgeTypography.H2) },
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
                repeat(3) { i ->
                    Card(
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = ForgeBrand.SurfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Save Slot ${i + 1}",
                                style = ForgeTypography.Body
                            )
                            Text(
                                text = "Chapter 1 · Level 3",
                                style = ForgeTypography.BodySmall.copy(
                                    color = ForgeBrand.TextSecondary
                                )
                            )
                        }
                    }
                }
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = ForgeBrand.SurfaceVariant,
                        contentColor = ForgeBrand.TextPrimary
                    )
                ) {
                    Text("Create New Save", style = ForgeTypography.Button)
                }
            }
        }
    }
}
