package com.example.rideon.ui.login.recuperar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.rideon.components.InputField
import com.example.rideon.components.PrimaryButton
import com.example.rideon.ui.login.UsuariosApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RecoverPasswordScreen(
    navController: NavController,
    viewModel: RecoverPasswordViewModel
) {
    var correo by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var nuevaClave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var tiempoRestante by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()
    val uiStateSend by viewModel.uiStateSendCode.collectAsState()
    val uiStateUpdate by viewModel.uiStateUpdatePass.collectAsState()

    // Navegar al login si contraseña cambiada
    LaunchedEffect(uiStateUpdate.success) {
        if (uiStateUpdate.success) {
            navController.navigate("login") {
                popUpTo("recoverPassword") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo y eslogan
        Text("RideOn", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("Más que una moto, un sentimiento", fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(35.dp))

        // Input correo
        InputField(
            label = "Correo",
            value = correo,
            onValueChange = {
                correo = it
                viewModel.correo = it
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botón enviar código
        PrimaryButton(
            text = if (tiempoRestante > 0) "Reenviar código ($tiempoRestante s)" else "Enviar código",
            onClick = {
                if (tiempoRestante <= 0) {
                    viewModel.enviarCodigo()
                    tiempoRestante = 600
                    scope.launch {
                        while (tiempoRestante > 0) {
                            delay(1000L)
                            tiempoRestante -= 1
                        }
                    }
                }
            },
            enabled = tiempoRestante <= 0
        )

        // Mensajes de error / loading
        uiStateSend.error?.let { Text(it, color = Color.Red) }
        if (uiStateSend.loading) CircularProgressIndicator()

        Spacer(modifier = Modifier.height(20.dp))

        // Mostrar inputs si código enviado
        if (tiempoRestante > 0) {
            InputField(
                label = "Código de verificación",
                value = token,
                onValueChange = { token = it; viewModel.token = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                label = "Nueva contraseña",
                value = nuevaClave,
                onValueChange = { nuevaClave = it; viewModel.nuevaClave = it },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                label = "Confirmar contraseña",
                value = confirmarClave,
                onValueChange = { confirmarClave = it },
                isPassword = true
            )

            if (confirmarClave.isNotBlank() && confirmarClave != nuevaClave) {
                Text("Las contraseñas no coinciden", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                text = if (uiStateUpdate.loading) "Cambiando..." else "Cambiar contraseña",
                onClick = { viewModel.cambiarPassword() },
                enabled = tiempoRestante > 0 && nuevaClave == confirmarClave && token.isNotBlank()
            )

            uiStateUpdate.error?.let { Text(it, color = Color.Red) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRecoverPasswordScreen() {
    val dummyApi: UsuariosApiService = TODO() // Solo preview
    val viewModel = RecoverPasswordViewModel(dummyApi)
    RecoverPasswordScreen(navController = rememberNavController(), viewModel = viewModel)
}
