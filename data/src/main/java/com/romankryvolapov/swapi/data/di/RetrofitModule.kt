/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.di

import com.romankryvolapov.swapi.data.network.utils.NullOrEmptyConverterFactory
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val GITHUB_URL = "https://api.github.com"

val retrofitModule = module {

    single<Retrofit>(named(HUGGING_FACE_API)) {
        Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .client(get<OkHttpClient>())
//            .addConverterFactory(get<ArrayConverterFactory>())
            .addConverterFactory(get<NullOrEmptyConverterFactory>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

}