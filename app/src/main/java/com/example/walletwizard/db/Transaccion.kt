package com.example.walletwizard.db

data class Transaccion(
    val transaccionId: Int,
    val nombre: String,
    val cuentaId: Int,
    val categoriaId: Int,
    //val fecha: String,
    val fecha: Long,
    val tipo: String,
    val importe: Double,
    val nota: String?,
    val valoracion: Int?
)
