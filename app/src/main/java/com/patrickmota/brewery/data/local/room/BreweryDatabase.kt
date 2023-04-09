package com.patrickmota.brewery.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.patrickmota.brewery.data.local.model.BreweryModel
import com.patrickmota.brewery.data.local.model.RateModel

@Database(entities = [BreweryModel::class, RateModel::class], version = 2, exportSchema = true)
abstract class BreweryDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun rateDao(): RateDao
}