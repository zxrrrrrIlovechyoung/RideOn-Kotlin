package com.example.rideon.ui.login.registro

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.rideon.components.InputField
import com.example.rideon.components.PrimaryButton
import com.example.rideon.theme.RideOnTheme
import kotlin.math.min
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var apPaterno by remember { mutableStateOf("") }
    var apMaterno by remember { mutableStateOf("") }
    var fechaNac by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasenaConfirm by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showPasswordConfirm by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val sexos by viewModel.sexos.collectAsState(initial = emptyList())
    var expanded by remember { mutableStateOf(false) }
    var selectedSexo by remember { mutableStateOf<Sexo?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState() // Scroll para toda la columna

    fun passwordStrength(password: String): Pair<Float, String> {
        var score = 0
        if (password.length >= 6) score++
        if (password.length >= 10) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++

        val strength = min(score, 6)
        val progress = strength / 6f
        val label = when(strength) {
            0,1 -> "Muy débil"
            2 -> "Débil"
            3,4 -> "Media"
            5 -> "Fuerte"
            6 -> "Muy fuerte"
            else -> "Débil"
        }
        return progress to label
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // <-- Permite scroll
            .imePadding()               // <-- Ajusta cuando se abre teclado
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Top, // Para que los campos inferiores suban
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("RideOn", style = MaterialTheme.typography.headlineLarge)
        Text("Bienvenido", fontSize = 20.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            label = "Tu nombre/Nickname",
            value = nombre,
            onValueChange = { nombre = it; viewModel.nombre = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Apellido Paterno",
            value = apPaterno,
            onValueChange = { apPaterno = it; viewModel.apPaterno = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Apellido Materno",
            value = apMaterno,
            onValueChange = { apMaterno = it; viewModel.apMaterno = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val mes = (month + 1).toString().padStart(2, '0')
                        val dia = dayOfMonth.toString().padStart(2, '0')
                        fechaNac = "$year-$mes-$dia"
                        viewModel.fechaNac = fechaNac
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (fechaNac.isEmpty()) "Selecciona tu fecha de nacimiento" else fechaNac) }
        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedSexo?.strDescripcion ?: "",
                onValueChange = {},
                label = { Text("Sexo") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sexos.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.strDescripcion) },
                        onClick = {
                            selectedSexo = s
                            viewModel.sexoId = s.idSexo
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Correo",
            value = correo,
            onValueChange = { correo = it; viewModel.correo = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Contraseña",
            value = contrasena,
            onValueChange = { contrasena = it; viewModel.contrasena = it },
            isPassword = !showPassword,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar/Ocultar"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        val (strengthProgress, strengthLabel) = passwordStrength(contrasena)
        LinearProgressIndicator(
            progress = strengthProgress,
            color = when(strengthProgress) {
                in 0f..0.33f -> Color.Red
                in 0.34f..0.66f -> Color.Yellow
                else -> Color.Green
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Text(strengthLabel, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        InputField(
            label = "Confirma tu contraseña",
            value = contrasenaConfirm,
            onValueChange = { contrasenaConfirm = it },
            isPassword = !showPasswordConfirm,
            trailingIcon = {
                IconButton(onClick = { showPasswordConfirm = !showPasswordConfirm }) {
                    Icon(
                        imageVector = if (showPasswordConfirm) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar/Ocultar"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButton(
            text = if (uiState.loading) "Creando..." else "Crear Cuenta",
            onClick = {
                if (contrasena != contrasenaConfirm) {
                    viewModel._uiState.value = RegisterUiState(
                        loading = false,
                        success = false,
                        error = "Las contraseñas no coinciden"
                    )
                } else {
                    viewModel.register()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        uiState.error?.let {
            Text(it, color = Color.Red, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RideOnTheme {
        RegisterScreen(navController = rememberNavController())
    }
}
