package com.example.proyectosimagrow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.data.Incidencia
import com.example.proyectosimagrow.databinding.ItemIncidenciaBinding

class IncidenciaAdapter(private var incidencias: List<Incidencia>,
                        private val onItemClick: (Incidencia) -> Unit) : RecyclerView.Adapter<IncidenciaAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_incidencia, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val incidencia = incidencias[position]

        with(holder) {
            binding.tvIdIncidencia.text = incidencia.id.toString()
            binding.tvNombreIncidencia.text = incidencia.titulo
            binding.tvEstadoIncidencia.text = incidencia.resuelta.toString()

            itemView.setOnClickListener {
                onItemClick(incidencia)
            }
        }
    }

    override fun getItemCount(): Int = incidencias.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemIncidenciaBinding.bind(view)
    }

    fun actualizarLista(nuevaLista: List<Incidencia>) {
        incidencias = nuevaLista
        notifyDataSetChanged()
    }

}