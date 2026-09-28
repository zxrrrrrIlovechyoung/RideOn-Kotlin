package com.example.rideon.network

data class ResetPasswordRequest(
    val Correo: String,
    val Token: String,
    val NuevaContrasena: String
)
