/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.di

import com.romankryvolapov.swapi.domain.mappers.ApplicationInfoMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val mappersModule = module {

    singleOf(::ApplicationInfoMapper)

}