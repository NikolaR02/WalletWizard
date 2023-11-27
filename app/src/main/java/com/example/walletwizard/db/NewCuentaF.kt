package com.example.walletwizard.db

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.walletwizard.MainActivity
import com.example.walletwizard.R
import com.example.walletwizard.databinding.ActivityNewCuentaFBinding

class NewCuentaF : AppCompatActivity() {

    private lateinit var binding: ActivityNewCuentaFBinding
    private lateinit var nombreET: EditText
    private lateinit var saldoET: EditText
    private var cuentaId: Int = -1
    private var nombreCuenta: String = ""
    private var saldoCuenta: Double = 0.0
    private var nuevo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCuentaFBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar vistas y obtener datos de la intención
        nombreET = binding.etNombreCuenta
        saldoET = binding.etSaldoInicial
        cuentaId = intent.getIntExtra("cuenta_id", -1)
        nuevo = cuentaId == -1

        setupUI()

        // Configurar acciones de los botones
        binding.btnGuardarCuenta.setOnClickListener { guardarCuenta() }
        binding.btnVolver.setOnClickListener { regresarAlFragmentoAnterior() }
    }

    private fun setupUI() {
        if (!nuevo) {
            // Configurar para una cuenta existente
            nombreCuenta = intent.getStringExtra("nombre") ?: ""
            saldoCuenta = intent.getDoubleExtra("saldo", 0.0)
            binding.btnBorrarCuenta.visibility = View.VISIBLE
            binding.btnBorrarCuenta.setOnClickListener { mostrarDialogoConfirmacionBorrarCuenta() }

            nombreET.setText(nombreCuenta)
            saldoET.hint = saldoCuenta.toString()
            saldoET.isEnabled = false

            binding.tvMensaje.text = getString(R.string.modificar_cuenta, nombreCuenta)
        } else {
            // Configurar para crear una nueva cuenta
            binding.tvMensaje.text =
                getString(R.string.introduce_un_nombre_descriptivo_y_el_saldo_actual_si_tiene)
        }
    }

    private fun mostrarDialogoConfirmacionBorrarCuenta() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmacion_borrar_cuenta))
            .setMessage(getString(R.string.confirmacion_borrar_cuenta_mensaje))
            .setPositiveButton(getString(R.string.si)) { _, _ -> borrarCuenta() }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    private fun borrarCuenta() {
        // Eliminar cuenta de la base de datos y regresar
        FinanzasRepository(this).deleteCuenta(cuentaId)
        regresarAlFragmentoAnterior()
    }

    private fun guardarCuenta() {
        val finanzasRepository = FinanzasRepository(this)
        nombreCuenta = nombreET.text.toString()

        if (nombreCuenta.isNotEmpty()) {
            val cuenta = if (nuevo) {
                CuentaFinanciera(
                    0,
                    nombreCuenta,
                    saldoET.text.toString().toDoubleOrNull() ?: 0.0
                )
            } else {
                CuentaFinanciera(cuentaId, nombreCuenta, saldoCuenta)
            }

            // Insertar o actualizar la cuenta en la base de datos
            if (nuevo) {
                finanzasRepository.insertCuenta(cuenta)
            } else {
                finanzasRepository.actualizarCuenta(cuenta)
            }

            // Mostrar mensaje de éxito y regresar
            binding.tvMensaje.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_green_dark
                )
            )
            binding.tvMensaje.text = getString(R.string.guardando_cuenta)
            regresarAlFragmentoAnterior()
        } else {
            // Mostrar mensaje de error si el nombre de la cuenta está vacío
            binding.tvMensaje.text = getString(R.string.ingrese_un_nombre)
            binding.tvMensaje.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_red_dark
                )
            )
        }
    }

    private fun regresarAlFragmentoAnterior() {
        // Regresar a la actividad principal
        startActivity(Intent(this, MainActivity::class.java))
    }
}
