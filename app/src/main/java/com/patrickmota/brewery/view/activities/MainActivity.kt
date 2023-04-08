package com.patrickmota.brewery.view.activities

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.patrickmota.brewery.R
import com.patrickmota.brewery.databinding.ActivityMainBinding
import com.patrickmota.brewery.utils.hideSoftKeyboard
import com.patrickmota.brewery.view.fragments.searchresult.SearchResultFragment
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply { hide() }

        setupObservers()
    }

    private fun setupObservers() {
        binding.activityMainSearchEditTextInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                setupBreweriesObserve()
            }
            false
        }
    }

    private fun setupBreweriesObserve() {
        val city = binding.activityMainSearchEditTextInputEditText.text.toString()

        if (city.isNotBlank()) {
            viewModel.loadBreweries(city)
            viewModel.breweries.observe(this@MainActivity) {
                when (it.status) {
                    ViewData.Status.COMPLETE -> {
                        binding.activityMainSearchResultsFragmentContainerView.visibility =
                            View.VISIBLE

                        val fragmentManager: FragmentManager = supportFragmentManager
                        val fragmentTransaction: FragmentTransaction =
                            fragmentManager.beginTransaction()
                        fragmentTransaction.replace(
                            R.id.activity_main_search_results_fragment_container_view,
                            SearchResultFragment(breweries = it.data.orEmpty())
                        )
                            .commit()
                    }
                    else -> Unit
                }
            }
        }
    }
}