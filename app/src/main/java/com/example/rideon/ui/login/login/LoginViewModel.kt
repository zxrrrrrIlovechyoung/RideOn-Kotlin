package com.example.rideon.ui.login.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rideon.network.LoginRequest
import com.example.rideon.ui.login.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    var user: String = ""
    var password: String = ""

    fun login() {
        if (user.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(
                loading = false,
                success = false,
                error = "Por favor ingresa usuario y contraseña"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(loading = true)

            try {
                val response = RetrofitClient.api.login(
                    LoginRequest(
                        Correo = user,
                        Contrasena = password
                    )
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        _uiState.value = LoginUiState(
                            loading = false,
                            success = true,
                            nombre = body.nombre
                        )
                    } else {
                        _uiState.value = LoginUiState(
                            loading = false,
                            error = "Usuario no registrado o contraseña incorrecta"
                        )
                    }
                } else {
                    if (response.code() == 401) {
                        // Mensaje claro para credenciales inválidas
                        _uiState.value = LoginUiState(
                            loading = false,
                            error = "Usuario no registrado o contraseña incorrecta"
                        )
                    } else {
                        _uiState.value = LoginUiState(
                            loading = false,
                            error = "Error en login: ${response.code()}"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.value = LoginUiState(
                    loading = false,
                    error = "Error de conexión: ${e.localizedMessage}"
                )
            }
        }
    }
}
