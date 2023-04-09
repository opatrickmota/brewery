package com.patrickmota.brewery.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.patrickmota.brewery.data.local.model.BreweryModel

@Database(entities = [BreweryModel::class], version = 1, exportSchema = true)
abstract class BreweryDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}