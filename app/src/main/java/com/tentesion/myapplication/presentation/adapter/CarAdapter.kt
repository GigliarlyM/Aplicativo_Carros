package com.tentesion.myapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tentesion.myapplication.R
import com.tentesion.myapplication.domain.Carro

class CarAdapter(
    private val carros: List<Carro>,
    private val isFavoriteScreen: Boolean = false
) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    var carItemLister: (Carro) -> Unit = {}

    // Cria uma nova view, inflando ela com dados
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item, parent, false)

        return ViewHolder(view)
    }

    // Define em cada um dos textVieww criados, com o conteudo que está em carros
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.preco.text = carros[position].preco
        holder.bateria.text = carros[position].bateria
        holder.potencia.text = carros[position].potencia
        holder.recarga.text = carros[position].recarga
        if (isFavoriteScreen) {
            holder.favorito.setImageResource(R.drawable.ic_star_select)
        }
        holder.favorito.setOnClickListener {
            val carro = carros[position]
            carItemLister(carro)
            setupFavorite(carro, holder)
        }
    }

    private fun setupFavorite(
        carro: Carro,
        holder: ViewHolder
    ) {

        carro.isFavorite = !carro.isFavorite
        if (carro.isFavorite)
            holder.favorito.setImageResource(R.drawable.ic_star_select)
        else
            holder.favorito.setImageResource(R.drawable.ic_star)


    }

    // Pega a quantidade de strings que tem em carros, consequentimente mostra tambem
    // a quantidade de views que tem no recylcer view
    override fun getItemCount(): Int = carros.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val preco: TextView
        val bateria: TextView
        val potencia: TextView
        val recarga: TextView
        val favorito: ImageView

        init {
            view.apply {
                preco = findViewById(R.id.tv_preco_value)
                bateria = findViewById(R.id.tv_bateria_value)
                potencia = findViewById(R.id.tv_potencia_value)
                recarga = findViewById(R.id.tv_recarga_value)
                favorito = findViewById(R.id.iv_favorite)
            }
        }

    }
}
