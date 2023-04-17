package com.patrickmota.brewery.core.domain.repositories

import com.patrickmota.brewery.core.data.models.RateModel

interface RateRepository {
    suspend fun addRate(rateModel: RateModel)
    suspend fun getRateById(id: String): RateModel
}