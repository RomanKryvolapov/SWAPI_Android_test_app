package com.romankryvolapov.swapi.mappers.persons

import com.romankryvolapov.swapi.domain.mappers.base.BaseMapper
import com.romankryvolapov.swapi.domain.models.person.PersonModel
import com.romankryvolapov.swapi.models.persons.PersonUi

class PersonsUiMapper : BaseMapper<PersonModel, PersonUi>() {

    override fun map(model: PersonModel): PersonUi {
        return with(model) {
            PersonUi(
                name = name,
                homeworldID = homeworldID
            )
        }
    }

}