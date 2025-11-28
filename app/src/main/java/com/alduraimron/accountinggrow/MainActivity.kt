package com.alduraimron.accountinggrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.alduraimron.accountinggrow.presentation.navigation.NavGraph
import com.alduraimron.accountinggrow.ui.theme.AccountingGrowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AccountingGrowTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}