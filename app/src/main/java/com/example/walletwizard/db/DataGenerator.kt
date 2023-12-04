package com.example.walletwizard.db

import android.content.Context

// Una clase utilitaria con un método estático para insertar datos ficticios en las tablas.
object DataGenerator {

    fun insertDummyData(context: Context, deleteExistingData: Boolean) {
        val finanzasRepository = FinanzasRepository(context)

        if (deleteExistingData) {
            // Borrar datos existentes
            delete(context)
        }

        // Insertar categorías ficticias
        val categorias = listOf("Alimentación", "Entretenimiento", "Transporte", "Salud")
        for (nombreCategoria in categorias) {
            finanzasRepository.insertCategoria(Categoria(0, nombreCategoria))
        }

        // Insertar cuentas financieras ficticias
        val cuentas = listOf("Cuenta Bancaria A", "Cuenta Bancaria B", "Efectivo")
        for (nombreCuenta in cuentas) {
            finanzasRepository.insertCuenta(CuentaFinanciera(0, nombreCuenta, 0.0))
        }

        // Insertar transacciones ficticias
        for (i in 1..10) {
            val cuentaId = (1..cuentas.size).random()
            val categoriaId = (1..categorias.size).random()
            val nombre = "Transacción $i"
            val fecha = System.currentTimeMillis() // Fecha actual en formato timestamp
            val tipo = if (i % 2 == 0) TipoTransaccion.INGRESO else TipoTransaccion.GASTO
            val importe = (1..100).random().toDouble()
            val nota = "Nota de la transacción $i"
            val valoracion = if (tipo == TipoTransaccion.INGRESO) 0 else (1..5).random()

            val transaccion = Transaccion(0, nombre, cuentaId, categoriaId, fecha, tipo, importe, nota, valoracion)
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
