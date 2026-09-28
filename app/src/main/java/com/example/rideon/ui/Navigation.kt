package com.example.rideon.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rideon.screens.*
import com.example.rideon.ui.login.home.HomeScreen

@Composable
fun RideOnApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("stats") { StatsScreen(navController) }
        composable("taller") { TallerScreen(navController) }
        composable("ruta") { RutaScreen(navController) }
        composable("yo") { YoScreen(navController) }
    }
}
