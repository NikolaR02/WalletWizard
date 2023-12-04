package com.example.walletwizard.db

import android.content.ContentValues
import android.content.Context

class FinanzasRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    // Métodos para la tabla CuentasFinancieras

    fun insertCuenta(cuenta: CuentaFinanciera): Long {
        val db = dbHelper.writableDatabase

        // Insertar la cuenta con saldo 0
        val cuentaId = db.insert(
            "CuentasFinancieras", null,
            ContentValues().apply {
                put("nombre_cuenta", cuenta.nombreCuenta)
                put("saldo", 0.0)
            }
        )

        val saldo = cuenta.saldo
        if (cuentaId != -1L && saldo != 0.0) {
            // Insertar la transacción asociada a la cuenta con el salario
            insertTransaccion(
                Transaccion(
                    0,
                    "Existencias",
                    cuentaId.toInt(),
                    4, // Ajusta esto según tu lógica
                    System.currentTimeMillis(), // Fecha actual
                    TipoTransaccion.INGRESO, // Se asume que es un ingreso
                    saldo,
                    "Dinero existente en la cuenta al insertarla en nuestra aplicación",
                    0 // Sin valoración
                )
            )
        }

        return cuentaId
    }


    fun actualizarCuenta(cuenta: CuentaFinanciera) {
        dbHelper.writableDatabase.update(
            "CuentasFinancieras",
            ContentValues().apply {
                put("nombre_cuenta", cuenta.nombreCuenta)
                put("saldo", cuenta.saldo)
            },
            "cuenta_id = ?", arrayOf(cuenta.cuentaId.toString())
        )
    }

    fun getAllCuentas(): List<CuentaFinanciera> = dbHelper.readableDatabase.query(
        "CuentasFinancieras",
        arrayOf("cuenta_id", "nombre_cuenta", "saldo"),
        null, null, null, null, "saldo desc"
    ).use { cursor ->
        generateSequence { if (cursor.moveToNext()) cursor else null }
            .map {
                CuentaFinanciera(
                    it.getInt(it.getColumnIndexOrThrow("cuenta_id")),
                    it.getString(it.getColumnIndexOrThrow("nombre_cuenta")),
                    it.getDouble(it.getColumnIndexOrThrow("saldo"))
                )
            }.toList()
    }

    fun getCuenta(cuentaId: Int): CuentaFinanciera? = dbHelper.readableDatabase.query(
        "CuentasFinancieras",
        arrayOf("cuenta_id", "nombre_cuenta", "saldo"),
        "cuenta_id = ?", arrayOf(cuentaId.toString()),
        null, null, null
    ).use { cursor ->
        if (cursor.moveToFirst()) {
            CuentaFinanciera(
                cursor.getInt(cursor.getColumnIndexOrThrow("cuenta_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("nombre_cuenta")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("saldo"))
            )
        } else {
            null
        }
    }

    // Función para calcular el nuevo saldo de la cuenta al insertar, actualizar o borrar una transacción
    private fun calcularNuevoSaldo(saldoActual: Double, diferenciaImporte: Double, tipoTransaccion: TipoTransaccion): Double =
        if (tipoTransaccion == TipoTransaccion.INGRESO) saldoActual + diferenciaImporte
        else saldoActual - diferenciaImporte

    fun deleteCuenta(cuentaId: Int): Int = dbHelper.writableDatabase.delete(
        "CuentasFinancieras", "cuenta_id = ?", arrayOf(cuentaId.toString())
    )

    // Métodos para la tabla Categorías

    fun insertCategoria(categoria: Categoria): Long = dbHelper.writableDatabase.insert(
        "Categorias", null,
        ContentValues().apply {
            put("nombre", categoria.nombre)
        }
    )

    fun getCategoria(categoriaId: Int): Categoria? = dbHelper.readableDatabase.query(
        "Categorias",
        arrayOf("categoria_id", "nombre"),
        "categoria_id = ?", arrayOf(categoriaId.toString()),
        null, null, null
    ).use { cursor ->
        if (cursor.moveToFirst()) {
            Categoria(
                cursor.getInt(cursor.getColumnIndexOrThrow("categoria_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            )
        } else {
            null
        }
    }

    fun getAllCategorias(): List<Categoria> = dbHelper.readableDatabase.query(
        "Categorias", arrayOf("categoria_id", "nombre"),
        null, null, null, null, null
    ).use { cursor ->
        generateSequence { if (cursor.moveToNext()) cursor else null }
            .map {
                Categoria(
                    it.getInt(it.getColumnIndexOrThrow("categoria_id")),
                    it.getString(it.getColumnIndexOrThrow("nombre"))
                )
            }.toList()
    }

    fun deleteCategoria(categoriaId: Int): Int = dbHelper.writableDatabase.delete(
        "Categorias", "categoria_id = ?", arrayOf(categoriaId.toString())
    )

    // Métodos para la tabla Transacciones

    fun insertTransaccion(transaccion: Transaccion): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("cuenta_id", transaccion.cuentaId)
            put("nombre", transaccion.nombre)
            put("categoria_id", transaccion.categoriaId)
            put("fecha", transaccion.fecha)
            put("tipo", transaccion.tipo.name)
            put("importe", transaccion.importe)
            put("nota", transaccion.nota)
            put("valoracion", transaccion.valoracion)
        }

        val transaccionId = db.insert("Transacciones", null, values)

        if (transaccionId != -1L) {
            getCuenta(transaccion.cuentaId)?.let { cuenta ->
                cuenta.saldo = calcularNuevoSaldo(cuenta.saldo, transaccion.importe, transaccion.tipo)
                actualizarCuenta(cuenta)
            }
        }
        return transaccionId
    }

    fun actualizarTransaccion(transaccion: Transaccion) {
        val db = dbHelper.writableDatabase

        // Obtener la información de la transacción antes de la actualización
        val transaccionAntigua = getTransaccion(transaccion.transaccionId)

        val values = ContentValues().apply {
            put("nombre", transaccion.nombre)
            put("cuenta_id", transaccion.cuentaId)
            put("categoria_id", transaccion.categoriaId)
            put("fecha", transaccion.fecha)
            put("tipo", transaccion.tipo.name)
            put("importe", transaccion.importe)
            put("nota", transaccion.nota)
            put("valoracion", transaccion.valoracion)
        }

        db.update("Transacciones", values, "transaccion_id = ?", arrayOf(transaccion.transaccionId.toString()))

        // Actualizar el saldo de la cuenta asociada
        val cuenta = getCuenta(transaccion.cuentaId)
        if (cuenta != null && transaccionAntigua != null) {

            // Revertir la transacción anterior antes de aplicar la nueva
            cuenta.saldo = calcularNuevoSaldo(cuenta.saldo, -transaccionAntigua.importe, transaccionAntigua.tipo)

            // Aplicar la nueva transacción
            cuenta.saldo = calcularNuevoSaldo(cuenta.saldo, transaccion.importe, transaccion.tipo)

            // Actualizar el saldo en la cuenta
            actualizarCuenta(cuenta)
        }
    }

    fun getTransaccion(transaccionId: Int): Transaccion? = dbHelper.readableDatabase.query(
        "Transacciones",
        arrayOf(
            "transaccion_id", "nombre", "cuenta_id", "categoria_id", "fecha",
            "tipo", "importe", "nota", "valoracion"
        ),
        "transaccion_id = ?", arrayOf(transaccionId.toString()),
        null, null, null
    ).use { cursor ->
        if (cursor.moveToFirst()) {
            Transaccion(
                cursor.getInt(cursor.getColumnIndexOrThrow("transaccion_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                cursor.getInt(cursor.getColumnIndexOrThrow("cuenta_id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("categoria_id")),
                cursor.getLong(cursor.getColumnIndexOrThrow("fecha")),
                TipoTransaccion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("tipo"))),
                cursor.getDouble(cursor.getColumnIndexOrThrow("importe")),
                cursor.getString(cursor.getColumnIndexOrThrow("nota")),
                cursor.getInt(cursor.getColumnIndexOrThrow("valoracion"))
            )
        } else {
            null
        }
    }

    fun getAllTransacciones(): List<Transaccion> = dbHelper.readableDatabase.query(
        "Transacciones",
        arrayOf(
            "transaccion_id", "nombre", "cuenta_id", "categoria_id", "fecha",
            "tipo", "importe", "nota", "valoracion"
        ),
        null, null, null, null, "fecha desc"
    ).use { cursor ->
        generateSequence { if (cursor.moveToNext()) cursor else null }
            .map {
                Transaccion(
                    it.getInt(it.getColumnIndexOrThrow("transaccion_id")),
                    it.getString(it.getColumnIndexOrThrow("nombre")),
                    it.getInt(it.getColumnIndexOrThrow("cuenta_id")),
                    it.getInt(it.getColumnIndexOrThrow("categoria_id")),
                    it.getLong(it.getColumnIndexOrThrow("fecha")),
                    TipoTransaccion.valueOf(it.getString(it.getColumnIndexOrThrow("tipo"))),
                    it.getDouble(it.getColumnIndexOrThrow("importe")),
                    it.getString(it.getColumnIndexOrThrow("nota")),
                    it.getInt(it.getColumnIndexOrThrow("valoracion"))
                )
            }.toList()
    }

    fun deleteTransaccion(transaccionId: Int): Int {
        // Obtener la información de la transacción antes de borrarla
        val transaccion = getTransaccion(transaccionId)

        // Borrar la transacción
        val deletedRows = dbHelper.writableDatabase.delete(
            "Transacciones", "transaccion_id = ?", arrayOf(transaccionId.toString())
        )

        // Restar el importe al saldo de la cuenta asociada
        if (deletedRows > 0 && transaccion != null) {
            getCuenta(transaccion.cuentaId)?.let { cuenta ->
                cuenta.saldo = calcularNuevoSaldo(cuenta.saldo, -transaccion.importe, transaccion.tipo)
                actualizarCuenta(cuenta)
            }
        }

        return deletedRows
    }
}