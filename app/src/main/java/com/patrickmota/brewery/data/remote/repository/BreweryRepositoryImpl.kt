package com.patrickmota.brewery.data.remote.repository

import com.patrickmota.brewery.data.remote.api.BreweryService
import com.patrickmota.brewery.data.remote.model.BreweryResponse

class BreweryRepositoryImpl(private val service: BreweryService) : BreweryRepository {

    override suspend fun getBreweries(city: String): List<BreweryResponse> =
        service.getBreweries(city)

    override suspend fun getBreweryById(id: String): BreweryResponse =
        service.getBreweryById(id)
}