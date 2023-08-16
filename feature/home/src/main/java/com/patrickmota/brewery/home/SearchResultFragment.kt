package com.patrickmota.brewery.home

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.patrickmota.brewery.common.NavigationHelper.DETAIL_DEEP_LINK
import com.patrickmota.brewery.core.data.models.Brewery
import com.patrickmota.brewery.core.data.models.BreweryModel
import com.patrickmota.brewery.core.data.models.BreweryResponse
import com.patrickmota.brewery.home.databinding.FragmentSearchResultBinding
import com.patrickmota.brewery.viewmodel.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchResultFragment(private val breweries: List<BreweryResponse>) : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var resultAdapter: SearchFragmentAdapter
    private val favoriteViewModel: FavoriteViewModel by sharedViewModel()
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
                com.patrickmota.brewery.viewmodel.ViewData.Status.COMPLETE -> {
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

                val destination = Uri.parse(DETAIL_DEEP_LINK + brewery.id)
                findNavController().navigate(destination)

            },
            onFavoriteClicked = {
                /* TODO */
            }
        )

        resultAdapter.favoriteBreweries = favoritesBreweries

        recyclerView.adapter = resultAdapter
    }
}