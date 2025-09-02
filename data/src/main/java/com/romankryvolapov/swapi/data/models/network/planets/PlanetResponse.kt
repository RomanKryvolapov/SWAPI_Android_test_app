package com.romankryvolapov.swapi.data.models.network.planets

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanetResponse(
    @SerialName("name")
    val name: String?,

    @SerialName("rotation_period")
    val rotationPeriod: Int?,

    @SerialName("orbital_period")
    val orbitalPeriod: Int?,

    @SerialName("diameter")
    val diameter: Int?,

    @SerialName("climate")
    val climate: String?,

    @SerialName("gravity")
    val gravity: String?,

    @SerialName("terrain")
    val terrain: String?,

    @SerialName("surface_water")
    val surfaceWater: Int?,

    @SerialName("population")
    val population: Int?,

    @SerialName("residents")
    val residents: List<String>?,

    @SerialName("films")
    val films: List<String>?,

    @SerialName("created")
    val created: String?,

    @SerialName("edited")
    val edited: String?,

    @SerialName("url")
    val url: String?,
)
