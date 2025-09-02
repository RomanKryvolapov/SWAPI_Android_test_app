/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.di

import com.romankryvolapov.swapi.data.repository.datastore.DataStoreRepositoryImpl
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataStoreModule = module {

    singleOf(::DataStoreRepositoryImpl) bind DataStoreRepository::class

}