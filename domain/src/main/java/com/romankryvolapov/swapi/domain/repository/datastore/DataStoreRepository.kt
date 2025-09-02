/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.repository.datastore

import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveApplicationInfo(value: ApplicationInfo): Boolean

    suspend fun readApplicationInfo(): ApplicationInfo

    suspend fun saveApplicationInfoFromJson(json: String): String?

    suspend fun readApplicationInfoToJson(): String?

    suspend fun restoreDefault(): Boolean

    fun subscribeToApplicationInfo(): Flow<ApplicationInfo>

}