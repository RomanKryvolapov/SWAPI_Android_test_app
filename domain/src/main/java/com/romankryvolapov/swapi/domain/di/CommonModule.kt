/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.di

import com.google.gson.Gson
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfoNullable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {

    singleOf(::Gson)

    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<JsonAdapter<ApplicationInfoNullable>> {
        get<Moshi>().adapter(ApplicationInfoNullable::class.java)
    }

}