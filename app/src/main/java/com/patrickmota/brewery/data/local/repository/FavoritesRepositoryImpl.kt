package com.patrickmota.brewery.data.local.repository

import com.patrickmota.brewery.data.local.model.BreweryModel
import com.patrickmota.brewery.data.local.room.FavoritesDao

class FavoritesRepositoryImpl(private val favoritesDao: FavoritesDao) : FavoritesRepository {

    override suspend fun addFavorite(breweryModel: BreweryModel) =
        favoritesDao.addFavorite(breweryModel)

    override suspend fun getBreweries(): List<BreweryModel> = favoritesDao.getBrewery()

    override suspend fun getBreweryById(id: String): BreweryModel = favoritesDao.getBreweryById(id)

    override suspend fun deleteBrewery(breweryModel: BreweryModel) =
        favoritesDao.deleteBrewery(breweryModel)

}
