package com.example.rideon.network

data class VerifyTokenRequest(
    val correo: String? = null,
    val token: String,
    val nuevaClave: String? = null
)
