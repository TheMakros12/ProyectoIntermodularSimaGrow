package com.example.proyectosimagrow.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectosimagrow.databinding.ActivityLoginBinding
import com.example.proyectosimagrow.pojo.User
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrarLogin.setOnClickListener {
            val username: String = binding.etUser.text.toString()
            val password: String = binding.etPassword.text.toString()
            val user = User(username, password)
            login(user)
        }

    }

    fun login(user: User) {
        val usuarioLogin = Gson().toJson(user)
        val intentMainActivity = Intent(this, MainActivity::class.java).apply {
            putExtra("user", usuarioLogin)
        }
        startActivity(intentMainActivity)
    }

}