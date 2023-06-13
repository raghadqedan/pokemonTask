package com.example.pokemonapp.model


import android.util.Log
import com.example.pokemonapp.common.Common

data class PokemonListResponse(
    val results:List<PokemonItem>)

 data class PokemonItem(
    val name:String,
    val url:String,
    var color:Int

   )
{

    @JvmName("getColor1")
    fun getColor():Int{
       this.color=Common.color.random()
       return color
    }

    fun getNumber():Int {
         val array =this.url.split("/")
         var  number:Int = Integer.parseInt(array[array.size - 2])
         return number
     }
 }

data class PokemonDetailsResponse(
    val name: String,
    val stats:List<Stat>,
    val weight:Double,
    val height:Double)

data class Stat(
    val stat:StatDetails,
    val base_stat:Int,
    val effort:Int)

data class StatDetails (
    val name:String,
    val url:String)

