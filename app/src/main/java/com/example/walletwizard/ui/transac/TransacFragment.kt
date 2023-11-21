package com.example.walletwizard.ui.transac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.walletwizard.LvTransacAdapter
import com.example.walletwizard.R
import com.example.walletwizard.Transac
import com.example.walletwizard.databinding.FragmentTransacBinding
import com.example.walletwizard.db.FinanzasRepository

class TransacFragment : Fragment() {

    private var _binding: FragmentTransacBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // datos temporales hasta finalizar la bd
    private val transacList = listOf(
        Transac("Domingo`s pizza", 10.45, 23, 11),
        Transac("Mercatienda", 36.65, 16, 11),
        Transac("Corte Frances", 220.98, 29, 9)
    )
    enum class Tipo { TODOS, INGRESOS, GASTOS }
    private var tipo = Tipo.TODOS

    //

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransacBinding.inflate(inflater, container, false)

        setupButtons()
        // por defecto se abre en la pestaña de todos
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
        val rep = FinanzasRepository(requireContext())
        val transacListMostrar = when (tipo) {
            Tipo.TODOS -> rep.getAllTransacciones()
            Tipo.INGRESOS -> rep.getAllTransacciones().filter { it.tipo == "Ingreso" }
            Tipo.GASTOS -> rep.getAllTransacciones().filter { it.tipo == "Gasto" }
        }
        binding.lvTransac.adapter = LvTransacAdapter(requireContext(), R.layout.lv_transac_item, transacListMostrar)
    }


    private fun cambiarTipo(nuevoTipo: Tipo) {
        tipo = nuevoTipo
        mostrar()
        binding.btnTodos.isSelected = nuevoTipo == Tipo.TODOS
        binding.btnIngres.isSelected = nuevoTipo == Tipo.INGRESOS
        binding.btnGastos.isSelected = nuevoTipo == Tipo.GASTOS
    }
}