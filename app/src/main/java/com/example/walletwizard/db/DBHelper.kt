package com.example.walletwizard.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "finanzas.db"
        const val DATABASE_VERSION = 1
    }

    // Definir la tabla Cuentas Financieras
    private val createCuentasTable  = """
        CREATE TABLE CuentasFinancieras (
            cuenta_id INTEGER PRIMARY KEY,
            nombre_cuenta TEXT
        )
    """.trimIndent()

    // Definir la tabla Transacciones
    private val createTransaccionesTable  = """
        CREATE TABLE Transacciones (
            transaccion_id INTEGER PRIMARY KEY,
            cuenta_id INTEGER,
            categoria_id INTEGER,
            fecha TEXT,
            tipo TEXT,
            importe REAL,
            nota TEXT,
            valoracion INTEGER,
            FOREIGN KEY (cuenta_id) REFERENCES CuentasFinancieras(cuenta_id),
            FOREIGN KEY (categoria_id) REFERENCES Categorias(categoria_id)
        )
    """.trimIndent()

    // Definir la tabla Categorias
    private val createCategoriasTable  = """
        CREATE TABLE Categorias (
            categoria_id INTEGER PRIMARY KEY,
            nombre TEXT
        )
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase) {
        // Crear las tablas
        db.execSQL(createCuentasTable)
        db.execSQL(createCategoriasTable)
        db.execSQL(createTransaccionesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Actualizar la base de datos si es necesario
        db.execSQL("DROP TABLE IF EXISTS CuentasFinancieras")
        db.execSQL("DROP TABLE IF EXISTS Categorias")
        db.execSQL("DROP TABLE IF EXISTS Transacciones")
        onCreate(db)
    }
}

