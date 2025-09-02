package com.romankryvolapov.swapi.data.mappers.network.planets

import com.romankryvolapov.swapi.data.models.network.planets.PlanetResponse
import com.romankryvolapov.swapi.domain.mappers.base.BaseMapper
import com.romankryvolapov.swapi.domain.models.planets.PlanetModel

class PlanetResponseMapper : BaseMapper<PlanetResponse, PlanetModel>() {

    override fun map(model: PlanetResponse): PlanetModel {
        return with(model) {
            PlanetModel(
                name = name,
                rotationPeriod = rotationPeriod,
                orbitalPeriod = orbitalPeriod,
                diameter = diameter,
                climate = climate,
                gravity = gravity,
                terrain = terrain,
                surfaceWater = surfaceWater,
                population = population,
                residents = residents,
                films = films,
                created = created,
                edited = edited,
                url = url
            )
        }
    }

}