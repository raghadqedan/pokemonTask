package com.example.pokemonapp

import android.content.Intent
import android.os.Bundle
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
import kotlinx.coroutines.*


class PokemonListActivity : AppCompatActivity() {
    private var offset: Int= 0
    private var load=true
    private lateinit var binding:PokemonListActivityBinding
    lateinit var recyclerView: RecyclerView
    private lateinit var  layoutManager:LayoutManager
    private lateinit var  searchView: SearchView
    lateinit var adapter:PokemonListAdapter
    var color:Int=0

    private val pokemonsAdapter: PokemonListAdapter
    private val searchResultAdaper: PokemonListAdapter

    private val onScrollPositionChanged = { newPosition: Int ->
        Common.position = newPosition
        if (newPosition == PokemonRepository.getPokemonList().size - 1) {
            fetchPokemonList(offset)
        }
    }

    private val onPokemonClicked = { selectedPokemon: PokemonItem ->
        startPokemonActivity(selectedPokemon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        runBlocking {
            launch(Dispatchers.IO) {
                PokemonRepository.fetchAllPokemonList()
            }.join()
        }
        binding=PokemonListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView=binding.pokemonRecyclerView
        layoutManager=GridLayoutManager(this@PokemonListActivity,2)
        recyclerView.layoutManager=layoutManager
        recyclerView.setHasFixedSize(true)
        searchView=binding.searchView
        if(offset==0){

            fetchPokemonList(offset)
        }
        adapter=PokemonListAdapter(
            this@PokemonListActivity,
            PokemonRepository.getPokemonList(),
            { pokemonItem ->
                startPokemonActivity(pokemonItem)
            },
            onScrollPositionChanged
        )

        recyclerView.adapter =adapter
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (newText.length <=2) {
                        adapter = PokemonListAdapter(
                            this@PokemonListActivity,
                            PokemonRepository.getPokemonList(),
                            { pokemonItem ->
                                startPokemonActivity(pokemonItem)
                            },
                            onScrollPositionChanged
                        )
                        recyclerView.adapter = adapter
                        recyclerView.scrollToPosition(Common.position)

                        //TODO use a different adapter for the search result
                        recyclerView.swapAdapter(searchResultAdaper, true)

                    }else{
                        val  filteredList= PokemonRepository.getallPokemonList().filter { item ->
                            item.name.contains(newText, ignoreCase = true)
                        }
                        adapter = PokemonListAdapter(this@PokemonListActivity,
                            filteredList as MutableList<PokemonItem>,
                            { pokemonItem->
                                startPokemonActivity(pokemonItem)
                            },
                            onScrollPositionChanged
                        )
                        recyclerView.adapter = adapter
                    }
                    return true
                }
                return false
            }
        })

//        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if(dy>0){
//                    val visibleItemCount = layoutManager.childCount
//                    val totalItemCount = layoutManager.itemCount
//                    val pastVisibleItems = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
//                    val loadThreshold = 0 // Number of items remaining from the end to start loading more
//                    if (visibleItemCount + pastVisibleItems + loadThreshold >= totalItemCount) {
//                        if (load) {
//                            load = false
//                            offset += 20
//                            Common.position=pokemonRepository.getPokemonList().size-2
//                            fetchPokemonList(offset)
//                        }
//                    }
//                }
//            }
//        }
//     )
    }


    private fun  startPokemonActivity(pokemonItem:PokemonItem) {
        val   intent= Intent(this@PokemonListActivity,PokemonActivity::class.java)
        intent.apply {
            putExtra(Extra_POKEMON_NUMBER, pokemonItem.getNumber().toString())
            putExtra(EXTRA_POKEMON_NAME,pokemonItem.name)
            putExtra(Extra_POKEMON_CARD_COLOR,pokemonItem.color)}

        startActivity(intent)
    }
    private fun fetchPokemonList(offset: Int) {
        PokemonRepository.fetchPokemonList(offset,
            onResponse = { pokemonList ->
                adapter = PokemonListAdapter(
                    this@PokemonListActivity,
                    PokemonRepository.getPokemonList(),
                    onPokemonClicked,
                    onScrollPositionChanged
                )

                adapter.addPokemons(pokemonList)
                adapter.notifyItemRangeInserted(offset, offset + pokemonList.size)

                recyclerView.adapter = adapter
                recyclerView.scrollToPosition(Common.position)
                load = true

                this@PokemonListActivity.offset += pokemonList.size
            },
            onFailed = {
                Toast.makeText(
                    this@PokemonListActivity,
                    "Failed To Retrieve item ",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }


    companion object {
        const val  EXTRA_POKEMON_NAME = "ARG_POKEMON_NAME"
        const val  Extra_POKEMON_NUMBER = "ARG_POKEMON_NUMBER"
        const val  Extra_POKEMON_CARD_COLOR= "ARG_POKEMON_CARD_COLOR"
    }

}
