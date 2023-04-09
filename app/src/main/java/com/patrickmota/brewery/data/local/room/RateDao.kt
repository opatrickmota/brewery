package com.patrickmota.brewery.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patrickmota.brewery.data.local.model.RateModel

@Dao
interface RateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRating(rateModel: RateModel)

    @Query("SELECT * FROM rate WHERE id = :id")
    fun getRateById(id: String): RateModel

}