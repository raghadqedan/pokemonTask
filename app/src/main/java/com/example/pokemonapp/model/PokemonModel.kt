package com.example.pokemonapp.model

data class PokemonListResponse(
    val results:MutableList<PokemonItem>)

class PokemonItem(
    val name:String,
    val url:String,)
{
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

