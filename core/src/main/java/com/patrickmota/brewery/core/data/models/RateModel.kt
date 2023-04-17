package com.patrickmota.brewery.core.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rate")
data class RateModel(
    @PrimaryKey
    val id: String,
    val rating: Int
)
