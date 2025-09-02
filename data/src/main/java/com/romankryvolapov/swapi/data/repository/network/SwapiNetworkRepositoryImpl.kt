/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.repository.network

import com.romankryvolapov.swapi.data.mappers.network.persons.PersonsResponseMapper
import com.romankryvolapov.swapi.data.mappers.network.planets.PlanetResponseMapper
import com.romankryvolapov.swapi.data.models.network.persons.PersonsResponse
import com.romankryvolapov.swapi.data.models.network.planets.PlanetResponse
import com.romankryvolapov.swapi.data.network.api.SwapiApi
import com.romankryvolapov.swapi.data.repository.network.base.BaseRepository
import com.romankryvolapov.swapi.data.utils.CoroutineContextProvider
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.models.common.ResultEmittedData
import com.romankryvolapov.swapi.domain.models.common.onFailure
import com.romankryvolapov.swapi.domain.models.common.onSuccess
import com.romankryvolapov.swapi.domain.models.person.PersonsModel
import com.romankryvolapov.swapi.domain.models.planets.PlanetModel
import com.romankryvolapov.swapi.domain.repository.network.SwapiNetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SwapiNetworkRepositoryImpl(
    private val swapiApi: SwapiApi,
    private val planetResponseMapper: PlanetResponseMapper,
    private val personsResponseMapper: PersonsResponseMapper,
    contextProvider: CoroutineContextProvider,
) : SwapiNetworkRepository, BaseRepository(
    contextProvider = contextProvider,
) {

    companion object {
        private const val TAG = "SwapiNetworkRepositoryTag"
    }

    override fun getPersons(
        page: Int,
        people: String?,
    ): Flow<ResultEmittedData<PersonsModel>> = flow {
        logDebug("getPersons page: $page people: $people", TAG)
        emit(ResultEmittedData.loading())
        getResult<PersonsResponse> {
            swapiApi.getPeoples(
                page = page,
                people = people,
            )
        }.onSuccess { model, message, responseCode ->
            logDebug("getPersons onSuccess", TAG)
            emit(
                ResultEmittedData.success(
                    message = message,
                    responseCode = responseCode,
                    model = personsResponseMapper.map(model),
                )
            )
        }.onFailure { error, title, message, responseCode, errorType, exception ->
            logError("getPersons onFailure", message, TAG)
            emit(
                ResultEmittedData.error(
                    model = null,
                    error = error,
                    title = title,
                    message = message,
                    throwable = exception,
                    errorType = errorType,
                    responseCode = responseCode,
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun getPlanetDetails(
        id: Int,
    ): Flow<ResultEmittedData<PlanetModel>> = flow {
        logDebug("getPlanet id: $id", TAG)
        emit(ResultEmittedData.loading())
        getResult<PlanetResponse> {
            swapiApi.getPlanet(
                id = id,
            )
        }.onSuccess { model, message, responseCode ->
            logDebug("getPlanet onSuccess", TAG)
            emit(
                ResultEmittedData.success(
                    message = message,
                    responseCode = responseCode,
                    model = planetResponseMapper.map(model),
                )
            )
        }.onFailure { error, title, message, responseCode, errorType, exception ->
            logError("getPlanet onFailure", message, TAG)
            emit(
                ResultEmittedData.error(
                    model = null,
                    error = error,
                    title = title,
                    message = message,
                    throwable = exception,
                    errorType = errorType,
                    responseCode = responseCode,
                )
            )
        }
    }.flowOn(Dispatchers.IO)

}