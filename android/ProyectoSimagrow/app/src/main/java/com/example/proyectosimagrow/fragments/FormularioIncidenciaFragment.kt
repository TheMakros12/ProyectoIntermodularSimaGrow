package com.example.proyectosimagrow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import android.util.Log
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.activities.MainActivity
import com.example.proyectosimagrow.api.RetrofitInstance
import com.example.proyectosimagrow.data.Espacio
import com.example.proyectosimagrow.data.IncidenciaRequest
import com.example.proyectosimagrow.data.UsuarioRequest
import com.example.proyectosimagrow.databinding.FragmentFormularioIncidenciaBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormularioIncidenciaFragment : Fragment() {

    private lateinit var binding: FragmentFormularioIncidenciaBinding
    private val plantas = arrayOf("0", "1", "2")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormularioIncidenciaBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        limparCampos()
        cargarSpinnerPlantas()

        val incidentId = arguments?.getInt("id")
        val incidentNombre = arguments?.getString("nombre")
        val incidentDescripcion = arguments?.getString("descripcion")

        if (incidentId != null) {
            binding.etNombreIncidencia.setText(incidentNombre)
            binding.etDescripcionIncidencia.setText(incidentDescripcion)
            binding.btnEnviar.text = "Actualizar"
        }

        binding.btnBorrar.setOnClickListener {
            limparCampos()
            Toast.makeText(requireContext(), "Se han borrado los datos!", Toast.LENGTH_SHORT).show()
        }

        binding.btnEnviar.setOnClickListener {
            if (validarFormulario()) {
                val mainActivity = activity as? MainActivity ?: return@setOnClickListener
                val user = mainActivity.activeUser ?: return@setOnClickListener
                
                val espacioSeleccionado = binding.spinnerEspacions.selectedItem as? Espacio
                if (espacioSeleccionado == null) {
                    binding.tiLayputDescripcionIncidencia.error = "Seleccione un espacio a vincular"
                    return@setOnClickListener
                }

                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val request = IncidenciaRequest(
                    titulo = binding.etNombreIncidencia.text.toString(),
                    descripcion = binding.etDescripcionIncidencia.text.toString(),
                    usuarioId = user.id,
                    espacioId = espacioSeleccionado.id,
                    fechaIncidencia = currentDate
                )

                lifecycleScope.launch {
                    try {
                        if (incidentId != null) {
                            val response = RetrofitInstance.instance.modificarIncidencia(incidentId, request)
                            if (response.isSuccessful) {
                                Toast.makeText(requireContext(), "Incidencia actualizada!", Toast.LENGTH_SHORT).show()
                                mainActivity.replaceFragment(IncidenciasFragment())
                            } else {
                                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val response = RetrofitInstance.instance.crearIncidencia(request)
                            if (response.isSuccessful) {
                                // Add 10 credits to user via API
                                val newCredits = user.creditos + 3
                                val userRequest = UsuarioRequest(
                                    nombre = user.nombre,
                                    apellidos = user.apellidos ?: "User",
                                    correo = user.correo,
                                    contrasena = user.password ?: "1234",
                                    nif = user.nif,
                                    creditos = newCredits
                                )
                                val userResponse = RetrofitInstance.instance.actualizarUsuario(user.id, userRequest)
                                if (userResponse.isSuccessful) {
                                    user.creditos = newCredits
                                    mainActivity.updateHeaderCredits(user.creditos)
                                } else {
                                    Log.e("FormularioIncidenciaFragment", "Error API actualizarUsuario: ${userResponse.code()} - ${userResponse.errorBody()?.string()}")
                                }
                                Toast.makeText(requireContext(), "Incidencia enviada. ¡+10 CRÉDITOS!", Toast.LENGTH_SHORT).show()
                                mainActivity.replaceFragment(IncidenciasFragment())
                            } else {
                                Toast.makeText(requireContext(), "Error al crear", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("FormularioIncidenciaFragment", "Error API Detele: ${e.message}")
                        Toast.makeText(requireContext(), "Error de red al enviar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.spinnerPlantaEspacions.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val plantaSeleccionada = plantas[position]
                    cargarSpinnerEspacios(plantaSeleccionada)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun limparCampos() {
        binding.etNombreIncidencia.text?.clear()
        binding.spinnerEspacions.setSelection(0)
        binding.etDescripcionIncidencia.text?.clear()
    }

    private fun validarFormulario(): Boolean {

        val titulo = binding.etNombreIncidencia.text.toString().trim()
        val descripcion = binding.etDescripcionIncidencia.text.toString().trim()

        var esValido = true

        binding.tiLayputTituloIncidencia.error = null
        binding.tiLayputDescripcionIncidencia.error = null

        if (titulo.isEmpty()) {
            binding.tiLayputTituloIncidencia.error = "El título no puede estar vacío"
            esValido = false
        } else if (titulo.length > 30) {
            binding.tiLayputTituloIncidencia.error = "Máximo 30 caracteres"
            esValido = false
        }

        if (descripcion.isEmpty()) {
            binding.tiLayputDescripcionIncidencia.error = "La descripción no puede estar vacía"
            esValido = false
        } else if (descripcion.length > 250) {
            binding.tiLayputDescripcionIncidencia.error = "Máximo 250 caracteres"
            esValido = false
        }

        return esValido
    }

    private fun cargarSpinnerPlantas() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, plantas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPlantaEspacions.adapter = adapter
    }

    private fun cargarSpinnerEspacios(planta: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.instance.getEspacios()
                
                if (response.isSuccessful && response.body() != null) {
                    val listaEspacios = response.body()!!
                    Log.d("FormularioIncidenciaFragment", "Espacios recibidos: ${listaEspacios.size}")
                    
                    val espacios = when (planta) {
                        "0" -> listaEspacios.filter { it.planta.trim() == "0" }
                        "1" -> listaEspacios.filter { it.planta.trim() == "1" }
                        "2" -> listaEspacios.filter { it.planta.trim() == "2" }
                        else -> emptyList()
                    }
        
                    Log.d("FormularioIncidenciaFragment", "Espacios filtrados para planta $planta: ${espacios.size}")

                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, espacios)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerEspacions.adapter = adapter
                } else {
                    Log.e("FormularioIncidenciaFragment", "Error API espacios: ${response.code()}")
                    Toast.makeText(requireContext(), "Error al cargar espacios: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("FormularioIncidenciaFragment", "Error al cargar espacios: ${e.message}")
                Toast.makeText(requireContext(), "Fallo en conexión al cargar espacios", Toast.LENGTH_SHORT).show()
            }
        }
    }

}