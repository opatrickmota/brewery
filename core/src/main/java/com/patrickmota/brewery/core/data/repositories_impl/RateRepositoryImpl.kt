package com.patrickmota.brewery.core.data.repositories_impl

import com.patrickmota.brewery.core.data.db.RateDao
import com.patrickmota.brewery.core.data.models.RateModel
import com.patrickmota.brewery.core.domain.repositories.RateRepository

class RateRepositoryImpl(private val rateDao: RateDao): RateRepository {
    override suspend fun addRate(rateModel: RateModel) = rateDao.addRating(rateModel)
    override suspend fun getRateById(id: String): RateModel = rateDao.getRateById(id)
}