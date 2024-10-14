package com.ravemaster.daddyjokes.data

import com.ravemaster.daddyjokes.data.interfaces.BreweriesRepository
import com.ravemaster.daddyjokes.data.interfaces.GetBrews
import com.ravemaster.daddyjokes.data.models.Breweries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class BreweriesRepositoryImplementation(
    private val getBrews: GetBrews
): BreweriesRepository {
    override suspend fun getList(): Flow<Result<Breweries>> {
        return flow {
            val breweriesFromApi = try {
                getBrews.getBreweries()
            } catch (e: IOException){
                e.printStackTrace()
                emit(Result.Error(message = "Error loading breweries"))
                return@flow
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Result.Error(message = "Error loading breweries"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading breweries"))
                return@flow
            }
            emit(Result.Success(breweriesFromApi))
        }
    }


}