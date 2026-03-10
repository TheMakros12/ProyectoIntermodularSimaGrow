package com.example.proyectosimagrow.data

data class RecompensaResponse(
    val id: Int,
    val nombre: String,
    val tokens: Double,
    val tipo: String,
    val imagen: String? = null
)
