package com.patrickmota.brewery.data.local.repository

import com.patrickmota.brewery.data.local.model.RateModel
import com.patrickmota.brewery.data.local.room.RateDao

class RateRepositoryImpl(private val rateDao: RateDao): RateRepository {
    override suspend fun addRate(rateModel: RateModel) = rateDao.addRating(rateModel)
    override suspend fun getRateById(id: String): RateModel = rateDao.getRateById(id)
}