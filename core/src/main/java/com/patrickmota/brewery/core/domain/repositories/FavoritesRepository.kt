package com.patrickmota.brewery.core.domain.repositories

import com.patrickmota.brewery.core.data.models.BreweryModel

interface FavoritesRepository {
    suspend fun addFavorite(breweryModel: BreweryModel)
    suspend fun getBreweries(): List<BreweryModel>
    suspend fun getBreweryById(id: String): BreweryModel
    suspend fun deleteBrewery(breweryModel: BreweryModel)
}
