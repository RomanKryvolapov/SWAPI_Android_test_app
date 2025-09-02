/**
 * single<Class>(named("name")){Class()} -> for creating a specific instance in module
 * single<Class1>{Class1(get<Class2>(named("name")))} -> for creating a specific instance in module
 * val nameOfVariable: Class by inject(named("name")) -> for creating a specific instance in class
 * get<Class>{parametersOf("param")} -> parameter passing in module
 * single<Class>{(param: String)->Class(param)} -> parameter passing in module
 * val name: Class by inject(parameters={parametersOf("param")}) -> parameter passing in class
 * Created & Copyright 2025 by Roman Kryvolapov
 */
package com.romankryvolapov.swapi.di

import com.romankryvolapov.swapi.ui.fragments.planet.list.PlanetFieldDelegate
import com.romankryvolapov.swapi.ui.fragments.planet.list.PlanetTitleDelegate
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val delegatesModule = module {

    singleOf(::PlanetTitleDelegate)
    singleOf(::PlanetFieldDelegate)

}