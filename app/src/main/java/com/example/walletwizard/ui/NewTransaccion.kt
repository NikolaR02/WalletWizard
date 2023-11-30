package com.example.walletwizard.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.MainActivity
import com.example.walletwizard.R
import com.example.walletwizard.databinding.ActivityNewTransaccionBinding
import com.example.walletwizard.db.FinanzasRepository
import com.example.walletwizard.db.TipoTransaccion
import com.example.walletwizard.db.Transaccion
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewTransaccion : AppCompatActivity() {

    private lateinit var binding: ActivityNewTransaccionBinding
    private var transaccionId: Int = -1
    private var selectedDateInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTransaccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()

        transaccionId = intent.getIntExtra("transaccion_id", -1)

        if (transaccionId != -1) {
            cargarDatosTransaccion()
            binding.btnBorrarTransaccion.visibility = View.VISIBLE
            binding.btnBorrarTransaccion.setOnClickListener { mostrarDialogoConfirmacionBorrarTransaccion() }
        }

        setupButtons()
    }

    private fun setupSpinners() {
        val repository = FinanzasRepository(this)

        val cuentas = repository.getAllCuentas()
        val cuentasAdapter = createSpinnerAdapter(cuentas.map { it.nombreCuenta })
        binding.spinnerCuenta.adapter = cuentasAdapter

        val categorias = repository.getAllCategorias()
        val categoriasAdapter = createSpinnerAdapter(categorias.map { it.nombre })
        binding.spinnerCategoria.adapter = categoriasAdapter
    }

    private fun createSpinnerAdapter(items: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun cargarDatosTransaccion() {
        binding.etNombreTransaccion.setText(intent.getStringExtra("nombre"))
        binding.spinnerCuenta.setSelection(obtenerPosicion(binding.spinnerCuenta, intent.getIntExtra("cuenta_id", -1)))
        binding.spinnerCategoria.setSelection(obtenerPosicion(binding.spinnerCategoria, intent.getIntExtra("categoria_id", -1)))
        selectedDateInMillis = intent.getLongExtra("fecha", 0)
        binding.btnFechaPicker.text = formatDate(selectedDateInMillis)
        binding.switchTipo.isChecked = intent.getSerializableExtra("tipo") == TipoTransaccion.INGRESO
        binding.etImporte.setText(intent.getDoubleExtra("importe", 0.0).toString())
        binding.etNota.setText(intent.getStringExtra("nota"))
        binding.ratingBar.rating = intent.getIntExtra("valoracion", 0).toFloat()
    }

    private fun obtenerPosicion(spinner: AdapterView<*>, itemId: Int): Int {
        val adapter = spinner.adapter as ArrayAdapter<Int>
        return adapter.getPosition(itemId)
    }

    private fun mostrarDialogoConfirmacionBorrarTransaccion() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmacion_borrar))
            .setMessage(getString(R.string.confirmacion_borrar_transaccion_mensaje))
            .setPositiveButton(getString(R.string.si)) { _, _ -> borrarTransaccion() }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    private fun borrarTransaccion() {
        FinanzasRepository(this).deleteTransaccion(transaccionId)
        regresarAlFragmentoAnterior()
    }

    private fun setupButtons() {
        binding.btnGuardarTransaccion.setOnClickListener { guardarTransaccion() }
        binding.btnVolverTransaccion.setOnClickListener { regresarAlFragmentoAnterior() }
        binding.btnFechaPicker.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, _, _, dayOfMonth ->
                selectedDateInMillis = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.timeInMillis

                binding.btnFechaPicker.text = formatDate(selectedDateInMillis)
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.maxDate = currentDate.timeInMillis
        datePickerDialog.show()
    }

    private fun formatDate(dateInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(dateInMillis)
    }

    private fun guardarTransaccion() {
        val nombre = binding.etNombreTransaccion.text.toString()
        val cuentaId = binding.spinnerCuenta.selectedItem // Adjust accordingly
        val categoriaId = binding.spinnerCategoria.selectedItem // Adjust accordingly
        val fecha = selectedDateInMillis
        val tipo = if (binding.switchTipo.isChecked) TipoTransaccion.INGRESO else TipoTransaccion.GASTO
        val importe = binding.etImporte.text.toString().toDoubleOrNull()
        val nota = binding.etNota.text.toString()
        val valoracion = binding.ratingBar.rating.toInt()

        if (nombre.isNotEmpty() && cuentaId != null && categoriaId != null && importe != null) {
            val transaccion = Transaccion(transaccionId, nombre, cuentaId as Int, categoriaId as Int, fecha, tipo, importe, nota, valoracion)

            if (transaccionId == -1) {
                FinanzasRepository(this).insertTransaccion(transaccion)
            } else {
                FinanzasRepository(this).actualizarTransaccion(transaccion)
            }

            Toast.makeText(this,  "Guardando Transacción", Toast.LENGTH_SHORT).show()
            regresarAlFragmentoAnterior()
        } else {
            Toast.makeText(this, "Nombre y/o importe n pueden ser vacios", Toast.LENGTH_SHORT).show()
        }
    }

    private fun regresarAlFragmentoAnterior() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}