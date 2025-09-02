package com.romankryvolapov.swapi.domain.models.planets

data class PlanetModel(
    val name: String?,
    val rotationPeriod: Int?,
    val orbitalPeriod: Int?,
    val diameter: Int?,
    val climate: String?,
    val gravity: String?,
    val terrain: String?,
    val surfaceWater: Int?,
    val population: Int?,
    val residents: List<String>?,
    val films: List<String>?,
    val created: String?,
    val edited: String?,
    val url: String?,
)
