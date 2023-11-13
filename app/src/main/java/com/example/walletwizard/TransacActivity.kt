package com.example.walletwizard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.databinding.TransacBinding

class TransacActivity : AppCompatActivity() {

    private lateinit var binding: TransacBinding
    private val transacList = listOf(
        Transac("Domingo`s pizza", 10.45, 23, 11),
        Transac("Mercatienda", 36.65, 16, 11),
        Transac("Corte Frances", 220.98, 29, 9)
    )

    enum class Tipo { TODOS, INGRESOS, GASTOS }

    private var tipo = Tipo.TODOS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TransacBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
        mostrar()
    }

    private fun setupButtons() {
        binding.btnTodos.setOnClickListener { cambiarTipo(Tipo.TODOS) }
        binding.btnIngres.setOnClickListener { cambiarTipo(Tipo.INGRESOS) }
        binding.btnGastos.setOnClickListener { cambiarTipo(Tipo.GASTOS) }
    }

    private fun cambiarTipo(nuevoTipo: Tipo) {
        tipo = nuevoTipo
        mostrar()
        binding.btnTodos.isSelected = nuevoTipo == Tipo.TODOS
        binding.btnIngres.isSelected = nuevoTipo == Tipo.INGRESOS
        binding.btnGastos.isSelected = nuevoTipo == Tipo.GASTOS
    }

    private fun mostrar() {
        val transacListMostrar = when (tipo) {
            Tipo.TODOS -> transacList
            Tipo.INGRESOS -> transacList.subList(2, 3)
            Tipo.GASTOS -> transacList.subList(0, 2)
        }

        val adapter = LvTransacAdapter(this, R.layout.lv_transac_item, transacListMostrar)
        binding.lvTransac.adapter = adapter
    }
}
