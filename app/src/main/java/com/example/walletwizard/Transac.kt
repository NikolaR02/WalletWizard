package com.example.walletwizard

data class Transac(
    val nombre: String,
    val cantidad: Double,
    val dia: Byte,
    val mes: Byte) {
    fun getFecha(): String {
        return "$dia/$mes"
    }
}