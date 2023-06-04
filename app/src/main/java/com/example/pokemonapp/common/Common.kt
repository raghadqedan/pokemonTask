package com.example.pokemonapp.common

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.example.pokemonapp.api.RetrofitBuilder
import com.example.pokemonapp.model.PokemonItem
import com.example.pokemonapp.model.PokemonListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Common {
    var PokemonList:MutableList<PokemonItem> =ArrayList()
    var AllPokemonList:MutableList<PokemonItem> =ArrayList()
    var position:Int=0
    val PokemonNumber = "pokemonNumber"
    val PokemonName = "pokemonName"
    val PokemonCardColor="pokemonCardColor"
    val color= arrayOf(
        Color.rgb(249, 123, 34),
        Color.rgb(254, 232, 176),
        Color.rgb(156, 167, 119),
        Color.rgb(249, 123, 34),
        Color.rgb(95, 38, 74),
        Color.rgb(176, 164, 164),
        Color.rgb(255, 109, 96),
        Color.rgb(152, 216, 170),
        Color.rgb(158, 111, 33),
        Color.rgb(223, 46, 56),
        Color.rgb(166, 35, 73),
        Color.rgb(255, 203, 66),
        Color.rgb(221, 74, 72),
        Color.rgb(0, 0, 0),
        Color.rgb(238, 238, 238),
        Color.rgb(109, 93, 110),
        Color.rgb(205, 88, 136),
        Color.rgb(192, 74, 130),
        Color.rgb(208, 156, 250)
    )

   fun  fetchAllPokemonList(){
       var data=RetrofitBuilder.retrofitApiInterface.getPokemonList(100000,0)
       data.enqueue(object:Callback<PokemonListResponse>{
           override fun onResponse(
               call: Call<PokemonListResponse>,
               response: Response<PokemonListResponse>) {
               if(response.isSuccessful) {
                  AllPokemonList= response.body()!!.results
                   Log.d("successful","successful")
                 }
               }

           override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
               Log.d("Failed","Failed")
           }
       })
   }
}