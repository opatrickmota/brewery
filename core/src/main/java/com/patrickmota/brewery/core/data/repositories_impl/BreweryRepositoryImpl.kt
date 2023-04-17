package com.patrickmota.brewery.core.data.repositories_impl

import com.patrickmota.brewery.core.data.api.BreweryService
import com.patrickmota.brewery.core.data.models.BreweryResponse
import com.patrickmota.brewery.core.domain.repositories.BreweryRepository

class BreweryRepositoryImpl(private val service: BreweryService) : BreweryRepository {

    override suspend fun getBreweries(city: String): List<BreweryResponse> =
        service.getBreweries(city)

    override suspend fun getBreweryById(id: String): BreweryResponse =
        service.getBreweryById(id)
}