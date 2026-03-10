package com.example.proyectosimagrow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val mainActivity = activity as? com.example.proyectosimagrow.activities.MainActivity
        val user = mainActivity?.activeUser

        user?.let {
            binding.tvNiaAlumno.text = it.nif
            binding.tvNombreAlumno.text = "${it.nombre} ${it.apellidos ?: ""}".trim()
            binding.tvCorreoAlumno.text = it.correo
        }
    }

}