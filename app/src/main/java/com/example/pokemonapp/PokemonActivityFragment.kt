package com.example.pokemonapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pokemonapp.adapter.PokemonActivityAdapter
import com.example.pokemonapp.api.RetrofitBuilder
import com.example.pokemonapp.common.Common
import com.example.pokemonapp.databinding.PokemonActivityFragmentBinding
import com.example.pokemonapp.model.PokemonDetailsResponse
import retrofit2.Call
import retrofit2.Response


class PokemonActivityFragment : Fragment() {
    private var pokemonName: String? = null
    private var pokemonNumber: String?= null
    private var recyclerView: RecyclerView?=null
    private  var pokemonHeight:TextView?=null
    private var pokemonWeight:TextView?=null
    private  var pokemonImage: ImageView?=null
    private var pokemonCard:CardView?=null
    private lateinit var layoutManager:LayoutManager
    private var _binding:PokemonActivityFragmentBinding?=null
    private  val binding get()=_binding!!
    private var pokemonCardColor:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonNumber= arguments?.getString(Common.PokemonNumber)
        pokemonName=arguments?.getString(Common.PokemonName)
        pokemonCardColor= arguments?.getInt(Common.PokemonCardColor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=PokemonActivityFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //this line to show the appbar in this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        pokemonHeight=view.findViewById(R.id.pokemonHeight)
        pokemonWeight=view.findViewById(R.id.pokemonWeight)
        pokemonImage=view.findViewById(R.id.pokemonActivityImage)
        recyclerView=view.findViewById(R.id.activityRecyclerView)
        pokemonCard=view.findViewById(R.id.pokemonActivityCard)
        layoutManager= GridLayoutManager(requireContext(), 2)
        recyclerView?.layoutManager=layoutManager
        recyclerView?.setHasFixedSize(true)
        pokemonImage?.let {
            Glide.with(this ).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+pokemonNumber+".png")
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(it)
        }
       pokemonCard?.setCardBackgroundColor(Common.color[pokemonCardColor!!])
        //TODO weight,height from api
        fetchPokemonData()
    }

    private fun fetchPokemonData() {
        val data = RetrofitBuilder.retrofitApiInterface.getPokemonDetails(pokemonName.toString())
        data.enqueue(object : retrofit2.Callback<PokemonDetailsResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<PokemonDetailsResponse>,
                response: Response<PokemonDetailsResponse>){
                if (response.isSuccessful) {
                    val pokemonList: PokemonDetailsResponse = response.body()!!
                    recyclerView?.adapter= PokemonActivityAdapter(pokemonList.stats)
                     pokemonWeight?.text=pokemonList.weight.toString()+" kgs"
                    pokemonHeight?.text=pokemonList.height.toString()+" metres"
                }
            else{
                Toast.makeText(context, "Failed To Retrieve item ", Toast.LENGTH_LONG).show()}}

            override fun onFailure(call: Call<PokemonDetailsResponse>, t: Throwable) {
                Toast.makeText(context, "Failed To Retrieve item ", Toast.LENGTH_LONG).show()
            }
        })
    }
  }
