package com.patrickmota.brewery.data.remote.api

import com.patrickmota.brewery.data.remote.model.BreweryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreweryService {

    @GET("breweries")
    suspend fun getBreweries(
        @Query("by_city") city: String,
        @Query("sort") sort: String = "type&#44;name&#58;asc"
    ): List<BreweryResponse>

    @GET("breweries/{id}")
    suspend fun getBreweryById(@Path("id") id: String): BreweryResponse
}