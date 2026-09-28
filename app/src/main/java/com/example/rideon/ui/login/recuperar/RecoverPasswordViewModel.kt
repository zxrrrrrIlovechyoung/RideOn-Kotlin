package com.example.rideon.ui.login.recuperar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rideon.ui.login.UsuariosApiService
import com.example.rideon.network.ForgotPasswordRequest
import com.example.rideon.network.ResetPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Estado UI
data class RecoverUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class RecoverPasswordViewModel(private val api: UsuariosApiService) : ViewModel() {

    var correo: String = ""
    var token: String = ""
    var nuevaClave: String = ""

    private val _uiStateSendCode = MutableStateFlow(RecoverUiState())
    val uiStateSendCode: StateFlow<RecoverUiState> get() = _uiStateSendCode

    private val _uiStateUpdatePass = MutableStateFlow(RecoverUiState())
    val uiStateUpdatePass: StateFlow<RecoverUiState> get() = _uiStateUpdatePass

    fun enviarCodigo() {
        if (correo.isBlank()) {
            _uiStateSendCode.value = RecoverUiState(error = "Ingresa un correo válido")
            return
        }
        viewModelScope.launch {
            _uiStateSendCode.value = RecoverUiState(loading = true)
            try {
                val response = api.forgotPassword(ForgotPasswordRequest(correo))
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiStateSendCode.value = RecoverUiState(success = true)
                } else {
                    _uiStateSendCode.value = RecoverUiState(
                        error = response.body()?.message ?: "Error desconocido"
                    )
                }
            } catch (e: IOException) {
                _uiStateSendCode.value = RecoverUiState(error = "Error de red")
            } catch (e: HttpException) {
                _uiStateSendCode.value = RecoverUiState(error = "Error de servidor")
            }
        }
    }

    fun cambiarPassword() {
        if (token.isBlank() || nuevaClave.isBlank()) {
            _uiStateUpdatePass.value = RecoverUiState(error = "Llena todos los campos")
            return
        }
        viewModelScope.launch {
            _uiStateUpdatePass.value = RecoverUiState(loading = true)
            try {
                val response = api.resetPassword(
                    ResetPasswordRequest(
                        Correo = correo,
                        Token = token,
                        NuevaContrasena = nuevaClave
                    )
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiStateUpdatePass.value = RecoverUiState(success = true)
                } else {
                    _uiStateUpdatePass.value = RecoverUiState(
                        error = response.body()?.message ?: "Error desconocido"
                    )
                }
            } catch (e: IOException) {
                _uiStateUpdatePass.value = RecoverUiState(error = "Error de red")
            } catch (e: HttpException) {
                _uiStateUpdatePass.value = RecoverUiState(error = "Error de servidor")
            }
        }
    }
}

// ViewModelFactory para pasar UsuariosApiService
class RecoverPasswordViewModelFactory(private val api: UsuariosApiService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecoverPasswordViewModel::class.java)) {
            return RecoverPasswordViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
