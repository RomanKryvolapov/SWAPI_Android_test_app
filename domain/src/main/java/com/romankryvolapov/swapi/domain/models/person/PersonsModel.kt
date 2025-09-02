package com.romankryvolapov.swapi.domain.models.person

data class PersonsModel(
    val count: Int?,
    val next: Int?,
    val previous: Int?,
    val results: List<PersonModel>
)

data class PersonModel(
    val name: String,
    val homeworldID: Int?,
)
