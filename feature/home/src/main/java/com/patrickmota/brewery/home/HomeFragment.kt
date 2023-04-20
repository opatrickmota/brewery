package com.patrickmota.brewery.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.patrickmota.brewery.home.databinding.FragmentHomeBinding
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        binding.fragmentHomeSearchEditTextInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                setupBreweriesObserve()
            }
            false
        }
    }

    private fun setupBreweriesObserve() {
        val city = binding.fragmentHomeSearchEditTextInputEditText.text.toString()

        if (city.isNotBlank()) {
            hideNoCityTyped()
            viewModel.loadBreweries(city)
            viewModel.breweries.observe(requireActivity()) {
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

                            val fragmentManager: FragmentManager = childFragmentManager
                            val fragmentTransaction: FragmentTransaction =
                                fragmentManager.beginTransaction()
                            fragmentTransaction.replace(
                                R.id.fragment_home_search_results_fragment_container_view,
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
        binding.fragmentHomeSearchResultsFragmentContainerView.visibility =
            View.VISIBLE
    }

    private fun hideFragmentContainer() {
        binding.fragmentHomeSearchResultsFragmentContainerView.visibility =
            View.GONE
    }

    private fun showEmptyState() {
        hideFragmentContainer()
        hideNoCityTyped()
        hideError()
        binding.fragmentHomeNoResultsTextView.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.fragmentHomeNoResultsTextView.visibility = View.GONE
    }

    private fun showNoCityTyped() {
        hideEmptyState()
        hideFragmentContainer()
        hideError()
        binding.fragmentHomeNoCityTypedTextView.visibility = View.VISIBLE
    }

    private fun hideNoCityTyped() {
        binding.fragmentHomeNoCityTypedTextView.visibility = View.GONE
    }

    private fun showLoading() {
        hideError()
        hideNoCityTyped()
        hideEmptyState()
        hideFragmentContainer()
        binding.fragmentHomeLoadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.fragmentHomeLoadingProgressBar.visibility = View.GONE
    }

    private fun showError() {
        hideNoCityTyped()
        hideEmptyState()
        hideFragmentContainer()
        hideLoading()
        binding.fragmentHomeErrorTextView.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.fragmentHomeErrorTextView.visibility = View.GONE
    }

}