package com.example.pokemonapp.api

import android.util.Log
import com.example.pokemonapp.model.PokemonItem
import com.example.pokemonapp.model.PokemonListResponse
import retrofit2.Call
import retrofit2.Response

object PokemonRepository {
     var allPokemonList:List<PokemonItem> =ArrayList()
    private  set

    fun getallPokemonList():List<PokemonItem>{
        return allPokemonList
    }
    fun fetchAllPokemonList() {
        val data = ServiceBuilder.Apiservice.getPokemonList(100000, 0)
            val response = data.execute()//sent request to Api and block the main thread until received response
            if (!response.isSuccessful) {
                Log.d("FetchAllPokemonList", "Error: ${response.code()}")
                return
            }
            val pokemonList: PokemonListResponse? = response.body()
            if( pokemonList != null){
            val allPokemonList = pokemonList!!.results
            for (pokemonData in allPokemonList) {
                pokemonData.getColor() // Assign color using the getColor() function
            }
            this.allPokemonList = allPokemonList
            Log.d("FetchAllPokemonList", "Successful: ${response.code()}")
        }}



    fun fetchPokemonList(offset: Int, onResponse:(List<PokemonItem>)->Unit, onFailed:()->Unit) {

            val data = ServiceBuilder.Apiservice.getPokemonList(20, offset)
            data.enqueue(object : retrofit2.Callback<PokemonListResponse> {
                override fun onResponse(
                    call: Call<PokemonListResponse>,
                    response: Response<PokemonListResponse>
                ) {
                    if (!response.isSuccessful) {
                        onFailed()
                        return
                    }
                    val pokemonList: PokemonListResponse? = response.body()
                    assert(pokemonList != null)
                    for (pokemonData in pokemonList!!.results) {
                        for (pokemonItem in allPokemonList) {
                            if (pokemonData.name == pokemonItem.name) {
                                pokemonData.color = pokemonItem.color
                                break
                            }
                        }
                    }
                   // this@PokemonRepository.pokemonList.addAll(pokemonList.results)
                    onResponse(pokemonList.results)
                }
                override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                    onFailed()
                    return
                }
            })
    }
}
