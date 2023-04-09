package com.patrickmota.brewery.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rate")
data class RateModel(
    @PrimaryKey
    val id: String,
    val rating: Int
)
