package com.patrickmota.brewery.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.patrickmota.brewery.core.data.models.Brewery
import com.patrickmota.brewery.core.data.models.BreweryResponse
import com.patrickmota.brewery.core.data.models.RateModel
import com.patrickmota.brewery.core.data.models.toBreweryModel
import com.patrickmota.brewery.core.data.models.toBreweryResponse
import com.patrickmota.brewery.detail.databinding.BottomSheetDialogLayoutBinding
import com.patrickmota.brewery.detail.databinding.FragmentDetailBinding
import com.patrickmota.brewery.detail.usecase.GetLocalization
import com.patrickmota.brewery.detail.usecase.GetUri
import com.patrickmota.brewery.detail.usecase.OpenNumberInDialer
import com.patrickmota.brewery.detail.usecase.ShowMap
import com.patrickmota.brewery.detail.usecase.ShowWebsite
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.detail.DetailViewModel
import com.patrickmota.brewery.viewmodel.favorite.FavoriteViewModel
import com.patrickmota.brewery.viewmodel.rate.RateViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModel()
    private val favoriteViewModel: FavoriteViewModel by sharedViewModel()
    private lateinit var rateBinding: BottomSheetDialogLayoutBinding
    private val dialog by lazy {
        BottomSheetDialog(requireContext(), R.style.RateThemeDialog)
    }
    private val rateViewModel: RateViewModel by viewModel()
    private val args by navArgs<DetailFragmentArgs>()
    private val showMap: ShowMap by inject()
    private val showWebsite: ShowWebsite by inject()
    private val openNumberInDialer: OpenNumberInDialer by inject()
    private val getLocalization: GetLocalization by inject()
    private val getUri: GetUri by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        rateBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        setupMenuListener()
        setupRatingListener()
    }

    private fun setupRatingListener() {
        binding.fragmentDetailRateButton.setOnClickListener {
            showRatingDialog()
        }
    }

    private fun showRatingDialog() {
        dialog.setContentView(rateBinding.root)
        dialog.show()

        rateBinding.dialogRateCloseButtonImageView.setOnClickListener {
            dialog.dismiss()
        }

        rateBinding.dialogRateSaveButton.setOnClickListener {
            saveRating()
            dialog.dismiss()
        }
    }

    private fun saveRating() {
        val breweryId = args.breweryId
        val rating = rateBinding.dialogRateRateRatingBar.rating

        binding.fragmentDetailTotalReviewTextView.text =
            resources.getQuantityString(
                com.patrickmota.brewery.common.R.plurals.fragment_detail_rating,
                1,
                1
            )
        binding.fragmentDetailRatingAverageTextView.text = rating.toString()
        binding.fragmentDetailStarsRatingBar.rating = rating

        Toast.makeText(
            requireContext(),
            "Avaliado",
            Toast.LENGTH_SHORT
        ).show()

        rateViewModel.addRating(
            RateModel(breweryId, rating.toInt())
        )

    }

    private fun setupObservers() {
        setupBreweryObserver()
    }

    private fun setupBreweryObserver() {
        val breweryId = args.breweryId

        viewModel.loadBrewery(breweryId)
        viewModel.brewery.observe(requireActivity()) {
            when (it.status) {
                ViewData.Status.LOADING -> {
                    /* TODO - criar loading */
                    setupUi(Brewery.brewery)
                }
                ViewData.Status.COMPLETE -> {
                    Brewery.brewery = it.data
                    setupUi(it.data)
                }
                ViewData.Status.ERROR -> {
                    /*TODO - tratar erro */
                    setupUi(Brewery.brewery)
                }
            }
        }
    }

    private fun setupUi(brewery: BreweryResponse?) {
        binding.fragmentDetailBreweryNameTextView.text = brewery?.name
        binding.fragmentDetailLogoTextTextView.text = brewery?.name?.substring(0, 1)
        binding.fragmentDetailBreweryTypeTextView.text = brewery?.breweryType.orEmpty()
        binding.fragmentDetailTotalReviewTextView.text =
            context?.resources?.getQuantityString(
                com.patrickmota.brewery.common.R.plurals.fragment_detail_rating,
                0,
                0
            )
        binding.fragmentDetailRatingAverageTextView.text = "0,0"
        binding.fragmentDetailAddressTextView.text = getLocalization(brewery)
        binding.fragmentDetailWebsiteTextView.text =
            brewery?.webSiteUrl ?: "The company does not have a website"
        binding.fragmentDetailPhoneTextView.text =
            brewery?.phone ?: "The company does not have a phone"

        binding.fragmentDetailMapTextView.setOnClickListener {
            brewery?.let {
                showMap(requireContext(), getUri(it))
            }
        }

        binding.fragmentDetailWebsiteTextView.setOnClickListener {
            brewery?.webSiteUrl?.let {
                showWebsite(requireContext(), it)
            }
        }

        binding.fragmentDetailPhoneTextView.setOnClickListener {
            brewery?.phone?.let {
                openNumberInDialer(requireContext(), it)
            }
        }

        rateViewModel.getRateById(brewery?.id.orEmpty())
        if (activity != null) {
            rateViewModel.rating.observe(requireActivity()) {
                when (it.status) {
                    ViewData.Status.COMPLETE -> {
                        it.data?.let { rateModel ->
                            binding.fragmentDetailTotalReviewTextView.text =
                                context?.resources?.getQuantityString(
                                    com.patrickmota.brewery.common.R.plurals.fragment_detail_rating,
                                    1,
                                    1
                                )
                            binding.fragmentDetailRatingAverageTextView.text =
                                rateModel.rating.toString()
                            binding.fragmentDetailStarsRatingBar.rating = rateModel.rating.toFloat()
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupMenuListener() {
        val breweryId = args.breweryId

        binding.fragmentDetailHeaderMaterialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        favoriteViewModel.loadFavoriteById(breweryId)
        favoriteViewModel.brewery.observe(requireActivity()) {
            binding.fragmentDetailHeaderMaterialToolbar.menu.clear()
            when (it.status) {
                ViewData.Status.COMPLETE -> {
                    if (it.data?.id == breweryId) {
                        Brewery.brewery = it.data?.toBreweryResponse()
                        binding.fragmentDetailHeaderMaterialToolbar
                            .inflateMenu(com.patrickmota.brewery.common.R.menu.top_app_bar_favorited)
                    } else {
                        binding.fragmentDetailHeaderMaterialToolbar.inflateMenu(com.patrickmota.brewery.common.R.menu.top_app_bar)
                    }
                }
                else -> Unit
            }
        }

        binding.fragmentDetailHeaderMaterialToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.patrickmota.brewery.common.R.id.favorite -> {
                    binding.fragmentDetailHeaderMaterialToolbar.menu.clear()
                    binding.fragmentDetailHeaderMaterialToolbar.inflateMenu(com.patrickmota.brewery.common.R.menu.top_app_bar_favorited)
                    Brewery.brewery?.let {
                        favoriteViewModel.addFavorite(it.toBreweryModel())
                    }
                    true
                }
                com.patrickmota.brewery.common.R.id.favorited -> {
                    binding.fragmentDetailHeaderMaterialToolbar.menu.clear()
                    binding.fragmentDetailHeaderMaterialToolbar.inflateMenu(com.patrickmota.brewery.common.R.menu.top_app_bar)
                    Brewery.brewery?.let {
                        favoriteViewModel.deleteFavorite(it.toBreweryModel())
                    }
                    true
                }
                else -> false
            }
        }
    }
}