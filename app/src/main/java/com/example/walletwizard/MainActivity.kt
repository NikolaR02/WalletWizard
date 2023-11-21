package com.example.walletwizard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.example.walletwizard.databinding.ActivityMainBinding
import com.example.walletwizard.db.FinanzasRepository


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* código para controlar con un toolbar
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration( setOf( R.id.nav_inicio), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        */

        //DataGenerator.insertDummyData(this, true)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Configurar el listener para cerrar el cajón de navegación al seleccionar un ítem y cambiar de fragment si corresponde
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // --POR HACER: si se cambia de pantalla con gesto o botón de atrás el menú no cambia--
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawers()

            // Manejar la navegación al fragment correspondiente
            when (menuItem.itemId) {
                R.id.menu_inicio -> navController.navigate(R.id.nav_inicio)
                R.id.menu_transac -> navController.navigate(R.id.nav_transac)
                // Agrega más casos según los fragments
            }
            true
        }
        //botón para abrir el menú
        binding.appBarMain.fabToggleDrawer.setOnClickListener {
            toggleDrawer()
        }
    }

    // abrir el menú
    fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}