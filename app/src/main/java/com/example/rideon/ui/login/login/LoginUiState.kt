package com.example.rideon.ui.login.login

data class LoginUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val nombre: String? = null
)
