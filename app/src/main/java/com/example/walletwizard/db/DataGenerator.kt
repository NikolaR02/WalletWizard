package com.example.walletwizard.db

import android.content.Context
import java.util.Locale
import java.util.concurrent.TimeUnit

// Una clase utilitaria con un método estático para insertar datos ficticios en las tablas.
object DataGenerator {

    private lateinit var cuentas: List<String>
    private lateinit var categorias: List<String>

    fun insertDummyData(context: Context, deleteExistingData: Boolean) {
        val finanzasRepository = FinanzasRepository(context)

        if (deleteExistingData) {
            // Borrar datos existentes
            delete(context)
        }

        // Insertar categorías
        categorias = listOf("Alimentación", "Entretenimiento", "Transporte", "Salud", "Otros")
        for (nombreCategoria in categorias) {
            println(nombreCategoria + ": " + finanzasRepository.insertCategoria(Categoria(0, nombreCategoria)))
        }

        // Insertar cuentas financieras ficticias
        cuentas = listOf("Cuenta Bancaria A", "Cuenta Bancaria B", "Efectivo")
        for (nombreCuenta in cuentas) {
            println(nombreCuenta + ": " + finanzasRepository.insertCuenta(CuentaFinanciera(0, nombreCuenta, 0.0)))
        }

        //insertarDatosBucle(context, 30)
        insertarDatosSignificativos(context)
    }

    fun insertarDatosBucle(context: Context, numeroTransacciones: Int) {
        val finanzasRepository = FinanzasRepository(context)

        val threeYearsMillis = TimeUnit.DAYS.toMillis(365 * 3)

        for (i in 1..numeroTransacciones) {
            val cuentaId = (1..cuentas.size).random()
            val categoriaId = (1..categorias.size).random()
            val nombre = "Transacción $i"

            // Generar fecha aleatoria en los últimos 3 años
            val randomDateMillis = System.currentTimeMillis() - (0..threeYearsMillis).random()

            val tipo = if (i % 2 == 0) TipoTransaccion.INGRESO else TipoTransaccion.GASTO

            // Generar importe con 2 decimales
            val importe = String.format(Locale.US, "%.2f", (1..100).random() + Math.random()).toDouble()

            val nota = "Nota de la transacción $i"
            val valoracion = if (tipo == TipoTransaccion.INGRESO) 0 else (1..5).random()

            val transaccion = Transaccion(0, nombre, cuentaId, categoriaId, randomDateMillis, tipo, importe, nota, valoracion)
            finanzasRepository.insertTransaccion(transaccion)
        }
    }

