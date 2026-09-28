package com.example.rideon.ui.login.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.rideon.R
import com.example.rideon.model.Moto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val motos = listOf(
        Moto(
            nombre = "Mi 250z",
            imagen = R.drawable.motorcycle_250z,
            taller = listOf("Último Servicio: 10000km", "Próximo Servicio: 15000km"),
            ruta = listOf("Tiempo: 20min", "Distancia: 10km", "Top speed: 130km/h"),
            estadisticas = listOf("Odómetro: 15000km", "Top Speed: 130km/h", "Servicios: 3"),
            gps = listOf("No Activo")
        ),
        Moto(
            nombre = "Mi Vortex 250",
            imagen = R.drawable.motorcycle_150cc,
            taller = listOf("Último Servicio: 5000km", "Próximo Servicio: 8000km"),
            ruta = listOf("Tiempo: 15min", "Distancia: 7km", "Top speed: 120km/h"),
            estadisticas = listOf("Odómetro: 8000km", "Top Speed: 120km/h", "Servicios: 2"),
            gps = listOf("Activo")
        )
    )

    val selectedMotoIndex = remember { mutableStateOf(0) }
    val selectedMoto = motos[selectedMotoIndex.value]

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { Text("Menú lateral", modifier = Modifier.padding(24.dp)) }
    ) {
        Scaffold(bottomBar = { /* Aquí tu BottomNavigationBar si lo tienes */ }) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        itemsIndexed(motos) { index, moto ->
                            MotoCard(title = moto.nombre, data = moto.taller) { selectedMotoIndex.value = index }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    MotoInfoSection(selectedMoto)
                }
            }
        }
    }
}

@Composable
fun MotoCard(title: String, data: List<String>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            data.forEach { Text(it, fontSize = 14.sp, color = Color.DarkGray) }
        }
    }
}

@Composable
fun MotoInfoSection(moto: Moto) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InfoCard("Taller", moto.taller)
        InfoCard("Ruta", moto.ruta)
        InfoCard("Estadísticas", moto.estadisticas)
        InfoCard("GPS", moto.gps)
    }
}

@Composable
fun InfoCard(title: String, data: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            data.forEach { Text(it, fontSize = 14.sp, color = Color.DarkGray) }
        }
    }
}
