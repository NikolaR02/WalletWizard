package com.example.walletwizard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.example.walletwizard.databinding.ActivityMainBinding
import com.example.walletwizard.db.DataGenerator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Configurar el listener para cerrar el cajón de navegación al seleccionar un ítem y cambiar de fragment si corresponde
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawers()

            // Manejar la navegación al fragment correspondiente
            when (menuItem.itemId) {
                R.id.menu_inicio -> navController.navigate(R.id.nav_inicio)
                R.id.menu_transac -> navController.navigate(R.id.nav_transac)
                R.id.menu_datos -> {
                    mostrarDialogo()
                }
            }
            true
        }

        //botón para abrir el menú
        binding.appBarMain.fabToggleDrawer.setOnClickListener {
            toggleDrawer()
        }


    }

    // abrir el menú
    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun mostrarDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.datos_prueba)
        builder.setMessage("¿Desea insertar datos ficticios o borrar todos los datos almacenados?")

        builder.setPositiveButton("Insertar") { _, _ ->
            // Lógica para insertar datos ficticios
            DataGenerator.insertDummyData(this, true)
            mostrarMensaje("Insertar datos ficticios")
        }

        builder.setNegativeButton("Borrar") { _, _ ->
            // Lógica para borrar todos los datos
            DataGenerator.delete(this)
            mostrarMensaje("Borrar todos los datos")
        }

        builder.setNeutralButton("Volver") { _, _ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

}
