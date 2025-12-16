package com.example.proyectosimagrow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.databinding.FragmentIncidenciasBinding

class IncidenciasFragment : Fragment() {

    private lateinit var binding: FragmentIncidenciasBinding
    private var espacios = arrayOf("AULA", "PATIO", "BAÑOS", "HALL", "PABELLÓN")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncidenciasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            espacios
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEspacioIncidencia.adapter = adapter

        binding.btnBorrar.setOnClickListener {
            borrarDatos()
            Toast.makeText(requireContext(), "Has borrado los datos!!", Toast.LENGTH_SHORT).show()
        }

        binding.btnEnviarIncidencia.setOnClickListener {
            borrarDatos()
            Toast.makeText(requireContext(), "Has enviado una nueva Incidencia!!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun borrarDatos() {
        binding.etNombreIncidencia.text.clear()
        binding.etDescripcionIncidencias.text.clear()
    }

}
