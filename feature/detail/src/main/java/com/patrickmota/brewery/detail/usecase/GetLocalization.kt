package com.patrickmota.brewery.detail.usecase

import com.patrickmota.brewery.core.data.models.BreweryResponse

class GetLocalization {
    operator fun invoke(brewery: BreweryResponse?): String {
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
}