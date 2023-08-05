package com.patrickmota.brewery.detail.usecase

import android.net.Uri
import androidx.core.net.toUri
import com.patrickmota.brewery.core.data.models.BreweryResponse

class GetUri(
    private val getLocalization: GetLocalization
) {
    operator fun invoke(brewery: BreweryResponse): Uri? {
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
}