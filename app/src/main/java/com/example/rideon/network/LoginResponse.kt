package com.example.rideon.network

data class LoginResponse(
    val success: Boolean,
    val usuarioId: Int?,
    val nombre: String?
)
