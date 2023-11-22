package com.example.walletwizard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.walletwizard.db.TipoTransaccion
import com.example.walletwizard.db.Transaccion
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class LvTransacAdapter(
    private val context: Context,
    private val layoutResourceId: Int,
    transacList: List<Transaccion>
) : ArrayAdapter<Transaccion?>(context, layoutResourceId, transacList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(layoutResourceId, parent, false)
        }

        // Obtén el elemento de la lista en la posición actual
        val transac = getItem(position)

        // Enlaza los datos a las vistas en el diseño XML
        val nombreTextView = listItemView!!.findViewById<TextView>(R.id.nombre)
        val cantidadTextView = listItemView.findViewById<TextView>(R.id.cantidad)
        val fechaTextView = listItemView.findViewById<TextView>(R.id.fecha)

        // Verifica si el elemento no es nulo antes de asignar los datos
        if (transac != null) {
            //nombre
            nombreTextView.text = transac.nombre
            //importe
            val signo = if (transac.tipo == TipoTransaccion.INGRESO) R.string.euros else R.string.neuros
            cantidadTextView.text = context.getString(signo, transac.importe)
            //fecha
            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            val fechaFormateada = dateFormat.format(Date(transac.fecha))
            fechaTextView.text = fechaFormateada
        }

        return listItemView
    }
}
