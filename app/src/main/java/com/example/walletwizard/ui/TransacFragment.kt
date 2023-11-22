package com.example.walletwizard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.walletwizard.LvTransacAdapter
import com.example.walletwizard.R
import com.example.walletwizard.databinding.FragmentTransacBinding
import com.example.walletwizard.db.FinanzasRepository
import com.example.walletwizard.db.TipoTransaccion
import com.example.walletwizard.db.Transaccion

class TransacFragment : Fragment() {

    private var _binding: FragmentTransacBinding? = null
    private val binding get() = _binding!!

    enum class Tipo { TODOS, INGRESOS, GASTOS }
    private var tipo = Tipo.TODOS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransacBinding.inflate(inflater, container, false)

        setupButtons()
        cambiarTipo(Tipo.TODOS)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupButtons() {
        binding.btnTodos.setOnClickListener { cambiarTipo(Tipo.TODOS) }
        binding.btnIngres.setOnClickListener { cambiarTipo(Tipo.INGRESOS) }
        binding.btnGastos.setOnClickListener { cambiarTipo(Tipo.GASTOS) }
    }

    private fun mostrar() {
        val repository = FinanzasRepository(requireContext())
        val transacciones = obtenerTransaccionesSegunTipo(repository)
        binding.lvTransac.adapter = LvTransacAdapter(requireContext(), R.layout.lv_transac_item, transacciones)
    }

    private fun obtenerTransaccionesSegunTipo(repository: FinanzasRepository): List<Transaccion> {
        return when (tipo) {
            Tipo.TODOS -> repository.getAllTransacciones()
            Tipo.INGRESOS -> repository.getAllTransacciones().filter { it.tipo == TipoTransaccion.INGRESO }
            Tipo.GASTOS -> repository.getAllTransacciones().filter { it.tipo == TipoTransaccion.GASTO }
        }
    }

    private fun cambiarTipo(nuevoTipo: Tipo) {
        tipo = nuevoTipo
        mostrar()
        binding.btnTodos.isSelected = nuevoTipo == Tipo.TODOS
        binding.btnIngres.isSelected = nuevoTipo == Tipo.INGRESOS
        binding.btnGastos.isSelected = nuevoTipo == Tipo.GASTOS
    }
}
