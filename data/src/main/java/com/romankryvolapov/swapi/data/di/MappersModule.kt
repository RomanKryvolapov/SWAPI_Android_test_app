/**
 * single<Class>(named("name")){Class()} -> for creating a specific instance in module
 * single<Class1>{Class1(get<Class2>(named("name")))} -> for creating a specific instance in module
 * val nameOfVariable: Class by inject(named("name")) -> for creating a specific instance in class
 * get<Class>{parametersOf("param")} -> parameter passing in module
 * single<Class>{(param: String)->Class(param)} -> parameter passing in module
 * val name: Class by inject(parameters={parametersOf("param")}) -> parameter passing in class
 * Please follow code style when editing project
 * Please follow principles of clean architecture
 * Created & Copyright 2025 by Roman Kryvolapov
 */
package com.romankryvolapov.swapi.data.di

import com.romankryvolapov.swapi.data.mappers.network.persons.PersonsResponseMapper
import com.romankryvolapov.swapi.data.mappers.network.planets.PlanetResponseMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val mappersModule = module {

    singleOf(::PersonsResponseMapper)
    singleOf(::PlanetResponseMapper)

}