package com.example.proyectosimagrow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectosimagrow.adapters.FilamentoAdapter
import com.example.proyectosimagrow.adapters.OnClickListener
import com.example.proyectosimagrow.databinding.FragmentFilamentosBinding
import com.example.proyectosimagrow.pojo.Filamento

class FilamentosFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentFilamentosBinding
    private lateinit var adapterFilamento: FilamentoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dividerItemDecoration: DividerItemDecoration
    private lateinit var listener: OnClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilamentosBinding.inflate(inflater, container, false)
        adapterFilamento = FilamentoAdapter(Filamento.DatosFilamentos.FILAMENTOS, this)
        linearLayoutManager = LinearLayoutManager(context)
        dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerViewFilamentos.apply {
            layoutManager = linearLayoutManager
            adapter = adapterFilamento
            addItemDecoration(dividerItemDecoration)
        }
        return binding.root
    }

    override fun onClick(filamento: Filamento) {
        Toast.makeText(null, "Has seleccionado un Filamento", Toast.LENGTH_SHORT).show()
    }

}