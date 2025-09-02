package com.romankryvolapov.swapi.mappers.planets

import com.romankryvolapov.swapi.domain.extensions.toSpaced
import com.romankryvolapov.swapi.domain.mappers.base.BaseMapper
import com.romankryvolapov.swapi.domain.models.planets.PlanetModel
import com.romankryvolapov.swapi.models.planets.PlanetAdapterMarker
import com.romankryvolapov.swapi.models.planets.PlanetUiField
import com.romankryvolapov.swapi.models.planets.PlanetUiTitle

class PlanetUiMapper : BaseMapper<PlanetModel, List<PlanetAdapterMarker>>() {

    override fun map(model: PlanetModel): List<PlanetAdapterMarker> {
        return with(model) {
            buildList {
                add(
                    PlanetUiTitle(
                        title = "HomeWorls",
                        description = name ?: "Unknown"
                    )
                )
                add(
                    PlanetUiField(
                        title = "Gravity",
                        description = gravity ?: "Unknown"
                    )
                )
                add(
                    PlanetUiField(
                        title = "Population",
                        description = population?.toSpaced() + " peoples" ?: "Unknown",
                    )
                )
                add(
                    PlanetUiField(
                        title = "Climate",
                        description = climate ?: "Unknown"
                    )
                )
                add(
                    PlanetUiField(
                        title = "Terrain",
                        description = terrain ?: "Unknown"
                    )
                )
                add(
                    PlanetUiField(
                        title = "Diameter",
                        description = diameter?.toSpaced() + " km" ?: "Unknown",
                    )
                )
                add(
                    PlanetUiField(
                        title = "Rotation Period",
                        description = rotationPeriod?.toSpaced() + " h" ?: "Unknown",
                    )
                )
                add(
                    PlanetUiField(
                        title = "Orbital Period",
                        description = orbitalPeriod?.toSpaced() + " h" ?: "Unknown",
                    )
                )
                add(
                    PlanetUiField(
                        title = "Surface Water",
                        description = surfaceWater?.toSpaced() ?: "Unknown",
                    )
                )
            }
        }
    }

}