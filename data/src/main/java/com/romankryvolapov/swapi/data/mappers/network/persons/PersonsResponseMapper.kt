package com.romankryvolapov.swapi.data.mappers.network.persons

import androidx.core.net.toUri
import com.romankryvolapov.swapi.data.models.network.persons.PersonsResponse
import com.romankryvolapov.swapi.domain.mappers.base.BaseMapper
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.models.person.PersonModel
import com.romankryvolapov.swapi.domain.models.person.PersonsModel

class PersonsResponseMapper : BaseMapper<PersonsResponse, PersonsModel>() {

    companion object {
        private const val TAG = "PersonsResponseMapperTag"
    }


    override fun map(model: PersonsResponse): PersonsModel {
        return PersonsModel(
            count = model.count,
            next = model.next.extractID(),
            previous = model.previous.extractID(),
            results = model.results?.map {
                PersonModel(
                    name = it.name ?: "Unknown name",
                    homeworldID = it.homeworld.extractID()
                )
            } ?: emptyList()
        )
    }

    private fun String?.extractID(): Int? {
        if (this == null) {
            logError("extractID source is null", TAG)
            return null
        }
        return try {
            val uri = toUri()
            uri.pathSegments.last { it.isNotBlank() }.toInt()
        } catch (e: Exception) {
            logError("extractID exception: ${e.message}", TAG)
            null
        }
    }

}