package com.example.proyectosimagrow.data

data class Incidencia(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val resuelta: Boolean,
    val usuarioId: Int,
    val usuarioNombre: String?,
    val usuarioApellidos: String?,
    val espacioUbicacion: String?,
    val fechaIncidencia: String?
)
