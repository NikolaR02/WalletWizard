package com.example.walletwizard.db

data class Transaccion(
    val transaccionId: Int,
    val cuentaId: Int,
    val categoriaId: Int,
    val fecha: String,
    val tipo: String,
    val importe: Double,
    val nota: String?,
    val valoracion: Int?
)
