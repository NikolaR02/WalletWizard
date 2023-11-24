package com.example.walletwizard.db

import android.content.ContentValues
import android.content.Context

class FinanzasRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    // Métodos para la tabla CuentasFinancieras

    fun insertCuenta(cuenta: CuentaFinanciera): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre_cuenta", cuenta.nombreCuenta)
            put("saldo", cuenta.saldo)
        }

        return db.insert("CuentasFinancieras", null, values)
    }

    fun actualizarSaldoCuenta(cuentaId: Int, nuevoSaldo: Double) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("saldo", nuevoSaldo)
        }

        db.update("CuentasFinancieras", values, "cuenta_id = ?", arrayOf(cuentaId.toString()))
    }

    fun getAllCuentas(): List<CuentaFinanciera> {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "CuentasFinancieras",
            arrayOf("cuenta_id", "nombre_cuenta", "saldo"),
            null,
            null,
            null,
            null,
            null
        )

        val cuentas = mutableListOf<CuentaFinanciera>()

        with(cursor) {
            while (moveToNext()) {
                val cuentaId = getInt(getColumnIndexOrThrow("cuenta_id"))
                val nombreCuenta = getString(getColumnIndexOrThrow("nombre_cuenta"))
                val saldo = getDouble(getColumnIndexOrThrow("saldo"))

                cuentas.add(CuentaFinanciera(cuentaId, nombreCuenta, saldo))
            }
        }
        cursor.close()

        return cuentas
    }

    fun getSaldoCuenta(cuentaId: Int): Double {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "CuentasFinancieras",
            arrayOf("saldo"),
            "cuenta_id = ?",
            arrayOf(cuentaId.toString()),
            null,
            null,
            null
        )

        val saldo = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()

        return saldo
    }

    fun deleteCuenta(cuentaId: Int): Int {
        val db = dbHelper.writableDatabase

        return db.delete("CuentasFinancieras", "cuenta_id = ?", arrayOf(cuentaId.toString()))
    }

    // Métodos para la tabla Categorías

    fun insertCategoria(categoria: Categoria): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombre", categoria.nombre)
        }

        return db.insert("Categorias", null, values)
    }

    fun getAllCategorias(): List<Categoria> {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "Categorias",
            arrayOf("categoria_id", "nombre"),
            null,
            null,
            null,
            null,
            null
        )

        val categorias = mutableListOf<Categoria>()

        with(cursor) {
            while (moveToNext()) {
                val categoriaId = getInt(getColumnIndexOrThrow("categoria_id"))
                val nombreCategoria = getString(getColumnIndexOrThrow("nombre"))

                categorias.add(Categoria(categoriaId, nombreCategoria))
            }
        }
        cursor.close()

        return categorias
    }

    fun deleteCategoria(categoriaId: Int): Int {
        val db = dbHelper.writableDatabase

        return db.delete("Categorias", "categoria_id = ?", arrayOf(categoriaId.toString()))
    }

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
            val nuevoSaldo = getSaldoCuenta(transaccion.cuentaId) + transaccion.importe
            actualizarSaldoCuenta(transaccion.cuentaId, nuevoSaldo)
        }

        return transaccionId
    }

    fun getAllTransacciones(): List<Transaccion> {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "Transacciones",
            arrayOf("transaccion_id", "nombre", "cuenta_id", "categoria_id", "fecha", "tipo", "importe", "nota", "valoracion"),
            null,
            null,
            null,
            null,
            null
        )

        val transacciones = mutableListOf<Transaccion>()

        with(cursor) {
            while (moveToNext()) {
                val transaccionId = getInt(getColumnIndexOrThrow("transaccion_id"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))
                val cuentaId = getInt(getColumnIndexOrThrow("cuenta_id"))
                val categoriaId = getInt(getColumnIndexOrThrow("categoria_id"))
                val fecha = getLong(getColumnIndexOrThrow("fecha"))
                val tipo = TipoTransaccion.valueOf(getString(getColumnIndexOrThrow("tipo")))
                val importe = getDouble(getColumnIndexOrThrow("importe"))
                val nota = getString(getColumnIndexOrThrow("nota"))
                val valoracion = getInt(getColumnIndexOrThrow("valoracion"))

                transacciones.add(
                    Transaccion(
                        transaccionId,
                        nombre,
                        cuentaId,
                        categoriaId,
                        fecha,
                        tipo,
                        importe,
                        nota,
                        valoracion
                    )
                )
            }
        }
        cursor.close()

        return transacciones
    }

    fun deleteTransaccion(transaccionId: Int): Int {
        val db = dbHelper.writableDatabase

        return db.delete("Transacciones", "transaccion_id = ?", arrayOf(transaccionId.toString()))
    }
}
