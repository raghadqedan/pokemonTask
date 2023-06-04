package com.example.pokemonapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.R
import com.example.pokemonapp.model.Stat


class PokemonActivityAdapter( private val pokemonStats:List<Stat>):RecyclerView.Adapter<PokemonActivityAdapter.PokemonActivityViewHolder>() {
     class PokemonActivityViewHolder(view:View):RecyclerView.ViewHolder(view) {
       val baseStat:TextView=view.findViewById(R.id.baseState)
       val statName:TextView=view.findViewById(R.id.statName)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonActivityViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.activity_item,parent,false)
        return PokemonActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonActivityViewHolder, position: Int) {
        holder.baseStat.text = pokemonStats[position].base_stat.toString()
        holder.statName.text = pokemonStats[position].stat.name
    }
    override fun getItemCount(): Int {
        return pokemonStats.size
    }

}