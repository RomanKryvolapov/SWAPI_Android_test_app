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

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.romankryvolapov.swapi.data.BuildConfig.ENCRYPTED_SHARED_PREFERENCES_NAME
import com.romankryvolapov.swapi.data.repository.preferences.PreferencesRepositoryImpl
import com.romankryvolapov.swapi.domain.repository.preferences.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesModule = module {

    single<MasterKey> {
        MasterKey.Builder(androidContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    single<EncryptedSharedPreferences> {
        EncryptedSharedPreferences
            .create(
                androidContext(),
                ENCRYPTED_SHARED_PREFERENCES_NAME,
                get<MasterKey>(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ) as EncryptedSharedPreferences
    }

    singleOf(::PreferencesRepositoryImpl) bind PreferencesRepository::class

}