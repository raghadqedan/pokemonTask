package com.example.pokemonapp.api


import com.example.pokemonapp.model.PokemonDetailsResponse
import com.example.pokemonapp.model.PokemonListResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface pokemonApis{
       @GET("pokemon/")
        fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int):Call<PokemonListResponse>

        @GET("pokemon/{name}")
        fun  getPokemonDetails(@Path("name") name:String): Call<PokemonDetailsResponse>

}
