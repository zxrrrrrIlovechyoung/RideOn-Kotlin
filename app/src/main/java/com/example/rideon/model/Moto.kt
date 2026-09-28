package com.example.rideon.model

data class Moto(
    val nombre: String,
    val imagen: Int,
    val taller: List<String>,
    val ruta: List<String>,
    val estadisticas: List<String>,
    val gps: List<String>
)
