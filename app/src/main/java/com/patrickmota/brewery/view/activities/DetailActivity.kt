package com.patrickmota.brewery.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.patrickmota.brewery.R
import com.patrickmota.brewery.constants.Brewery
import com.patrickmota.brewery.data.remote.model.BreweryResponse
import com.patrickmota.brewery.databinding.ActivityDetailBinding
import com.patrickmota.brewery.viewmodel.ViewData
import com.patrickmota.brewery.viewmodel.detail.DetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMenuListener()
        setupObservers()
    }

    private fun setupObservers() {
        setupBreweryObserver()
    }

    private fun setupBreweryObserver() {
        val breweryId = intent.getStringExtra("id").orEmpty()

        viewModel.loadBrewery(breweryId)
        viewModel.brewery.observe(this@DetailActivity) {
            when (it.status) {
                ViewData.Status.LOADING -> {
                    setupUi(Brewery.brewery)
                }
                ViewData.Status.COMPLETE -> {
                    setupUi(it.data)
                }
                ViewData.Status.ERROR -> Unit
            }
        }
    }

    private fun setupUi(brewery: BreweryResponse?) {
        binding.activityDetailBreweryNameTextView.text = brewery?.name
        binding.activityDetailLogoTextTextView.text = brewery?.name?.substring(0, 1)
        binding.activityDetailBreweryTypeTextView.text = brewery?.breweryType.orEmpty()
        binding.activityDetailTotalReviewTextView.text =
            resources.getQuantityString(R.plurals.activity_detail_rating, 0, 0)
        binding.activityDetailRatingAverageTextView.text = "0,0"
        binding.activityDetailAddressTextView.text = getLocalization(brewery)
        binding.activityDetailWebsiteTextView.text =
            brewery?.webSiteUrl ?: "The company does not have a website"
        binding.activityDetailPhoneTextView.text =
            brewery?.phone ?: "The company does not have a phone"
    }

    private fun getLocalization(brewery: BreweryResponse?): String {
        val localizations = arrayListOf(
            brewery?.street,
            brewery?.city,
            "${brewery?.state} ${brewery?.postalCode}",
            brewery?.country
        )

        var localization = ""
        localizations.forEach { property ->
            if (property != null) {
                localization += if (localization == "") {
                    property
                } else {
                    ", $property"
                }
            }
        }

        return brewery?.address1 ?: brewery?.address2 ?: brewery?.address3
        ?: localization
    }

    private fun setupMenuListener() {

        binding.activityDetailHeaderMaterialToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.activityDetailHeaderMaterialToolbar.inflateMenu(R.menu.top_app_bar)

        binding.activityDetailHeaderMaterialToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    binding.activityDetailHeaderMaterialToolbar.menu.clear()
                    binding.activityDetailHeaderMaterialToolbar.inflateMenu(R.menu.top_app_bar_favorited)
                    true
                }
                R.id.favorited -> {
                    binding.activityDetailHeaderMaterialToolbar.menu.clear()
                    binding.activityDetailHeaderMaterialToolbar.inflateMenu(R.menu.top_app_bar)
                    true
                }
                else -> false
            }
        }
    }
}