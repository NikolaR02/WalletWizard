package com.example.walletwizard.db

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.walletwizard.R
import com.example.walletwizard.databinding.ActivityNewCuentaFBinding

class NewCuentaF : AppCompatActivity() {

    private lateinit var binding: ActivityNewCuentaFBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCuentaFBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGuardarCuenta.setOnClickListener {
            guardarCuenta()
        }

        binding.btnVolver.setOnClickListener {
            regresarAlFragmentoAnterior()
        }

        binding.tvMensaje.text = intent.getStringExtra("cuentaId")
    }

    private fun guardarCuenta() {
        val nombreCuenta = binding.etNombreCuenta.text.toString()
        val saldoInicial = binding.etSaldoInicial.text.toString().toDoubleOrNull() ?: 0.0
        val finanzasRepository = FinanzasRepository(this)

        if (nombreCuenta.isNotEmpty()) {
            binding.tvMensaje.text = finanzasRepository.insertCuenta(CuentaFinanciera(0, nombreCuenta, saldoInicial)).toString()
            regresarAlFragmentoAnterior()
        } else {
            // Mostrar un mensaje de error si el nombre de la cuenta está vacío
            binding.tvMensaje.text = getString(R.string.ingrese_un_nombre)
            binding.tvMensaje.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }
    }

    private fun regresarAlFragmentoAnterior() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.popBackStack(R.id.nav_inicio, true)
    }
}