package com.example.walletwizard.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class FinanzasRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertCuenta(cuenta: CuentaFinanciera): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre_cuenta", cuenta.nombreCuenta)
        }
        return db.insert("CuentasFinancieras", null, values)
    }

    fun getAllCuentas(): List<CuentaFinanciera> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "CuentasFinancieras",
            arrayOf("cuenta_id", "nombre_cuenta"),
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
                cuentas.add(CuentaFinanciera(cuentaId, nombreCuenta))
            }
        }

        cursor.close()

        return cuentas
    }

    // Puedes implementar métodos similares para las otras operaciones CRUD y para las otras tablas
}
