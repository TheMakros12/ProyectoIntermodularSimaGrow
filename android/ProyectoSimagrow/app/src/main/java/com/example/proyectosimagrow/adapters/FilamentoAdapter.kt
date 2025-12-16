package com.example.proyectosimagrow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.databinding.ItemFilamentoBinding
import com.example.proyectosimagrow.pojo.Filamento
import java.lang.System.load

class FilamentoAdapter(private val filamentos: Array<Filamento>, private val listener: OnClickListener): RecyclerView.Adapter<FilamentoAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_filamento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val filamento = filamentos[position]
        with(holder) {
            itemView.setOnClickListener {
                listener.onClick(filamento)
            }
            binding.tvNombreFilamento.text = filamento.nombre
            binding.tvPrecioFilamento.text = filamento.precio.toString()
            binding.tvCategoriaFilamento.text = filamento.categoria.categoria
            /*Glide.with(context)
                .load(filamento.imagen)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.ivFilamento)*/
        }
    }

    override fun getItemCount(): Int = filamentos.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemFilamentoBinding.bind(view)
        fun setListener(filamento: Filamento) {
            binding.root.setOnClickListener {
                listener.onClick(filamento)
            }
        }
    }
}