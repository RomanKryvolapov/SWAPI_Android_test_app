package com.romankryvolapov.swapi.domain.usecase

import com.romankryvolapov.swapi.domain.models.common.BaseFlowUseCase
import com.romankryvolapov.swapi.domain.models.common.ResultEmittedData
import com.romankryvolapov.swapi.domain.models.person.PersonsModel
import com.romankryvolapov.swapi.domain.repository.network.SwapiNetworkRepository
import kotlinx.coroutines.flow.Flow

class GetPersonsUseCase(
    private val swapiNetworkRepository: SwapiNetworkRepository,
) : BaseFlowUseCase {

    fun invoke(
        page: Int,
        people: String?,
    ): Flow<ResultEmittedData<PersonsModel>> = swapiNetworkRepository.getPersons(
        page = page,
        people = people,
    )

}