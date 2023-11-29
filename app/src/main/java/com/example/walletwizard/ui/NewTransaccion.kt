package com.example.walletwizard.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.MainActivity
import com.example.walletwizard.databinding.ActivityNewTransaccionBinding
import com.example.walletwizard.db.FinanzasRepository
import com.example.walletwizard.db.TipoTransaccion
import com.example.walletwizard.db.Transaccion
import java.util.Calendar

class NewTransaccion : AppCompatActivity() {

    private lateinit var binding: ActivityNewTransaccionBinding
    private var nuevo: Boolean = true
    private var selectedDateInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTransaccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()

        binding.btnFechaPicker.setOnClickListener { showDatePickerDialog() }
        binding.btnGuardarTransaccion.setOnClickListener { guardarTransaccion() }
        binding.btnVolverTransaccion.setOnClickListener { regresarAlFragmentoAnterior() }
    }

    private fun setupSpinners() {
        val cuentas = FinanzasRepository(this).getAllCuentas()
        val categorias = FinanzasRepository(this).getAllCategorias()

        // Adaptadores para los spinners
        val cuentaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuentas.map { it.nombreCuenta })
        val categoriaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias.map { it.nombre })

        cuentaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar adaptadores a los spinners
        binding.spinnerCuenta.adapter = cuentaAdapter
        binding.spinnerCategoria.adapter = categoriaAdapter
    }

    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                selectedDateInMillis = selectedDate.timeInMillis

                // Actualizar la interfaz de usuario con la fecha seleccionada
                binding.btnFechaPicker.text = formatDate(selectedDateInMillis)
            },
            year,
            month,
            day
        )

        // Configurar límite para seleccionar solo fechas pasadas o actuales
        datePickerDialog.datePicker.maxDate = currentDate.timeInMillis

        // Mostrar el diálogo
        datePickerDialog.show()
    }

    private fun formatDate(dateInMillis: Long): String {
        // Implementa la lógica para formatear la fecha según tus necesidades
        // En este ejemplo, se muestra la fecha en formato dd/MM/yyyy
        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        return dateFormat.format(dateInMillis)
    }

    private fun guardarTransaccion() {
        val nombre = binding.etNombreTransaccion.text.toString()
        val cuentaPosition = binding.spinnerCuenta.selectedItemPosition
        val categoriaPosition = binding.spinnerCategoria.selectedItemPosition
        val fecha = selectedDateInMillis
        val tipo = if (binding.switchTipo.isChecked) TipoTransaccion.INGRESO else TipoTransaccion.GASTO
        val importe = binding.etImporte.text.toString().toDoubleOrNull() ?: 0.0
        val nota = binding.etNota.text.toString()
        val valoracion = binding.ratingBar.rating.toInt()

        // Obtener cuentas y categorías desde las listas cargadas en los spinners
        val cuentas = FinanzasRepository(this).getAllCuentas()
        val categorias = FinanzasRepository(this).getAllCategorias()

        // Verificar que la posición sea válida
        if (cuentaPosition in cuentas.indices && categoriaPosition in categorias.indices) {
            val cuentaId = cuentas[cuentaPosition].cuentaId
            val categoriaId = categorias[categoriaPosition].categoriaId

            val transaccion = Transaccion(0, nombre, cuentaId, categoriaId, fecha, tipo, importe, nota, valoracion)

            // Insertar la transacción en la base de datos
            val transaccionId = FinanzasRepository(this).insertTransaccion(transaccion)

            if (transaccionId != -1L) {
                // Mostrar mensaje de éxito y regresar
                Toast.makeText(this, "Guardando T", Toast.LENGTH_SHORT).show()
                regresarAlFragmentoAnterior()
            } else {
                // Mostrar mensaje de error si la inserción falla
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Mostrar mensaje de error si la posición no es válida
            Toast.makeText(this, "Error al optener los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun regresarAlFragmentoAnterior() {
        // Regresar a la actividad principal
        startActivity(Intent(this, MainActivity::class.java))
    }
}
