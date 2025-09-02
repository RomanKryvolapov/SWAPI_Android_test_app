/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.repository.network

import com.romankryvolapov.swapi.domain.models.common.ResultEmittedData
import com.romankryvolapov.swapi.domain.models.person.PersonsModel
import com.romankryvolapov.swapi.domain.models.planets.PlanetModel
import kotlinx.coroutines.flow.Flow

interface SwapiNetworkRepository {

    fun getPersons(
        page: Int,
        people: String?,
    ): Flow<ResultEmittedData<PersonsModel>>

    fun getPlanetDetails(
        id: Int,
    ): Flow<ResultEmittedData<PlanetModel>>

}