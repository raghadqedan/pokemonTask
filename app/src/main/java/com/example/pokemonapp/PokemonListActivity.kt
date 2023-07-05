package com.example.pokemonapp

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.pokemonapp.adapter.PokemonListAdapter
import com.example.pokemonapp.api.PokemonRepository
import com.example.pokemonapp.common.Common
import com.example.pokemonapp.databinding.PokemonListActivityBinding
import com.example.pokemonapp.model.PokemonItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class PokemonListActivity : AppCompatActivity() {
    private var offset: Int= 0
    private var previousPosition: Int = 0
    private lateinit var binding:PokemonListActivityBinding
    lateinit var recyclerView: RecyclerView
    private lateinit var  layoutManager:LayoutManager
    private lateinit var  searchView: SearchView
    private  lateinit var pokemonAdapter:PokemonListAdapter
    private lateinit var searchPokemonadapter:PokemonListAdapter
    var pokemonList:ArrayList<PokemonItem> =ArrayList()
    var color:Int=0
    var lock=true
    private val selectedPokemonItem={selectedItem:PokemonItem ->
        startPokemonActivity(selectedItem)}
    private val onScrollPositionChanged={newPosition:Int->
        Common.position=newPosition
        if(newPosition==pokemonList.size-1){
            offset+=20
            fetchPokemonList(offset)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        runBlocking {
            launch(Dispatchers.IO) {
                PokemonRepository.fetchAllPokemonList()
            }.join()
        }
        binding = PokemonListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.pokemonRecyclerView
        layoutManager = GridLayoutManager(this@PokemonListActivity, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        searchView = binding.searchView
        if (offset == 0) {
            fetchPokemonList(offset)
        }
        pokemonAdapter = PokemonListAdapter(
            this@PokemonListActivity,
            pokemonList,
            selectedPokemonItem,
            onScrollPositionChanged

        )
        recyclerView.adapter = pokemonAdapter
        previousPosition=Common.position
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (newText.length <= 2) {
                        Log.d("pBefore", Common.position.toString())
                        recyclerView.swapAdapter(pokemonAdapter,true)
                        Log.d("pAfter", Common.position.toString())
                        recyclerView.scrollToPosition(previousPosition)
                    } else {
                        if(newText.length==3){
                        previousPosition= Common.position}
                        Log.d("PreviousPosition",previousPosition.toString())
                        val filteredList = PokemonRepository.allPokemonList.filter { item ->
                            item.name.contains(newText, ignoreCase = true)
                        }
                        if (lock) {
                            searchPokemonadapter = PokemonListAdapter(
                                this@PokemonListActivity,
                                filteredList as ArrayList<PokemonItem>,
                                selectedPokemonItem,
                                onScrollPositionChanged
                            )
                            lock = false
                            recyclerView.swapAdapter(searchPokemonadapter, true)
                        } else {
                            //previousPosition=Common.position
                            recyclerView.swapAdapter(searchPokemonadapter, true)
                            searchPokemonadapter.setPokemons(filteredList as ArrayList<PokemonItem>)
                            searchPokemonadapter.notifyDataSetChanged()
                        }

                    }

                    return true
                }
                return false
            }
        })
    }

    private fun  startPokemonActivity(pokemonItem:PokemonItem) {
        val   intent= Intent(this@PokemonListActivity,PokemonActivity::class.java)
        intent.apply {
            putExtra(Extra_POKEMON_NUMBER, pokemonItem.getNumber().toString())
            putExtra(EXTRA_POKEMON_NAME,pokemonItem.name)
            putExtra(Extra_POKEMON_CARD_COLOR,pokemonItem.color)}

        startActivity(intent)
    }
    private fun fetchPokemonList(offset: Int){
        PokemonRepository.fetchPokemonList(offset,
                onResponse={pokemonList->
                  pokemonAdapter.addPokemons(pokemonList)
                  pokemonAdapter.notifyItemRangeInserted(Common.position+1,pokemonList.size-1)
                    },
                onFailed={Toast.makeText(this@PokemonListActivity,
                          "Failed To Retrieve item ",
                          Toast.LENGTH_LONG).show()}
            )
        }
    companion object {
        const val  EXTRA_POKEMON_NAME = "ARG_POKEMON_NAME"
        const val  Extra_POKEMON_NUMBER = "ARG_POKEMON_NUMBER"
        const val  Extra_POKEMON_CARD_COLOR= "ARG_POKEMON_CARD_COLOR"
    }

}
