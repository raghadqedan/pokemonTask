package com.example.pokemonapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pokemonapp.adapter.PokemonActivityAdapter
import com.example.pokemonapp.api.ServiceBuilder
import com.example.pokemonapp.common.Common
import com.example.pokemonapp.databinding.PokemonActivityBinding
import com.example.pokemonapp.model.PokemonDetailsResponse
import retrofit2.Call
import retrofit2.Response


class PokemonActivity : AppCompatActivity() {
    private var pokemonName: String? = null
    private var pokemonNumber: String?= null
    private var recyclerView: RecyclerView?=null
    private  var pokemonHeight:TextView?=null
    private var pokemonWeight:TextView?=null
    private  var pokemonImage: ImageView?=null
    private var pokemonCard:CardView?=null
    private lateinit var layoutManager:LayoutManager
    private lateinit var binding:PokemonActivityBinding
    private var pokemonCardColor:Int?=null
    private var toolbar:Toolbar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= PokemonActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        pokemonCardColor= intent.getIntExtra("ARG_POKEMON_CARD_COLOR",0)
        pokemonNumber= intent.getStringExtra("ARG_POKEMON_NUMBER")
        pokemonName=intent.getStringExtra("ARG_POKEMON_NAME")
        supportActionBar?.title = pokemonName
        toolbar?.setTitleTextColor( Color.rgb(250, 250, 250))
        toolbar?.setBackgroundColor(pokemonCardColor!!)
        initializeViews()
        fetchPokemonData()
    }

    private fun initializeViews() {
        pokemonHeight=findViewById(R.id.pokemonHeight)
        pokemonWeight=findViewById(R.id.pokemonWeight)
        pokemonImage=findViewById(R.id.pokemonActivityImage)
        recyclerView=findViewById(R.id.activityRecyclerView)
        pokemonCard=findViewById(R.id.pokemonActivityCard)
        layoutManager= GridLayoutManager(this, 2)
        recyclerView?.layoutManager=layoutManager
        recyclerView?.setHasFixedSize(true)
        pokemonImage?.let {
            Glide.with(this ).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+pokemonNumber+".png")
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(it)
        }
       // pokemonCard?.setCardBackgroundColor(pokemonCardColor!!)
        pokemonCard?.setCardBackgroundColor(pokemonCardColor!!)
    }

    private fun fetchPokemonData() {
        val data = ServiceBuilder.Apiservice.getPokemonDetails(pokemonName.toString())
        data.enqueue(object : retrofit2.Callback<PokemonDetailsResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<PokemonDetailsResponse>,
                response: Response<PokemonDetailsResponse>){
                if (response.isSuccessful) {
                    val pokemonList: PokemonDetailsResponse = response.body()!!
                    recyclerView?.adapter= PokemonActivityAdapter(pokemonList.stats)
                    pokemonWeight?.text=pokemonList.weight.toString()+" kgs"
                    pokemonHeight?.text=pokemonList.height.toString()+" metres"
                }
                else{
                    Toast.makeText(this@PokemonActivity, "Failed To Retrieve item ", Toast.LENGTH_LONG).show()}}

            override fun onFailure(call: Call<PokemonDetailsResponse>, t: Throwable) {
                Toast.makeText(this@PokemonActivity, "Failed To Retrieve item ", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
               finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}
