package com.patrickmota.brewery.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
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

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        setupMenuListener()
    }

    private fun getUri(brewery: BreweryResponse): Uri? {
        val baseUri = "geo:0,0?q="
        return when {
            (brewery.latitude != null && brewery.longitude != null) -> {
                (baseUri + brewery.latitude + ", " + brewery.longitude + "(Treasure)").toUri()
            }
            (brewery.street?.isNotBlank() == true) -> {
                (baseUri + brewery.street.orEmpty() + " " + (brewery.address1 ?: brewery.address2
                ?: brewery.address3
                ?: "") + " " + brewery.city.orEmpty() + " " + brewery.state.orEmpty() + " " + brewery.country.orEmpty()).toUri()
            }
            (getLocalization(brewery).isNotBlank()) -> {
                (baseUri + getLocalization(brewery)).toUri()
            }
            (brewery.address2?.isNotBlank() == true) -> {
                (baseUri + brewery.address2).toUri()
            }
            (brewery.address3?.isNotBlank() == true) -> {
                (baseUri + brewery.address3).toUri()
            }
            else -> null
        }
    }

    private fun showMap(geoLocation: Uri?) {
        val intent = Intent(Intent.ACTION_VIEW, geoLocation)
        intent.setPackage("com.google.android.apps.maps")

        try {
            startActivity(intent)
        } catch (e: Throwable) {
            Toast.makeText(
                this,
                getString(R.string.activity_detail_error_location),
                Toast.LENGTH_SHORT
            ).show()
        }
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

        binding.activityDetailMapTextView.setOnClickListener {
            brewery?.let {
                showMap(getUri(it))
            }
        }

        binding.activityDetailWebsiteTextView.setOnClickListener {
            brewery?.webSiteUrl?.let {
                toBrowser(it)
            }
        }

        binding.activityDetailPhoneTextView.setOnClickListener {
            brewery?.phone?.let {
                openNumberInDialer(it)
            }
        }
    }

    private fun openNumberInDialer(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    private fun toBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
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