package com.example.proyectosimagrow.data

data class IncidenciaRequest(
    val titulo: String,
    val descripcion: String,
    val usuarioId: Int,
    val espacioId: Int,
    val fechaIncidencia: String? = null
)
