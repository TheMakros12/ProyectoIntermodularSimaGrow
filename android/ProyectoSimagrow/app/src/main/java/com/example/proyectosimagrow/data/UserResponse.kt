package com.example.proyectosimagrow.data

data class UserResponse(
    val id: Int,
    val nombre: String,
    val apellidos: String?,
    val correo: String,
    var admin: Boolean = false,
    var nif: String,
    var creditos: Int,
    var password: String?
)