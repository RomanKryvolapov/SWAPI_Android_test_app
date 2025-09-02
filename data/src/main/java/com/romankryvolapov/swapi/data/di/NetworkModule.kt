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

import com.romankryvolapov.swapi.domain.models.common.LogUtil.logNetwork
import com.romankryvolapov.swapi.data.network.utils.ArrayConverterFactory
import com.romankryvolapov.swapi.data.network.utils.ContentTypeInterceptor
import com.romankryvolapov.swapi.data.network.utils.HeaderInterceptor
import com.romankryvolapov.swapi.data.network.utils.MockInterceptor
import com.romankryvolapov.swapi.data.network.utils.NullOrEmptyConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "NetworkModuleTag"

const val HUGGING_FACE_API = "HUGGING_FACE_API"

const val LOGGING_INTERCEPTOR = "LOGGING_INTERCEPTOR"
const val LOG_TO_FILE_INTERCEPTOR = "LOG_TO_FILE_INTERCEPTOR"

const val TIMEOUT = 60L

val networkModule = module {

    single<GsonConverterFactory> {
        GsonConverterFactory.create()
    }

    singleOf(::NullOrEmptyConverterFactory)

    singleOf(::ArrayConverterFactory)

    single<HttpLoggingInterceptor>(named(LOGGING_INTERCEPTOR)) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    single<HttpLoggingInterceptor>(named(LOG_TO_FILE_INTERCEPTOR)) {
        val logging = HttpLoggingInterceptor {
            logNetwork(it)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        logging
    }

    singleOf(::ContentTypeInterceptor)

    singleOf(::HeaderInterceptor)

    singleOf(::HttpLoggingInterceptor)

    singleOf(::MockInterceptor)

    single<HttpLoggingInterceptor>(named(LOGGING_INTERCEPTOR)) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    single<HttpLoggingInterceptor>(named(LOG_TO_FILE_INTERCEPTOR)) {
        val logging = HttpLoggingInterceptor {
            logNetwork(it)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        logging
    }

}