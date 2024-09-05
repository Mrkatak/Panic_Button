package com.example.punicbutton

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.punicbutton.screen.Dashboard
import com.example.punicbutton.screen.DataRekap
import com.example.punicbutton.screen.DetailLogScreen
import com.example.punicbutton.screen.HomeScreen
import com.example.punicbutton.screen.LoginScreen
import com.example.punicbutton.screen.MainFunction
import com.example.punicbutton.screen.RegisterScreen
import com.example.punicbutton.ui.theme.PunicButtonTheme
import com.example.punicbutton.viewmodel.ViewModel

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
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    val isOnboardingShown = sharedPreferences.getBoolean("OnBoardingShown", false)
    val viewModel : ViewModel = viewModel()

    LaunchedEffect(Unit) {
        if (!isOnboardingShown) {
            navController.navigate("onboarding")
        } else {
            viewModel.checkUserLogin(context, navController)
        }
    }

    NavHost(navController, startDestination = if (isOnboardingShown)"login" else "onboarding") {
        composable("onboarding"){
            MainFunction(navController = navController)
            LaunchedEffect(Unit) {
                sharedPreferences.edit().putBoolean("OnBoardingShown", true).apply()
            }
        }
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(navController = navController) }
        composable("admin"){ Dashboard(navController)}
        composable("data_rekap") { DataRekap(context, viewModel)}
        composable("home/{nomorRumah}") { backStackEntry ->
            val nomorRumah = backStackEntry.arguments?.getString("nomorRumah")?.toIntOrNull() ?: 0
            HomeScreen(
                board = nomorRumah,
                snackbarHostState = remember { SnackbarHostState() },
                navController =navController
            )
        }
        composable("detail_log/{nomorRumah}") { backStackEntry ->
            val nomorRumah = backStackEntry.arguments?.getString("nomorRumah") ?: 0
            DetailLogScreen(nomorRumah = nomorRumah.toString(), context)
        }
    }
}
