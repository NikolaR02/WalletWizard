package com.example.walletwizard

//import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class LvTransacAdapter(
    private val context: Context,
    private val layoutResourceId: Int,
    transacList: List<Transac>
) : ArrayAdapter<Transac?>(context, layoutResourceId, transacList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutResourceId, parent, false)
        }

        // Obtén el elemento de la lista en la posición actual
        val transac: Transac? = getItem(position)

        // Enlaza los datos a las vistas en el diseño XML
        val nombreTextView = convertView!!.findViewById<TextView>(R.id.nombre)
        val cantidadTextView = convertView.findViewById<TextView>(R.id.cantidad)
        val fechaTextView = convertView.findViewById<TextView>(R.id.fecha)

        // Verifica si el elemento no es nulo antes de asignar los datos
        if (transac != null) {
            nombreTextView.text = transac.nombre
            cantidadTextView.text = "${transac.cantidad}€"
            fechaTextView.text = transac.getFecha()
        }

        return convertView
    }
/*
    fun actualizarDatos(nuevaLista: List<Transac>) {
        clear()  // Limpia la lista actual
        addAll(nuevaLista)  // Agrega la nueva lista
        notifyDataSetChanged()
    }*/
}
