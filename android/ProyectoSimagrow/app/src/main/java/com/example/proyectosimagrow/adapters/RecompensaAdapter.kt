package com.example.proyectosimagrow.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.data.RecompensaResponse
import com.example.proyectosimagrow.databinding.ItemRecompensaBinding
import com.squareup.picasso.Picasso

class RecompensaAdapter(
    private var recompensaResponses: List<RecompensaResponse>,
    private val onCanjearClick: (RecompensaResponse) -> Unit
) : RecyclerView.Adapter<RecompensaAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_recompensa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recompensa = recompensaResponses[position]
        with(holder) {
            binding.tvNombreRecompensa.text = recompensa.nombre
            binding.tvPrecioRecompensa.text = recompensa.tokens.toString()
            cargarImagen(recompensa.imagen, holder)
            binding.btnCanjearRecompensa.setOnClickListener {
                onCanjearClick(recompensa)
            }
        }
    }

    private fun cargarImagen(imageString: String?, holder: ViewHolder) {
        if (imageString.isNullOrEmpty()) {
            holder.binding.ivRecompensa.setImageResource(R.mipmap.ic_launcher)
            return
        }

        when {
            imageString.startsWith("http://") || imageString.startsWith("https://") -> {
                Picasso.get()
                    .load(imageString)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.binding.ivRecompensa)
            }

            imageString.startsWith("[B@") -> {
                Log.e("IMAGEN", "El backend devuelve referencia de memoria. Corrige el backend.")
                holder.binding.ivRecompensa.setImageResource(R.mipmap.ic_launcher)
            }

            else -> {
                try {
                    val base64Data = if (imageString.contains(",")) {
                        imageString.substringAfter(",")
                    } else {
                        imageString
                    }

                    val base64Clean = base64Data
                        .replace("\n", "")
                        .replace("\r", "")
                        .replace(" ", "")
                        .trim()

                    val firstDecode = Base64.decode(base64Clean, Base64.DEFAULT or Base64.NO_WRAP)

                    val secondBase64 = String(firstDecode, Charsets.UTF_8)
                        .replace("\n", "")
                        .replace("\r", "")
                        .replace(" ", "")
                        .trim()

                    Log.d("IMAGEN", "Segunda capa Base64 primeros 20: ${secondBase64.take(20)}")

                    val imageBytes = Base64.decode(secondBase64, Base64.DEFAULT or Base64.NO_WRAP)

                    Log.d("IMAGEN", "Bytes imagen real: ${imageBytes.size}")

                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    if (bitmap != null) {
                        Log.d("IMAGEN", "Bitmap OK: ${bitmap.width}x${bitmap.height}")
                        holder.binding.ivRecompensa.setImageBitmap(bitmap)
                    } else {
                        Log.e("IMAGEN", "Bitmap null tras doble decodificación")
                        holder.binding.ivRecompensa.setImageResource(R.mipmap.ic_launcher)
                    }

                } catch (e: Exception) {
                    Log.e("IMAGEN", "Error: ${e.message}")
                    holder.binding.ivRecompensa.setImageResource(R.mipmap.ic_launcher)
                }
            }
        }
    }

    override fun getItemCount(): Int = recompensaResponses.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecompensaBinding.bind(view)
    }

    fun actualizarLista(nuevaLista: List<RecompensaResponse>) {
        recompensaResponses = nuevaLista
        notifyDataSetChanged()
    }
}
