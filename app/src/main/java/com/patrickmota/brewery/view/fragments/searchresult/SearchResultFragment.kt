package com.patrickmota.brewery.view.fragments.searchresult

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.patrickmota.brewery.constants.Brewery
import com.patrickmota.brewery.data.local.model.BreweryModel
import com.patrickmota.brewery.data.remote.model.BreweryResponse
import com.patrickmota.brewery.databinding.FragmentSearchResultBinding
import com.patrickmota.brewery.view.activities.DetailActivity
import com.patrickmota.brewery.view.adapters.searchFragment.SearchFragmentAdapter
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchResultFragment(private val breweries: List<BreweryResponse>) : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var resultAdapter: SearchFragmentAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private lateinit var favoritesBreweries: ArrayList<BreweryModel>
    private var state: Parcelable? = null

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

    override fun onPause() {
        super.onPause()
        saveScrollState()
    }

    override fun onResume() {
        super.onResume()
        setupUi()
    }

    private fun saveScrollState() {
        state = binding.fragmentSearchResultRecyclerView
            .layoutManager
            ?.onSaveInstanceState()
    }

    private fun restoreScrollState() {
        state?.let { previousState ->
            binding.fragmentSearchResultRecyclerView
                .layoutManager
                ?.onRestoreInstanceState(previousState)
        }
    }

    private fun setupUi() {
        favoriteViewModel.loadFavoriteBreweries()
        favoriteViewModel.breweries.observe(requireActivity()) {
            when (it.status) {
                ViewData.Status.COMPLETE -> {
                    favoritesBreweries = ArrayList(it.data ?: emptyList())
                    setupAdapter()
                    restoreScrollState()
                }
                else -> Unit
            }
        }
    }

    private fun setupAdapter() {
        val recyclerView = binding.fragmentSearchResultRecyclerView

        resultAdapter = SearchFragmentAdapter(
            breweries,
            onItemClicked = { brewery ->
                Brewery.brewery = brewery
                startActivity(
                    Intent(context, DetailActivity::class.java)
                        .putExtra("id", brewery.id)
                )
            },
            onFavoriteClicked = {
                /* TODO */
            }
        )

        resultAdapter.favoriteBreweries = favoritesBreweries

        recyclerView.adapter = resultAdapter
    }
}