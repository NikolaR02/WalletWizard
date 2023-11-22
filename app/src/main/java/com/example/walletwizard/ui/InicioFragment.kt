package com.example.walletwizard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.walletwizard.R
import com.example.walletwizard.databinding.FragmentInicioBinding
import com.example.walletwizard.db.FinanzasRepository

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)

        val repository = FinanzasRepository(requireContext())
        val (listaSaldos, totalSaldos) = obtenerListaSaldosFormateados(repository)

        binding.tvSaldoTotal.text = getString(R.string.saldo_total, totalSaldos)
        binding.lvSaldos.adapter = ArrayAdapter(requireContext(), R.layout.lv_activos_item, listaSaldos)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtenerListaSaldosFormateados(repository: FinanzasRepository): Pair<List<String>, Double> {
        val cuentas = repository.getAllCuentas()
        var totalSaldos = 0.0

        val listaSaldos = cuentas.map { cuenta ->
            val saldoFormateado = "${cuenta.nombreCuenta} \n Saldo: ${getString(R.string.euros, cuenta.saldo)}"
            totalSaldos += cuenta.saldo
            saldoFormateado
        }

        return Pair(listaSaldos, totalSaldos)
    }
}
