/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.di

import com.romankryvolapov.swapi.data.network.api.SwapiApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val apiModule = module {

    singleOf(::SwapiApi)

}