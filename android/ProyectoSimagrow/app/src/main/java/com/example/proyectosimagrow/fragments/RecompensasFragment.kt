package com.example.proyectosimagrow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.activities.MainActivity
import com.example.proyectosimagrow.adapters.RecompensaAdapter
import com.example.proyectosimagrow.api.RetrofitInstance
import com.example.proyectosimagrow.data.RecompensaResponse
import com.example.proyectosimagrow.data.UsuarioRequest
import com.example.proyectosimagrow.databinding.FragmentRecompensasBinding
import kotlinx.coroutines.launch
import kotlin.collections.arrayListOf

class RecompensasFragment : Fragment() {

    private lateinit var binding: FragmentRecompensasBinding
    private lateinit var adapterFilamento: RecompensaAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val CategoriaRecompensas = arrayOf("Filamentos", "Snacks", "Merchandising")

    private var todasLasRecompensas: List<RecompensaResponse> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecompensasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerRecompensas.setSelection(0)
        
        adapterFilamento = RecompensaAdapter(arrayListOf()) { recompensaResponse -> canjearRecompensa(recompensaResponse) }
        binding.recyclerViewFilamentos.adapter = adapterFilamento
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewFilamentos.layoutManager = linearLayoutManager

        val mainActivity = activity as? MainActivity
        mainActivity?.activeUser?.let {
            binding.tvTokens.text = it.creditos.toString()
        }

        cargarSpinner()

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.instance.getRecompensas()
                Log.d("RecompensasFragment", "Response: ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    todasLasRecompensas = response.body()!!
                    Log.d("RecompensasFragment", "Recompensas cargadas: ${todasLasRecompensas.size}")
                    filtrarRecompensas(CategoriaRecompensas[binding.spinnerRecompensas.selectedItemPosition])
                } else {
                    Toast.makeText(requireContext(), "Error al cargar recompensas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RecompensasFragment", "Error API: ${e.message}")
                Toast.makeText(requireContext(), "Fallo en conexión al obtener recompensas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, CategoriaRecompensas.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRecompensas.adapter = adapter

        binding.spinnerRecompensas.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    filtrarRecompensas(CategoriaRecompensas[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun canjearRecompensa(recompensaResponse: RecompensaResponse) {
        val mainActivity = activity as? MainActivity ?: return
        val user = mainActivity.activeUser ?: return
        
        if (user.creditos < recompensaResponse.tokens) {
            Toast.makeText(requireContext(), "No tienes créditos suficientes", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val newCredits = user.creditos - recompensaResponse.tokens.toInt()
                val request = UsuarioRequest(
                    nombre = user.nombre,
                    apellidos = user.apellidos ?: "User",
                    correo = user.correo,
                    contrasena = user.password ?: "1234",
                    nif = user.nif,
                    creditos = newCredits
                )
                
                val response = RetrofitInstance.instance.actualizarUsuario(user.id, request)
                if (response.isSuccessful) {
                    user.creditos = newCredits
                    mainActivity.updateHeaderCredits(user.creditos)
                    binding.tvTokens.text = user.creditos.toString()
                    Toast.makeText(requireContext(), "¡Has canjeado esta Recompensa con éxito!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("RecompensasFragment", "Error API actualizarUsuario: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Error del servidor al canjear recompensa", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RecompensasFragment", "Error API canjearRecompensa: ${e.message}")
                Toast.makeText(requireContext(), "Error de red al canjear", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filtrarRecompensas(categoria: String) {
        val lsitaFiltrada = when (categoria) {
            "Filamentos" -> todasLasRecompensas.filter { it.tipo.equals("filamento", ignoreCase = true) }
            "Snacks" -> todasLasRecompensas.filter { it.tipo.equals("snacks", ignoreCase = true) }
            "Merchandising" -> todasLasRecompensas.filter { it.tipo.equals("merchandising", ignoreCase = true) }
            else -> todasLasRecompensas
        }
            adapterFilamento.actualizarLista(lsitaFiltrada)
    }

}