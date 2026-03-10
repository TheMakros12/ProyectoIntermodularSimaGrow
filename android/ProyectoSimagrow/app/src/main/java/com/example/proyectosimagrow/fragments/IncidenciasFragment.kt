package com.example.proyectosimagrow.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectosimagrow.adapters.IncidenciaAdapter
import com.example.proyectosimagrow.databinding.FragmentIncidenciasBinding
import com.example.proyectosimagrow.activities.MainActivity
import com.example.proyectosimagrow.api.RetrofitInstance
import com.example.proyectosimagrow.data.Incidencia
import kotlinx.coroutines.launch
import android.text.SpannableString
import android.text.style.AlignmentSpan

class IncidenciasFragment : Fragment() {

    private lateinit var binding: FragmentIncidenciasBinding
    private lateinit var incidenciaAdapter: IncidenciaAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dividerDecoration: DividerItemDecoration
    private val estados = arrayOf("Todas", "Resueltas", "No resueltas")
    private var todasLasIncidencias: List<Incidencia> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncidenciasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarRecyclerView()
        
        lifecycleScope.launch {
            try {
                val user = (activity as? MainActivity)?.activeUser
                if (user == null) {
                    Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val response = RetrofitInstance.instance.getIncidencias()
                if (response.isSuccessful && response.body() != null) {
                    todasLasIncidencias = response.body()!!.filter { it.usuarioId == user.id }
                    
                    if (todasLasIncidencias.isEmpty()) {
                        binding.tvDatosIncidencias.visibility = View.VISIBLE
                    } else {
                        binding.tvDatosIncidencias.visibility = View.GONE
                    }
                    
                    cargarSpinner()
                    filtrarIncidencias(estados[binding.spinnerEstadosIncidencias.selectedItemPosition])
                } else {
                    Toast.makeText(requireContext(), "Error al obtener incidencias", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Error al cargar las incidencias: ${e.message}")
            }
        }
    }

    private fun cargarSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEstadosIncidencias.adapter = adapter

        binding.spinnerEstadosIncidencias.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    filtrarIncidencias(estados[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun cargarRecyclerView() {
        incidenciaAdapter = IncidenciaAdapter(emptyList()) { incidencia -> mostrarDetalleIncidencia(incidencia) }
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        dividerDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recyclerViewIncidencias.apply {
            adapter = incidenciaAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(dividerDecoration)
        }
    }

    private fun mostrarDetalleIncidencia(incidencia: Incidencia) {
        val titulo = SpannableString("Detalle de la IncidenciaResponse")
        titulo.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, titulo.length, 0)

        val estadoTexto = if (incidencia.resuelta) "Resuelta" else "Pendiente"

        val mensaje = """
            Id Incidenida:
            ${incidencia.id}
            
            Título:
            ${incidencia.titulo}
            
            Descripción:
            ${incidencia.descripcion}
            
            Fecha de creación:
            ${incidencia.fechaIncidencia}
            
            Estado: $estadoTexto
        """.trimIndent()

        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Eliminar") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.instance.eliminarIncidencia(incidencia.id)
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Incidencia eliminada", Toast.LENGTH_SHORT).show()
                            todasLasIncidencias = todasLasIncidencias.filter { it.id != incidencia.id }
                            filtrarIncidencias(estados[binding.spinnerEstadosIncidencias.selectedItemPosition])
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar incidencia", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("IncidenciasFragment", "Error API Detele: ${e.message}")
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Editar") { dialog, _ ->
                val fragment = FormularioIncidenciaFragment().apply {
                    arguments = Bundle().apply {
                        putInt("id", incidencia.id)
                        putString("nombre", incidencia.titulo)
                        putString("descripcion", incidencia.descripcion)
                    }
                }
                (activity as? MainActivity)?.replaceFragment(fragment)
                dialog.dismiss()
            }
            .show()
    }

    private fun filtrarIncidencias(estado: String) {

        val listaFiltrada = when (estado) {
            "Resueltas" -> todasLasIncidencias.filter { it.resuelta }
            "No resueltas" -> todasLasIncidencias.filter { !it.resuelta }
            else -> todasLasIncidencias
        }

        incidenciaAdapter.actualizarLista(listaFiltrada)
    }

}
