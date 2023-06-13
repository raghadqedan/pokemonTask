package com.example.pokemonapp.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


import com.example.pokemonapp.R
import com.example.pokemonapp.common.Common
import com.example.pokemonapp.model.PokemonItem
import kotlin.reflect.typeOf


class PokemonListAdapter(private val context:Context, private val pokemonList:List<PokemonItem>, private val onPokemonItemClicked: (PokemonItem) -> Unit):RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(private val view: View):RecyclerView.ViewHolder(view) {
        val pokemonName:TextView=view.findViewById(R.id.pokemonName)
        val pokemonImage:ImageView=view.findViewById(R.id.pokemonImage)
        val pokemonCard:CardView=view.findViewById(R.id.pokemonCard)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.pokemon_item,parent,false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.pokemonName.text=pokemonList[position].name

        Glide.with(context).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+pokemonList[position].getNumber()+".png")
           .centerCrop()
           .transition(DrawableTransitionOptions.withCrossFade())
           .into(holder.pokemonImage)
       var color=pokemonList[position].color
        holder.pokemonCard.setCardBackgroundColor(color)
        holder.pokemonCard.setOnClickListener {
            Common.position= position
            Log.d("position1",Common.position.toString())
            onPokemonItemClicked.invoke(pokemonList[position])

    }
}

    override fun getItemCount(): Int {
        return pokemonList.size
    }
}