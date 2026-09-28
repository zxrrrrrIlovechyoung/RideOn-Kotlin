package com.example.rideon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rideon.ui.login.home.HomeScreen
import com.example.rideon.ui.login.login.LoginScreen
import com.example.rideon.ui.login.login.LoginViewModel
import com.example.rideon.ui.login.recuperar.RecoverPasswordScreen
import com.example.rideon.ui.login.recuperar.RecoverPasswordViewModel
import com.example.rideon.ui.login.recuperar.RecoverPasswordViewModelFactory
import com.example.rideon.ui.login.registro.RegisterScreen
import com.example.rideon.theme.RideOnTheme
import com.example.rideon.ui.login.RetrofitClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RideOnTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    RideOnApp()
                }
            }
        }
    }
}

@Composable
fun RideOnApp() {
    val navController = rememberNavController()
    val loginViewModel = viewModel<LoginViewModel>()

    // Crear ViewModel para RecoverPassword usando RetrofitClient.api
    val recoverViewModel: RecoverPasswordViewModel = viewModel(
        factory = RecoverPasswordViewModelFactory(RetrofitClient.api)
    )

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("recoverPassword") {
            RecoverPasswordScreen(
                navController = navController,
                viewModel = recoverViewModel
            )
        }
    }
}
