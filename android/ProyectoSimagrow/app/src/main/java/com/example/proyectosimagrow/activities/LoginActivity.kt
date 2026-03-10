package com.example.proyectosimagrow.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectosimagrow.api.RetrofitInstance
import com.example.proyectosimagrow.data.LoginRequest
import com.example.proyectosimagrow.databinding.ActivityLoginBinding
import com.example.proyectosimagrow.data.UserResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrarLogin.setOnClickListener {
            realizarLogin()
        }
    }

    private fun realizarLogin() {
        val nia = binding.etUser.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (!validarCampos(nia, password)) return

        val request = LoginRequest(nia, password)
        Log.e("LOGIN", "$request")

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.instance.login(request)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    user.password = password
                    login(user)
                    Log.d("LOGIN", "Usuario logueado con éxito: ${user.nombre}, Pass: ${user.password}")
                } else {
                    Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

    }

    private fun login(userResponse: UserResponse) {
        val usuarioLogin = Gson().toJson(userResponse)
        val intentMainActivity = Intent(this, MainActivity::class.java).apply {
            putExtra("user", usuarioLogin)
        }
        startActivity(intentMainActivity)
        finish()
    }

    private fun validarCampos(username: String, password: String): Boolean {
        binding.tilUser.error = null
        binding.tilPassword.error = null

        var esValido = true

        if (username.isEmpty()) {
            binding.tilUser.error = "El usuario es obligatorio"
            esValido = false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = "La contraseña es obligatoria"
            esValido = false
        }

        return esValido
    }

}