package com.forgebound.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forgebound.ui.theme.ForgeBrand
import com.forgebound.ui.theme.ForgeTheme
import com.forgebound.ui.theme.ForgeTypography

/**
 * Settings screen (Section 6 / M17): audio, graphics, language, haptics,
 * reduce-motion, text size, backup export/import, credits, privacy note.
 * Offline — no data collected, no INTERNET permission.
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    ForgeTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings", style = ForgeTypography.H2) },
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "General",
                    style = ForgeTypography.H2.copy(
                        color = ForgeBrand.TextSecondary
                    )
                )
                ListItem(
                    text = { Text("Language: English / Persian") },
                    leadingIcon = { Icon(Icons.Default.Language, contentDescription = "Language") },
                    colors = ListItemDefaults.listItemColors(
                        containerColor = ForgeBrand.SurfaceVariant
                    )
                )
                ListItem(
                    text = { Text("Haptics: On") },
                    leadingIcon = { Icon(Icons.Default.Vibration, contentDescription = "Haptics") },
                    colors = ListItemDefaults.listItemColors(
                        containerColor = ForgeBrand.SurfaceVariant
                    )
                )
                ListItem(
                    text = { Text("Reduce Motion: Off") },
                    leadingIcon = { Icon(Icons.Default.Remove, contentDescription = "Reduce Motion") },
                    colors = ListItemDefaults.listItemColors(
                        containerColor = ForgeBrand.SurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Data & Privacy",
                    style = ForgeTypography.H2.copy(
                        color = ForgeBrand.TextSecondary
                    )
                )
                ListItem(
                    text = { Text("Backup Export (local file)") },
                    leadingIcon = { Icon(Icons.Default.Backup, contentDescription = "Backup") },
                    colors = ListItemDefaults.listItemColors(
                        containerColor = ForgeBrand.SurfaceVariant
                    )
                )
                ListItem(
                    text = { Text("Backup Import (local file)") },
                    leadingIcon = { Icon(Icons.Default.Restore, contentDescription = "Restore") },
                    colors = ListItemDefaults.listItemColors(
                        containerColor = ForgeBrand.SurfaceVariant
                    )
                )
                ListItem(
                    text = { Text("Credits") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Credits") },
                    colors = ListItemDefaults.listItemColors(
                        containerColor = ForgeBrand.SurfaceVariant
                    )
                )
                ListItem(
                    text = { Text("Privacy note — offline, no data collected") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Privacy") },
                    colors = ListItemDefaults.listItemColors(
                        containerColor = ForgeBrand.SurfaceVariant
                    )
                )
            }
        }
    }
}
