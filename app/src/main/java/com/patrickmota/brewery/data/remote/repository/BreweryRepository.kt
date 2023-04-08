package com.patrickmota.brewery.data.remote.repository

import com.patrickmota.brewery.data.remote.model.BreweryResponse

interface BreweryRepository {
    suspend fun getBreweries(city: String): List<BreweryResponse>

    suspend fun getBreweryById(id: String): BreweryResponse
}
