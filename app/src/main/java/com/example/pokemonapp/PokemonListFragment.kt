package com.example.pokemonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.pokemonapp.adapter.PokemonListAdapter
import com.example.pokemonapp.api.RetrofitBuilder
import com.example.pokemonapp.common.Common
import com.example.pokemonapp.databinding.PokemonListFragmentBinding
import com.example.pokemonapp.model.PokemonItem
import com.example.pokemonapp.model.PokemonListResponse
import retrofit2.Call
import retrofit2.Response


class PokemonListFragment : Fragment() {
   private var offset: Int= 0
   private var load=true
   private  var _binding:PokemonListFragmentBinding?=null
   private val binding get() =_binding!!
   lateinit var recyclerView: RecyclerView
   private lateinit var  layoutManager:LayoutManager
   private lateinit var  searchView: SearchView
   lateinit var adapter:PokemonListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Common.fetchAllPokemonList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
       _binding=PokemonListFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        recyclerView=binding.pokemonRecyclerView
        layoutManager=GridLayoutManager(requireContext(),2)
        recyclerView.layoutManager=layoutManager
        recyclerView.setHasFixedSize(true)
        searchView=view.findViewById(R.id.searchView)
        if(offset==0){
        fetchPokemonList(offset)}
        adapter=PokemonListAdapter(requireContext(), Common.PokemonList)
        recyclerView.adapter =adapter
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (newText.length <=2) {
                        adapter = PokemonListAdapter(requireContext(),Common.PokemonList)
                        recyclerView.adapter = adapter
                    }else{
                        Log.d("No", "no")
                        val filteredList = Common.AllPokemonList.filter { item ->
                            item.name.contains(newText, ignoreCase = true)
                        }
                        adapter = PokemonListAdapter(requireContext(), filteredList)
                        recyclerView.adapter = adapter
                    }
                    return true
                }
                return false
            }
        })

        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy>0){
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                val loadThreshold = 0 // Number of items remaining from the end to start loading more
                if (visibleItemCount + pastVisibleItems + loadThreshold >= totalItemCount) {
                    if (load) {
                        load = false
                        offset += 20
                        Log.d("ofsset",offset.toString())
                        Common.position=Common.PokemonList.size-2
                        fetchPokemonList(offset)
                        }
                    }
                 }
            }
        }
    )}

    private fun fetchPokemonList(offset: Int) {
        if(offset==0)
        { Common.PokemonList.clear()}
        val data = RetrofitBuilder.retrofitApiInterface.getPokemonList(20, offset)
        data.enqueue(object : retrofit2.Callback<PokemonListResponse> {
            override fun onResponse(
                call: Call<PokemonListResponse>,
                response: Response<PokemonListResponse>) {
                if (response.isSuccessful) {
                    val pokemonList: PokemonListResponse = response.body()!!
                    addItems(pokemonList.results)
                    adapter=PokemonListAdapter(requireContext(), Common.PokemonList)
                    recyclerView.adapter =adapter
                    recyclerView.scrollToPosition(Common.position)
                    }
                    load = true
            }
            override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                Toast.makeText(context, "Failed To Retrieve item ", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun addItems(results: MutableList<PokemonItem>){
        Common.PokemonList.addAll(results)
    }
}




