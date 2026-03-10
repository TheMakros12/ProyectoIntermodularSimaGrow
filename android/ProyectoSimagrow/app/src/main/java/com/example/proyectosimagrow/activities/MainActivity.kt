package com.example.proyectosimagrow.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.databinding.ActivityMainBinding
import com.example.proyectosimagrow.fragments.FormularioIncidenciaFragment
import com.example.proyectosimagrow.fragments.RecompensasFragment
import com.example.proyectosimagrow.fragments.IncidenciasFragment
import com.example.proyectosimagrow.fragments.NoticiasFragment
import com.example.proyectosimagrow.fragments.PerfilFragment
import com.example.proyectosimagrow.data.UserResponse
import com.example.proyectosimagrow.databinding.DrawerHeaderBinding
import com.example.proyectosimagrow.fragments.ConfiguracionFragment
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlin.jvm.java

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: DrawerHeaderBinding

    var activeUser: UserResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(IncidenciasFragment())

        val headerView = binding.navView.getHeaderView(0)
        headerView?.let {
            headerBinding = DrawerHeaderBinding.bind(it)
        }

        val userJson = intent.getStringExtra("user")
        if (userJson != null) {
            val user = Gson().fromJson(userJson, UserResponse::class.java)
            activeUser = user
            cargarDatosHeader(user)
        } else {
            Toast.makeText(this, "No se recibió información del usuario", Toast.LENGTH_SHORT).show()
            finish()
        }

        configurarMenu()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_incidencias -> replaceFragment(IncidenciasFragment())
            R.id.nav_nueva_incidencia -> replaceFragment(FormularioIncidenciaFragment())
            R.id.nav_recompensas -> replaceFragment(RecompensasFragment())
            R.id.nav_noticias -> replaceFragment(NoticiasFragment())
            R.id.nav_profile -> replaceFragment(PerfilFragment())
            R.id.nav_settings -> replaceFragment(ConfiguracionFragment())
            R.id.nav_exit -> mostrarDialogoCerrarSesion()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun configurarMenu() {
        setSupportActionBar(binding.toolBarInclude.myToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolBarInclude.myToolBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close,
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)

        val headerView = binding.navView.getHeaderView(0)
        headerView?.findViewById<TextView>(R.id.tvNombreUsuario)
    }

    private fun mostrarDialogoCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Seguro que quieres cerrar tu sesión?")
            .setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarDatosHeader(userResponse: UserResponse?) {
        if (::headerBinding.isInitialized) {
            userResponse?.let {
                val nombre = it.nombre + " " + it.apellidos
                headerBinding.tvNombreUsuario.text = nombre
                headerBinding.tvCorreoUsuario.text = it.correo
                headerBinding.tvNumeroTokens.text = it.creditos.toString()
            }
        }
    }

    fun updateHeaderCredits(creditos: Int) {
        if (::headerBinding.isInitialized) {
            headerBinding.tvNumeroTokens.text = creditos.toString()
        }
    }

}