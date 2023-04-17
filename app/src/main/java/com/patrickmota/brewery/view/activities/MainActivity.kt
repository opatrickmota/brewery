package com.patrickmota.brewery.view.activities

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.patrickmota.brewery.R
import com.patrickmota.brewery.common.hideSoftKeyboard
import com.patrickmota.brewery.databinding.ActivityMainBinding
import com.patrickmota.brewery.view.fragments.searchresult.SearchResultFragment
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Brewery)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            hideNoCityTyped()
            viewModel.loadBreweries(city)
            viewModel.breweries.observe(this@MainActivity) {
                when (it.status) {
                    ViewData.Status.LOADING -> {
                        showLoading()
                        hideError()
                    }
                    ViewData.Status.COMPLETE -> {
                        hideLoading()
                        hideError()

                        if (it.data?.isEmpty() == true) {
                            showEmptyState()
                        } else {
                            showFragmentContainer()

                            val fragmentManager: FragmentManager = supportFragmentManager
                            val fragmentTransaction: FragmentTransaction =
                                fragmentManager.beginTransaction()
                            fragmentTransaction.replace(
                                R.id.activity_main_search_results_fragment_container_view,
                                SearchResultFragment(breweries = it.data.orEmpty())
                            )
                                .commit()
                        }
                    }
                    ViewData.Status.ERROR -> {
                        showError()
                    }
                }
            }
        } else {
            showNoCityTyped()
        }
    }

    private fun showFragmentContainer() {
        hideEmptyState()
        hideNoCityTyped()
        hideError()
        binding.activityMainSearchResultsFragmentContainerView.visibility =
            View.VISIBLE
    }

    private fun hideFragmentContainer() {
        binding.activityMainSearchResultsFragmentContainerView.visibility =
            View.GONE
    }

    private fun showEmptyState() {
        hideFragmentContainer()
        hideNoCityTyped()
        hideError()
        binding.activityMainNoResultsTextView.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.activityMainNoResultsTextView.visibility = View.GONE
    }

    private fun showNoCityTyped() {
        hideEmptyState()
        hideFragmentContainer()
        hideError()
        binding.activityMainNoCityTypedTextView.visibility = View.VISIBLE
    }

    private fun hideNoCityTyped() {
        binding.activityMainNoCityTypedTextView.visibility = View.GONE
    }

    private fun showLoading() {
        hideError()
        hideNoCityTyped()
        hideEmptyState()
        hideFragmentContainer()
        binding.activityMainLoadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.activityMainLoadingProgressBar.visibility = View.GONE
    }

    private fun showError() {
        hideNoCityTyped()
        hideEmptyState()
        hideFragmentContainer()
        hideLoading()
        binding.activityMainErrorTextView.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.activityMainErrorTextView.visibility = View.GONE
    }
}