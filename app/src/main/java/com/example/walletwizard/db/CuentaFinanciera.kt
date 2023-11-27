package com.example.walletwizard.db

data class CuentaFinanciera(
    val cuentaId: Int,
    val nombreCuenta: String,
    var saldo: Double
)
