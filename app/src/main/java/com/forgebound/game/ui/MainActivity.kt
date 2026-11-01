package com.forgebound.game.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.forgebound.game.R
import com.forgebound.ui.MainNavigator

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    color = Color(0xFF2A1E18),
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainNavigator()
                }
            }
        }
    }
}

@Composable
fun ForgeBoundPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF2A1E18), modifier = Modifier.fillMaxSize()) {
            MainNavigator()
        }
    }
}
