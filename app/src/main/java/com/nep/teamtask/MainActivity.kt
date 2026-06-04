package com.nep.teamtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nep.teamtask.ui.navigation.TeamTaskNavHost
import com.nep.teamtask.ui.screen.HomeScreen
import com.nep.teamtask.ui.theme.TeamTaskAppTheme
import com.nep.teamtask.ui.theme.TeamTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamTaskApp()
        }
    }
}

@Composable
fun TeamTaskApp() {
    TeamTaskAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TeamTaskNavHost()
        }
    }
}