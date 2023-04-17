package com.patrickmota.brewery.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.patrickmota.brewery.core.data.models.BreweryModel
import com.patrickmota.brewery.core.data.models.RateModel

@Database(entities = [BreweryModel::class, RateModel::class], version = 2, exportSchema = true)
abstract class BreweryDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun rateDao(): RateDao
}