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
fun ShopsScreen(onBack: () -> Unit) {
    ForgeTheme {
        Scaffold(
            topBar = {
                androidx.compose.material3.TopAppBar(
                    title = { Text("Shops", style = ForgeTypography.H2) },
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
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                items(listOf(
                    "General Goods", "Blacksmith Supplies", "Material Trader",
                    "Gem Cutter", "Alchemist", "Rare Imports", "Traveling Merchant", "Guild Vault"
                )) { name ->
                    Card(
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = ForgeBrand.SurfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text(text = name, style = ForgeTypography.Body)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Enter",
                                style = ForgeTypography.BodySmall.copy(
                                    color = ForgeBrand.EmberOrange
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
