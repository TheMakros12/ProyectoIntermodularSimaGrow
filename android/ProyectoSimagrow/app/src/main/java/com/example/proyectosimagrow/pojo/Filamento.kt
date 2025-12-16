package com.example.proyectosimagrow.pojo

data class Filamento(val nombre: String, val descripcion: String, val precio: Int, val categoria: CategoriaFilamento) {

    class DatosFilamentos {
        companion object {
            val FILAMENTOS = arrayOf(
                Filamento("PLA Normal", "Biodegradable, fácil y barato.", 3, CategoriaFilamento.ESTANDAR),
                Filamento("PLA Reciclado", "Hecho con material reutilizado.", 4, CategoriaFilamento.ESTANDAR),
                Filamento("PLA Madera", "Mezcla de PLA + polvo de madera.", 6, CategoriaFilamento.PREMIUM),
                Filamento("PLA Mármol", "Acabado tipo piedra.", 7, CategoriaFilamento.PREMIUM),
                Filamento("PLA Seda", "Brillante, colores metálicos.", 8, CategoriaFilamento.PREMIUM),
                Filamento("PETG Eco", "Más resistente, sostenible.", 9, CategoriaFilamento.PREMIUM),
                Filamento("PLA Arcoíris", "Cambia de color.", 12, CategoriaFilamento.EXCLUSIVO),
                Filamento("PLA Fosforescente", "Brilla en la oscuridad.", 12, CategoriaFilamento.EXCLUSIVO),
                Filamento("TPU Flexible", "Goma elástica.", 15, CategoriaFilamento.EXCLUSIVO),
            )
        }
    }
}