package com.example.walletwizard.db

import android.content.Context
import java.util.concurrent.TimeUnit

// Una clase utilitaria con un método estático para insertar datos ficticios en las tablas.
object DataGenerator {

    fun insertDummyData(context: Context, deleteExistingData: Boolean) {
        val finanzasRepository = FinanzasRepository(context)

        if (deleteExistingData) {
            // Borrar datos existentes
            delete(context)
        }

        // Insertar categorías ficticias
        val categorias = listOf("Alimentación", "Entretenimiento", "Transporte", "Salud", "Otros")
        for (nombreCategoria in categorias) {
            finanzasRepository.insertCategoria(Categoria(0, nombreCategoria))
        }

        // Insertar cuentas financieras ficticias
        val cuentas = listOf("Cuenta Bancaria A", "Cuenta Bancaria B", "Efectivo")
        for (nombreCuenta in cuentas) {
            finanzasRepository.insertCuenta(CuentaFinanciera(0, nombreCuenta, 0.0))
        }

        for (i in 1..20) {
            val cuentaId = (1..cuentas.size).random()
            val categoriaId = (1..categorias.size).random()
            val nombre = "Transacción $i"

            // Generar fecha aleatoria en los últimos 3 años
            val randomDateMillis = System.currentTimeMillis() - (0..(TimeUnit.DAYS.toMillis(365 * 3))).random()

            val tipo = if (i % 2 == 0) TipoTransaccion.INGRESO else TipoTransaccion.GASTO

            // Generar importe con 2 decimales
            val importe = String.format("%.2f", (1..100).random() + Math.random()).replace(",", ".").toDouble()

            val nota = "Nota de la transacción $i"
            val valoracion = if (tipo == TipoTransaccion.INGRESO) 0 else (1..5).random()

            val transaccion = Transaccion(0, nombre, cuentaId, categoriaId, randomDateMillis, tipo, importe, nota, valoracion)
            finanzasRepository.insertTransaccion(transaccion)
        }
    }

    fun delete(context: Context) {
        val finanzasRepository = FinanzasRepository(context)

        finanzasRepository.getAllCuentas().forEach { cuenta ->
            finanzasRepository.deleteCuenta(cuenta.cuentaId)
        }

        finanzasRepository.getAllCategorias().forEach { categoria ->
            finanzasRepository.deleteCategoria(categoria.categoriaId)
        }

        finanzasRepository.getAllTransacciones().forEach { transaccion ->
            finanzasRepository.deleteTransaccion(transaccion.transaccionId)
        }
    }
}
