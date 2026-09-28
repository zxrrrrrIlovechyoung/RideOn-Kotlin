package com.example.rideon.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rideon.R

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(modifier = modifier, containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("taller") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_tools),
                    contentDescription = "Taller"
                )
            },
            label = { Text("Taller", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("ruta") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_route),
                    contentDescription = "Ruta"
                )
            },
            label = { Text("Ruta", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("home") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = "Inicio"
                )
            },
            label = { Text("Inicio", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("stats") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_stats),
                    contentDescription = "Stats"
                )
            },
            label = { Text("Stats", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("yo") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "Yo"
                )
            },
            label = { Text("Yo", fontSize = 12.sp) }
        )
    }
}
