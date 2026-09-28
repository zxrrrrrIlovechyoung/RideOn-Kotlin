package com.example.rideon.ui.login.registro

data class RegisterUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
