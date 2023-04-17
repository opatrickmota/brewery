package com.patrickmota.brewery.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patrickmota.brewery.core.data.models.RateModel

@Dao
interface RateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRating(rateModel: RateModel)

    @Query("SELECT * FROM rate WHERE id = :id")
    fun getRateById(id: String): RateModel

}