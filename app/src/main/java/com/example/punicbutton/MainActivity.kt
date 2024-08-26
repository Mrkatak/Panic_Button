package com.example.punicbutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.punicbutton.screen.HomeScreenContent
import com.example.punicbutton.screen.LoginScreen
import com.example.punicbutton.screen.RegisterScreen
import com.example.punicbutton.ui.theme.PunicButtonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PunicButtonTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController,) }
        composable("register") { RegisterScreen(navController = navController) }
        composable("home/{nomorRumah}") { backStackEntry ->
            val nomorRumah = backStackEntry.arguments?.getString("nomorRumah")?.toIntOrNull() ?: 0
            HomeScreenContent(
                board = nomorRumah,
                snackbarHostState = remember { SnackbarHostState() })
        }
    }
}
