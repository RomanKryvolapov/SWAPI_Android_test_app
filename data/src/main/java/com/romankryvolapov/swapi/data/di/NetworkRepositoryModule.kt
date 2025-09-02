/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.di

import com.romankryvolapov.swapi.data.repository.network.SwapiNetworkRepositoryImpl
import com.romankryvolapov.swapi.domain.repository.network.SwapiNetworkRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkRepositoryModule = module {

    singleOf(::SwapiNetworkRepositoryImpl) bind SwapiNetworkRepository::class

}