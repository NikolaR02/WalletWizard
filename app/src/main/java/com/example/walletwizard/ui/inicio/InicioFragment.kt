package com.example.walletwizard.ui.inicio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.walletwizard.R
import com.example.walletwizard.databinding.FragmentInicioBinding

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var saldo: Double = 500.98
    private var activos = arrayOf("BBVA ...3695\n${saldo}€","Caixa ...4534\n${saldo}€","Efectivo\n${saldo}€")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)


        binding.tvSaldoTotal.text= getString(R.string.saldo_total, saldo)
        binding.lvSaldos.adapter = ArrayAdapter(requireContext(),R.layout.lv_activos_item,activos)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}