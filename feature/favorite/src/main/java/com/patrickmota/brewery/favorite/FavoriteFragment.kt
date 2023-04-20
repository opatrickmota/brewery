package com.patrickmota.brewery.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.patrickmota.brewery.core.data.models.toBreweryResponse
import com.patrickmota.brewery.favorite.databinding.FragmentFavoriteBinding
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        setupNavigationIconListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupBreweriesObserver()
    }

    private fun setupNavigationIconListener() {
        binding.fragmentFavoriteHeaderMaterialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupBreweriesObserver() {
        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()

        favoriteViewModel.loadFavoriteBreweries()
        favoriteViewModel.breweries.observe(requireActivity()) {
            when (it.status) {
                ViewData.Status.COMPLETE -> {
                    if (it.data?.isEmpty() == true) {
                        showEmptyState()
                    } else {
                        showFragmentContainer()

                        val breweries = it.data?.map { brewery ->
                            brewery.toBreweryResponse()
                        }

                        if (fragmentTransaction.isEmpty) {
                            fragmentTransaction.replace(
                                R.id.fragment_favorite_breweries_fragment_container_view,
                                FavoriteBreweriesFragment(breweries = breweries.orEmpty())
                            ).commit()
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    private fun showFragmentContainer() {
        hideEmptyState()
        binding.fragmentFavoriteBreweriesFragmentContainerView.visibility =
            View.VISIBLE
    }

    private fun hideFragmentContainer() {
        binding.fragmentFavoriteBreweriesFragmentContainerView.visibility =
            View.GONE
    }

    private fun showEmptyState() {
        hideFragmentContainer()
        binding.fragmentFavoriteNoFavoritesResultsTextView.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.fragmentFavoriteNoFavoritesResultsTextView.visibility = View.GONE
    }
}