    fun insertarDatosSignificativos(context: Context) {
        val finanzasRepository = FinanzasRepository(context)

        // Gastos
        val gasto1 = Transaccion(0, "Compra en Zara", 1, 2, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 50.75, "Ropa de moda", 4)
        val gasto2 = Transaccion(0, "Compra en Lidl", 2, 1, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 25.99, "Comestibles", 3)
        val gasto3 = Transaccion(0, "Material de papelería", 3, 5, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 15.30, "Utiles de oficina", 5)
        val gasto4 = Transaccion(0, "Cena en restaurante", 1, 2, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 70.40, "Salida con amigos", 4)
        val gasto5 = Transaccion(0, "Factura de luz", 2, 4, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 40.50, "Suministros del hogar", 2)
        val gasto6 = Transaccion(0, "Gasolina", 3, 3, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 55.20, "Combustible para el coche", 3)
        val gasto7 = Transaccion(0, "Compra de libros", 1, 2, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 32.80, "Libros para la lectura", 5)
        val gasto8 = Transaccion(0, "Mantenimiento del coche", 2, 1, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 120.00, "Reparaciones y servicio", 4)
        val gasto9 = Transaccion(0, "Regalos de cumpleaños", 3, 5, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 60.75, "Obsequios para amigos", 5)
        val gasto10 = Transaccion(0, "Compra en Papelería", 1, 2, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 18.45, "Materiales de escritura", 4)
        val gasto11 = Transaccion(0, "Compra en Ikea", 2, 1, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 120.90, "Mobiliario para el hogar", 4)
        val gasto12 = Transaccion(0, "Entrada al cine", 3, 2, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 15.50, "Entretenimiento", 3)
        val gasto13 = Transaccion(0, "Compra de ropa deportiva", 1, 5, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 80.25, "Artículos deportivos", 5)
        val gasto14 = Transaccion(0, "Cena en restaurante de lujo", 2, 4, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 120.00, "Celebración especial", 4)
        val gasto15 = Transaccion(0, "Compra de electrónicos", 3, 3, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 350.99, "Gadgets y dispositivos", 2)
        val gasto16 = Transaccion(0, "Factura de teléfono", 1, 5, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 25.80, "Servicio telefónico", 3)
        val gasto17 = Transaccion(0, "Suscripción a revistas", 2, 1, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 10.50, "Lectura mensual", 5)
        val gasto18 = Transaccion(0, "Compra de juguetes", 3, 2, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 40.00, "Juguetes para niños", 4)
        val gasto19 = Transaccion(0, "Compra de plantas", 1, 5, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 30.25, "Decoración verde", 5)
        val gasto20 = Transaccion(0, "Compra en tienda de informática", 2, 1, obtenerFechaAleatoria(), TipoTransaccion.GASTO, 90.60, "Accesorios y gadgets", 4)

        // Ingresos
        val ingreso1 = Transaccion(0, "Nómina", 1, 5, obtenerFechaAleatoria(), TipoTransaccion.INGRESO, 2500.00, "Salario mensual", 5)
        val ingreso2 = Transaccion(0, "Venta de artículos usados", 2, 2, obtenerFechaAleatoria(), TipoTransaccion.INGRESO, 80.50, "Artículos en línea", 4)
        val ingreso3 = Transaccion(0, "Ingreso adicional", 3, 3, obtenerFechaAleatoria(), TipoTransaccion.INGRESO, 150.00, "Ingreso inesperado", 3)
        val ingreso4 = Transaccion(0, "Bono anual", 1, 1, obtenerFechaAleatoria(), TipoTransaccion.INGRESO, 500.00, "Recompensa laboral", 5)
        val ingreso5 = Transaccion(0, "Venta de antigüedades", 2, 2, obtenerFechaAleatoria(), TipoTransaccion.INGRESO, 120.25, "Objetos coleccionables", 4)
        val ingreso6 = Transaccion(0, "Ingreso por alquiler", 3, 3, obtenerFechaAleatoria(), TipoTransaccion.INGRESO, 200.00, "Renta mensual", 3)

        // Insertar las transacciones en la base de datos
        finanzasRepository.insertTransaccion(gasto1)
        finanzasRepository.insertTransaccion(gasto2)
        finanzasRepository.insertTransaccion(gasto3)
        finanzasRepository.insertTransaccion(gasto4)
        finanzasRepository.insertTransaccion(gasto5)
        finanzasRepository.insertTransaccion(gasto6)
        finanzasRepository.insertTransaccion(gasto7)
        finanzasRepository.insertTransaccion(gasto8)
        finanzasRepository.insertTransaccion(gasto9)
        finanzasRepository.insertTransaccion(gasto10)
        finanzasRepository.insertTransaccion(gasto11)
        finanzasRepository.insertTransaccion(gasto12)
        finanzasRepository.insertTransaccion(gasto13)
        finanzasRepository.insertTransaccion(gasto14)
        finanzasRepository.insertTransaccion(gasto15)
        finanzasRepository.insertTransaccion(gasto16)
        finanzasRepository.insertTransaccion(gasto17)
        finanzasRepository.insertTransaccion(gasto18)
        finanzasRepository.insertTransaccion(gasto19)
        finanzasRepository.insertTransaccion(gasto20)
        finanzasRepository.insertTransaccion(ingreso1)
        finanzasRepository.insertTransaccion(ingreso2)
        finanzasRepository.insertTransaccion(ingreso3)
        finanzasRepository.insertTransaccion(ingreso4)
        finanzasRepository.insertTransaccion(ingreso5)
        finanzasRepository.insertTransaccion(ingreso6)
    }

    private fun obtenerFechaAleatoria(): Long {
        // Obtener una fecha aleatoria dentro de los últimos 3 años
        val ahora = System.currentTimeMillis()
        val tresAniosEnMillis = TimeUnit.DAYS.toMillis(365 * 3)
        return (ahora - (0..tresAniosEnMillis).random()).coerceAtLeast(0)
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
