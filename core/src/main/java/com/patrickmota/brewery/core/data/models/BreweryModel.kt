package com.patrickmota.brewery.core.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brewery")
data class BreweryModel(
    @PrimaryKey
    val id: String,
    val name: String?,
    @ColumnInfo(name = "brewery_type")
    val breweryType: String?,
    @ColumnInfo(name = "address_1")
    val address1: String?,
    @ColumnInfo(name = "address_2")
    val address2: String?,
    @ColumnInfo(name = "address_3")
    val address3: String?,
    val city: String?,
    @ColumnInfo(name = "state_province")
    val stateProvince: String?,
    @ColumnInfo(name = "postal_code")
    val postalCode: String?,
    val country: String?,
    val longitude: String?,
    val latitude: String?,
    val phone: String?,
    @ColumnInfo(name = "website_url")
    val webSiteUrl: String?,
    val state: String?,
    val street: String?
)

fun BreweryModel.toBreweryResponse() = BreweryResponse(
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