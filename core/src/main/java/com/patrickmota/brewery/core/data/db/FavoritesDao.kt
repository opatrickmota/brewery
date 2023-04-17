package com.patrickmota.brewery.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patrickmota.brewery.core.data.models.BreweryModel

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(breweryModel: BreweryModel)

    @Query("SELECT * FROM brewery ORDER BY name ASC")
    fun getBrewery(): List<BreweryModel>

    @Query("SELECT * FROM brewery WHERE id = :id")
    fun getBreweryById(id: String): BreweryModel

    @Delete
    fun deleteBrewery(breweryModel: BreweryModel)
}