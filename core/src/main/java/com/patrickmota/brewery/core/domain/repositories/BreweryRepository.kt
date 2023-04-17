package com.patrickmota.brewery.core.domain.repositories

import com.patrickmota.brewery.core.data.models.BreweryResponse

interface BreweryRepository {
    suspend fun getBreweries(city: String): List<BreweryResponse>

    suspend fun getBreweryById(id: String): BreweryResponse
}
