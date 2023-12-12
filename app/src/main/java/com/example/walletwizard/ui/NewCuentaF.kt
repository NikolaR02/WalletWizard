package com.example.walletwizard.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.MainActivity
import com.example.walletwizard.R
import com.example.walletwizard.databinding.ActivityNewCuentaFBinding
import com.example.walletwizard.db.CuentaFinanciera
import com.example.walletwizard.db.FinanzasRepository

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

        nombreET = binding.etNombreCuenta
        saldoET = binding.etSaldoInicial
        cuentaId = intent.getIntExtra("cuenta_id", -1)
        nuevo = cuentaId == -1

        setupUI()

        binding.btnGuardarCuenta.setOnClickListener { guardarCuenta() }
        binding.btnVolver.setOnClickListener { regresarAlFragmentoAnterior() }
    }

    private fun setupUI() {
        if (!nuevo) {
            nombreCuenta = intent.getStringExtra("nombre") ?: ""
            saldoCuenta = intent.getDoubleExtra("saldo", 0.0)
            binding.btnBorrarCuenta.visibility = View.VISIBLE
            binding.btnBorrarCuenta.setOnClickListener { mostrarDialogoConfirmacionBorrarCuenta() }

            nombreET.setText(nombreCuenta)
            saldoET.hint = saldoCuenta.toString()
            saldoET.isEnabled = false
            showToast(getString(R.string.modificar_cuenta, nombreCuenta))
        } else {
            showToast(getString(R.string.introduce_un_nombre_descriptivo_y_el_saldo_actual_si_tiene))
        }
    }

    private fun mostrarDialogoConfirmacionBorrarCuenta() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmacion_borrar))
            .setMessage(getString(R.string.confirmacion_borrar_cuenta_mensaje))
            .setPositiveButton(getString(R.string.si)) { _, _ -> borrarCuenta() }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    private fun borrarCuenta() {
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

            if (nuevo) {
                finanzasRepository.insertCuenta(cuenta,this)
            } else {
                finanzasRepository.actualizarCuenta(cuenta)
            }

            showToast(getString(R.string.guardando_cuenta))
            regresarAlFragmentoAnterior()
        } else {
            showToast(getString(R.string.ingrese_un_nombre))
        }
    }

    private fun regresarAlFragmentoAnterior() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
