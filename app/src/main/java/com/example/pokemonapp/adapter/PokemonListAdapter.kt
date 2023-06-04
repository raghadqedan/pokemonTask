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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

import com.example.pokemonapp.R
import com.example.pokemonapp.common.Common
import com.example.pokemonapp.common.Common.PokemonCardColor
import com.example.pokemonapp.common.Common.PokemonName
import com.example.pokemonapp.common.Common.PokemonNumber
import com.example.pokemonapp.model.PokemonItem


class PokemonListAdapter(val context:Context,private val pokemonList:List<PokemonItem>):RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(val view: View):RecyclerView.ViewHolder(view) {
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
        val n=(0..13).random()
        var color=Common.color[n]
        holder.pokemonCard.setCardBackgroundColor(color)
        holder.pokemonCard.setOnClickListener {
            Log.d("PositionAdapter",position.toString())
            Common.position= position
            val bundle = Bundle()
            bundle.putString(PokemonNumber, pokemonList[position].getNumber().toString())
            bundle.putString(PokemonName,pokemonList[position].name)
            bundle.putInt(PokemonCardColor,n)
            holder.view.findNavController().navigate(R.id.action_pokemonListFragment_to_pokemonActivityFragment,bundle)

    }
}

    override fun getItemCount(): Int {
        return pokemonList.size
    }
}