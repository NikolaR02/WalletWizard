package com.example.walletwizard.ui

import android.content.Intent
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
        val adapter =  ArrayAdapter(requireContext(), R.layout.lv_activos_item, listaSaldos)
        binding.lvSaldos.adapter = adapter

        binding.lvSaldos.setOnItemClickListener { _, _, position, _ ->
            // Obtener la cuenta seleccionada
            val cuentaSeleccionada = repository.getAllCuentas()[position]

            // Crear un Intent con el id de la cuenta
            val intent = Intent(requireContext(), NewCuentaF::class.java)
            intent.putExtra("cuenta_id", cuentaSeleccionada.cuentaId)
            intent.putExtra("nombre", cuentaSeleccionada.nombreCuenta)
            intent.putExtra("saldo", cuentaSeleccionada.saldo)

            // Iniciar la actividad
            startActivity(intent)
        }

        binding.fabNueva.setOnClickListener {
            val intent = Intent(requireContext(), NewCuentaF::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtenerListaSaldosFormateados(repository: FinanzasRepository): Pair<List<String>, Double> {
        val cuentas = repository.getAllCuentas()
        var totalSaldos = 0.0

        val listaSaldos = mutableListOf<String>()

        for (cuenta in cuentas) {
            val saldoFormateado = "${cuenta.nombreCuenta} \n ${getString(R.string.saldo)}: ${getString(R.string.euros, cuenta.saldo)}"
            listaSaldos.add(saldoFormateado)
            totalSaldos += cuenta.saldo
        }

        return Pair(listaSaldos, totalSaldos)
    }

}