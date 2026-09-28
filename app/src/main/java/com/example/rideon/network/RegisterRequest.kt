package com.example.rideon.network

data class RegisterRequest(
    val strNombre: String,
    val strApellidoPaterno: String,
    val strApellidoMaterno: String,
    val dtFechaNacimiento: String,
    val idSexo: Int,
    val strCorreo: String,
    val strContrasena: String,
    val idTipoUsuario: Int
)
