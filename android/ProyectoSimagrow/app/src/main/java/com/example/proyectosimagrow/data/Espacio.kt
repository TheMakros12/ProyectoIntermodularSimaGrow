package com.example.proyectosimagrow.data

data class Espacio(
    val id: Int,
    val ubicacion: String,
    val planta: String
) {
    override fun toString(): String {
        return "$ubicacion - Planta $planta"
    }
}
