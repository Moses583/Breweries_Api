package com.ravemaster.daddyjokes.data.interfaces

import com.ravemaster.daddyjokes.data.models.Breweries
import retrofit2.http.GET

interface GetBrews {

    @GET("breweries")
    suspend fun getBreweries(): Breweries

    companion object{
        const val BASE_URL = "https://api.openbrewerydb.org/v1/"
    }
}