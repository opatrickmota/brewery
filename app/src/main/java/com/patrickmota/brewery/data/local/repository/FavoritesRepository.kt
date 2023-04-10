package com.patrickmota.brewery.data.local.repository

import com.patrickmota.brewery.data.local.model.BreweryModel

interface FavoritesRepository {
    suspend fun addFavorite(breweryModel: BreweryModel)
    suspend fun getBreweries(): List<BreweryModel>
    suspend fun getBreweryById(id: String): BreweryModel
    suspend fun deleteBrewery(breweryModel: BreweryModel)
}
