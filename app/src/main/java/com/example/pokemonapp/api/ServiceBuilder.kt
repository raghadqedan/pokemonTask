package com.example.pokemonapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
        const val  BASE_URL="https://pokeapi.co/api/v2/"
        val Apiservice = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build().create(pokemonApis::class.java)

}