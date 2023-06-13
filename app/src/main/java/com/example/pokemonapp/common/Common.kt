package com.example.pokemonapp.common


import android.graphics.Color
import android.util.Log
import com.example.pokemonapp.api.RetrofitBuilder
import com.example.pokemonapp.model.PokemonItem
import com.example.pokemonapp.model.PokemonListResponse


object Common {
    var PokemonList:MutableList<PokemonItem> =ArrayList()
    var AllPokemonList:List<PokemonItem> =ArrayList()
    var position:Int=0
    val color= arrayOf(
        Color.rgb(208, 156, 250),
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
        Color.rgb(249, 123, 34)
    )

     fun fetchAllPokemonList() {
        val data = RetrofitBuilder.retrofitApiInterface.getPokemonList(100000, 0)
        try {
            val response = data.execute()//sent request to Api and block the main thread until received response
            if (!response.isSuccessful) {
                Log.d("FetchAllPokemonList", "Error: ${response.code()}")
                return
                }
            val pokemonList: PokemonListResponse? = response.body()
            assert( pokemonList != null)
            val allPokemonList = pokemonList!!.results
            for (pokemonData in allPokemonList) {
                pokemonData.getColor() // Assign color using the getColor() function
            }
            AllPokemonList = allPokemonList
            Log.d("FetchAllPokemonList", "Successful: ${response.code()}")
        } catch (e: java.lang.AssertionError) {
            Log.e("FetchAllPokemonList", "Exception: ${e.message}")
        }
    }

}
