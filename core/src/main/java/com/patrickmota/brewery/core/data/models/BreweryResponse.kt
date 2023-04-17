package com.patrickmota.brewery.core.data.models

import com.google.gson.annotations.SerializedName

data class BreweryResponse(
    val id: String,
    val name: String?,
    @SerializedName("brewery_type")
    val breweryType: String?,
    @SerializedName("address_1")
    val address1: String?,
    @SerializedName("address_2")
    val address2: String?,
    @SerializedName("address_3")
    val address3: String?,
    val city: String?,
    @SerializedName("state_province")
    val stateProvince: String?,
    @SerializedName("postal_code")
    val postalCode: String?,
    val country: String?,
    val longitude: String?,
    val latitude: String?,
    val phone: String?,
    @SerializedName("website_url")
    val webSiteUrl: String?,
    val state: String?,
    val street: String?
)

fun BreweryResponse.toBreweryModel() = BreweryModel(
    id,
    name,
    breweryType,
    address1,
    address2,
    address3,
    city,
    stateProvince,
    postalCode,
    country,
    longitude,
    latitude,
    phone,
    webSiteUrl,
    state,
    street
)