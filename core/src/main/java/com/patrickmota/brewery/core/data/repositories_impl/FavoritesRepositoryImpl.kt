package com.patrickmota.brewery.core.data.repositories_impl

import com.patrickmota.brewery.core.data.db.FavoritesDao
import com.patrickmota.brewery.core.data.models.BreweryModel
import com.patrickmota.brewery.core.domain.repositories.FavoritesRepository

class FavoritesRepositoryImpl(private val favoritesDao: FavoritesDao) : FavoritesRepository {

    override suspend fun addFavorite(breweryModel: BreweryModel) =
        favoritesDao.addFavorite(breweryModel)

    override suspend fun getBreweries(): List<BreweryModel> = favoritesDao.getBrewery()

    override suspend fun getBreweryById(id: String): BreweryModel = favoritesDao.getBreweryById(id)

    override suspend fun deleteBrewery(breweryModel: BreweryModel) =
        favoritesDao.deleteBrewery(breweryModel)

}
