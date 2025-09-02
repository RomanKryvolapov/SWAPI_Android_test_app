package com.romankryvolapov.swapi.domain.usecase

import com.romankryvolapov.swapi.domain.models.common.BaseFlowUseCase
import com.romankryvolapov.swapi.domain.models.common.ResultEmittedData
import com.romankryvolapov.swapi.domain.models.planets.PlanetModel
import com.romankryvolapov.swapi.domain.repository.network.SwapiNetworkRepository
import kotlinx.coroutines.flow.Flow

class GetPlanetDetailsUseCase (
    private val swapiNetworkRepository: SwapiNetworkRepository,
) : BaseFlowUseCase {

    fun invoke(
        id: Int,
    ): Flow<ResultEmittedData<PlanetModel>> = swapiNetworkRepository.getPlanetDetails(
        id = id,
    )

}