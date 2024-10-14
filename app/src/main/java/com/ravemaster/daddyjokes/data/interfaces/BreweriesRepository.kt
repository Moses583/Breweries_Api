package com.ravemaster.daddyjokes.data.interfaces

import com.ravemaster.daddyjokes.data.Result
import com.ravemaster.daddyjokes.data.models.Breweries
import kotlinx.coroutines.flow.Flow

interface BreweriesRepository {
    suspend fun getList(): Flow<Result<Breweries>>
}