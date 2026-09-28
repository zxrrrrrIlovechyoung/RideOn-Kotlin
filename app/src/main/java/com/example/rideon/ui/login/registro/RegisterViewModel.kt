package com.example.rideon.ui.login.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rideon.network.RegisterRequest
import com.example.rideon.ui.login.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.example.rideon.ui.login.registro.RegisterUiState


// Modelo para la UI


// Modelo para sexo (dropdown)
data class Sexo(val idSexo: Int, val strDescripcion: String)

class RegisterViewModel : ViewModel() {

    // Estado de la UI
    internal val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> get() = _uiState

    // Lista de sexos
    private val _sexos = MutableStateFlow<List<Sexo>>(emptyList())
    val sexos: StateFlow<List<Sexo>> get() = _sexos

    // Campos del formulario
    var nombre = ""
    var apPaterno = ""
    var apMaterno = ""
    var fechaNac = ""
    var sexoId: Int? = null
    var correo = ""
    var contrasena = ""

    init { cargarSexos() }

    private fun cargarSexos() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSexos()
                _sexos.value = if (response.isSuccessful) {
                    response.body()?.map { Sexo(it.idSexo, it.strDescripcion) } ?: emptyList()
                } else emptyList()
            } catch (e: Exception) {
                _sexos.value = emptyList()
            }
        }
    }

    fun register() {
        // Validar campos vacíos
        if (nombre.isBlank() || apPaterno.isBlank() || apMaterno.isBlank() ||
            fechaNac.isBlank() || sexoId == null || correo.isBlank() || contrasena.isBlank()
        ) {
            _uiState.value = RegisterUiState(
                loading = false, success = false,
                error = "Por favor llena todos los campos"
            )
            return
        }

        // Validar espacios en nombre y apellidos
        if (nombre.contains(" ") || apPaterno.contains(" ") || apMaterno.contains(" ")) {
            _uiState.value = RegisterUiState(
                loading = false, success = false,
                error = "Los nombres y apellidos no deben contener espacios"
            )
            return
        }

        val fechaFormateada = fechaNac

        viewModelScope.launch {
            _uiState.value = RegisterUiState(loading = true)
            try {
                val response = RetrofitClient.api.register(
                    RegisterRequest(
                        strNombre = nombre,
                        strApellidoPaterno = apPaterno,
                        strApellidoMaterno = apMaterno,
                        dtFechaNacimiento = fechaFormateada,
                        idSexo = sexoId!!,
                        strCorreo = correo.trim().lowercase(),
                        strContrasena = contrasena,
                        idTipoUsuario = 2
                    )
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    _uiState.value = if (body?.success == true) {
                        RegisterUiState(loading = false, success = true, message = body.message)
                    } else {
                        RegisterUiState(loading = false, success = false, error = body?.message ?: "Error desconocido")
                    }
                } else {
                    val errorMsg = parseErrorMessage(response.errorBody()?.string(), response.code())
                    _uiState.value = RegisterUiState(loading = false, success = false, error = errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState(
                    loading = false,
                    success = false,
                    error = "Error de conexión: ${e.localizedMessage}"
                )
            }
        }
    }

    private fun parseErrorMessage(errorBody: String?, statusCode: Int): String {
        return try {
            if (!errorBody.isNullOrEmpty()) {
                val json = JSONObject(errorBody)
                if (statusCode == 400 && json.has("errors")) {
                    val errors = json.getJSONObject("errors")
                    if (errors.keys().asSequence().any { it.equals("strCorreo", true) }) {
                        return "Correo ya registrado, usa otro"
                    }
                    val mensajes = mutableListOf<String>()
                    errors.keys().forEach { key ->
                        val arr = errors.getJSONArray(key)
                        for (i in 0 until arr.length()) { mensajes.add(arr.getString(i)) }
                    }
                    return mensajes.joinToString("\n")
                } else if (json.has("title")) {
                    return json.getString("title")
                } else return "Correo ya registrado, usa otro"
            } else return "Correo ya registrado, usa otro"
        } catch (e: Exception) {
            "Correo ya registrado, usa otro"
        }
    }
}
