package com.example.punicbutton

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.simulateHotReload
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.punicbutton.screen.Dashboard
import com.example.punicbutton.screen.DataRekap
import com.example.punicbutton.screen.HomeScreen
import com.example.punicbutton.screen.LoginScreen
import com.example.punicbutton.screen.RegisterScreen
import com.example.punicbutton.ui.theme.PunicButtonTheme
import com.example.punicbutton.viewmodel.LoginViewModel

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
    val context = LocalContext.current
    val loginViewModel : LoginViewModel = viewModel()

    LaunchedEffect(Unit) {
        loginViewModel.checkUserLogin(context, navController)
    }

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController,) }
        composable("register") { RegisterScreen(navController = navController) }
        composable("admin"){ Dashboard(navController)}
        composable("data_rekap") { DataRekap()}
        composable("home/{nomorRumah}") { backStackEntry ->
            val nomorRumah = backStackEntry.arguments?.getString("nomorRumah")?.toIntOrNull() ?: 0
            HomeScreen(
                board = nomorRumah,
                snackbarHostState = remember { SnackbarHostState() },
                navController =navController
            )
        }

    }
}
