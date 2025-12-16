package com.example.proyectosimagrow.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.databinding.ActivityMainBinding
import com.example.proyectosimagrow.fragments.FilamentosFragment
import com.example.proyectosimagrow.fragments.IncidenciasFragment
import com.example.proyectosimagrow.fragments.PerfilFragment
import com.example.proyectosimagrow.pojo.User
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRecibido = intent.getStringExtra("user")
        var user: User? = null

        if (userRecibido != null) {
            user = Gson().fromJson(userRecibido, User::class.java)
        }

        binding.bottomNavView.selectedItemId = R.id.menuIncidencias
        binding.tvUsernameUser.text = user?.username.toString()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, IncidenciasFragment())
            .commit()

        binding.bottomNavView.setOnItemSelectedListener {
            item ->
            when(item.itemId) {
                R.id.menuIncidencias -> lanzarFragmentIncidencias()
                R.id.menuPremios -> lanzarFragmentPremios()
                R.id.menuPerfil -> lanzarFragmentPerfil()
            }
            true
        }

    }

    private fun lanzarFragmentPremios() {
        binding.cardViewUser.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FilamentosFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun lanzarFragmentIncidencias() {
        binding.cardViewUser.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, IncidenciasFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun lanzarFragmentPerfil() {
        binding.cardViewUser.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PerfilFragment())
            .addToBackStack(null)
            .commit()
    }

}