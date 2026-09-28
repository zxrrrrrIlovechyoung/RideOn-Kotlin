package com.example.rideon.ui.login.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.rideon.components.InputField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "RideOn",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            "Más que una moto, un sentimiento",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(35.dp))
        InputField(
            label = "Correo",
            value = correo,
            onValueChange = {
                correo = it
                viewModel.user = it
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        InputField(
            label = "Contraseña",
            value = password,
            onValueChange = {
                password = it
                viewModel.password = it
            },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(4.dp))

        uiState.error?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = Color(0xFF48D8D5),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    navController.navigate("recoverPassword")
                }
        )


        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF7DF1EF),
                            Color(0xFF48D8D5)
                        )
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .clickable {
                    viewModel.login()
                },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))
        Row {
            Text("¿No tienes cuenta? ", color = Color.Gray)
            Text(
                "Crea tu cuenta aquí",
                color = Color(0xFF48D8D5),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), viewModel = LoginViewModel())
}
