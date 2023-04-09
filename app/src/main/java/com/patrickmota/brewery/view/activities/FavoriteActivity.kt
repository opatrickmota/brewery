package com.patrickmota.brewery.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.patrickmota.brewery.R
import com.patrickmota.brewery.data.local.model.toBreweryResponse
import com.patrickmota.brewery.databinding.ActivityFavoriteBinding
import com.patrickmota.brewery.view.fragments.searchresult.SearchResultFragment
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationIconListener()
    }

    override fun onResume() {
        super.onResume()
        setupBreweriesObserver()
    }

    private fun setupNavigationIconListener() {
        binding.activityFavoriteHeaderMaterialToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBreweriesObserver() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()

        favoriteViewModel.loadFavoriteBreweries()
        favoriteViewModel.breweries.observe(this@FavoriteActivity) {
            when (it.status) {
                ViewData.Status.COMPLETE -> {
                    binding.activityFavoriteBreweriesFragmentContainerView.visibility = View.VISIBLE

                    val breweries = it.data?.map { brewery ->
                        brewery.toBreweryResponse()
                    }

                    if (fragmentTransaction.isEmpty) {
                        fragmentTransaction.replace(
                            R.id.activity_favorite_breweries_fragment_container_view,
                            SearchResultFragment(breweries = breweries.orEmpty())
                        ).commit()
                    }
                }
                else -> Unit
            }
        }
    }
}