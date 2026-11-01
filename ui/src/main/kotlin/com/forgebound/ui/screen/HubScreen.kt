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
 * Smithy Hub (Section 6): central hub with bottom-bar navigation to all
 * Section-6 screens: Map/Battle, Forging, Inventory, Shops, Quest Log, etc.
 */
@Composable
fun HubScreen(
    onToBattle: () -> Unit,
    onToForging: () -> Unit,
    onToInventory: () -> Unit,
    onToShop: () -> Unit,
    onToQuestLog: () -> Unit,
    onToSettings: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    ForgeTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Smithy Hub", style = ForgeTypography.H2) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ForgeBrand.Surface,
                        contentColor = ForgeBrand.TextPrimary
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = ForgeBrand.SurfaceVariant,
                    selectedIndex = selectedIndex,
                    onDestinationSelected = { selectedIndex = it }
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                        label = { Text("Map") },
                        selected = selectedIndex == 0,
                        onClick = { selectedIndex = 0; onToBattle() }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Build, contentDescription = "Forge") },
                        label = { Text("Forge") },
                        selected = selectedIndex == 1,
                        onClick = { selectedIndex = 1; onToForging() }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Inventory, contentDescription = "Inventory") },
                        label = { Text("Inventory") },
                        selected = selectedIndex == 2,
                        onClick = { selectedIndex = 2; onToInventory() }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Shop, contentDescription = "Shops") },
                        label = { Text("Shops") },
                        selected = selectedIndex == 3,
                        onClick = { selectedIndex = 3; onToShop() }
                    )
                }
            },
            modifier = Modifier.fillMaxSize().background(ForgeBrand.Background)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome back to the Smithy, Kai.",
                    style = ForgeTypography.H2.copy(
                        color = ForgeBrand.TextPrimary
                    )
                )
                Text(
                    text = "All actions work fully offline — no network required.",
                    style = ForgeTypography.BodySmall.copy(
                        color = ForgeBrand.TextSecondary
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf(
                        Pair("Enter Battle / Zone 1", R.string.placeholder_battle),
                        Pair("Open Forge", R.string.placeholder_forge),
                        Pair("Inventory & Equipment", R.string.placeholder_inventory),
                        Pair("Shops (8 variants)", R.string.placeholder_shops),
                        Pair("Quest Log", R.string.placeholder_quests),
                        Pair("Allies & Party", R.string.placeholder_allies),
                    )) { (label, _) ->
                        Card(
                            modifier = Modifier.fillMaxWidth().height(96.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = ForgeBrand.SurfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = label, style = ForgeTypography.Body)
                            }
                        }
                    }
                }
            }
        }
    }
}
