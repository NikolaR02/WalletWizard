package com.example.walletwizard

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.databinding.InicioBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: InicioBinding
    private var saldo: Double = 500.98
    private var activos = arrayOf("BBVA ...3695\n${saldo}€","Caixa ...4534\n${saldo}€","Efectivo\n${saldo}€")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSaldoTotal.text= getString(R.string.saldo_total, saldo)
        binding.lvSaldos.adapter = ArrayAdapter(this,R.layout.lv_activos_item,activos)


    }


}