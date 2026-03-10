package com.example.proyectosimagrow.data

data class UsuarioRequest(
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val nif: String,
    val creditos: Int,
    val fechaNacimiento: String? = null
)
