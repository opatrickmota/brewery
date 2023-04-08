package com.patrickmota.brewery.view.fragments.searchresult

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.patrickmota.brewery.constants.Brewery
import com.patrickmota.brewery.data.remote.model.BreweryResponse
import com.patrickmota.brewery.databinding.FragmentSearchResultBinding
import com.patrickmota.brewery.view.activities.DetailActivity
import com.patrickmota.brewery.view.adapters.searchFragment.SearchFragmentAdapter

class SearchResultFragment(private val breweries: List<BreweryResponse>) : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var resultAdapter: SearchFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        val recyclerView = binding.fragmentSearchResultRecyclerView

        resultAdapter = SearchFragmentAdapter(breweries) { brewery ->
            Brewery.brewery = brewery
            startActivity(
                Intent(context, DetailActivity::class.java)
                    .putExtra("id", brewery.id)
            )
        }

        recyclerView.adapter = resultAdapter
    }
}