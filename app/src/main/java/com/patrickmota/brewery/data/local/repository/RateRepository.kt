package com.patrickmota.brewery.data.local.repository

import com.patrickmota.brewery.data.local.model.RateModel

interface RateRepository {
    suspend fun addRate(rateModel: RateModel)
    suspend fun getRateById(id: String): RateModel
}