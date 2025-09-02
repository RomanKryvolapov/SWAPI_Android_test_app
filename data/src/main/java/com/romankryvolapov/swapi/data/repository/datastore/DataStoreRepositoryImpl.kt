/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.repository.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.data.BuildConfig.DATASTORE_NAME
import com.romankryvolapov.swapi.data.utils.ApplicationInfoSerializer
import com.romankryvolapov.swapi.domain.defaultApplicationInfo
import com.romankryvolapov.swapi.domain.mappers.ApplicationInfoMapper
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfoNullable
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

class DataStoreRepositoryImpl(
    private val applicationInfoMapper: ApplicationInfoMapper,
    private val adapter: JsonAdapter<ApplicationInfoNullable>,
    private val context: Context
) : DataStoreRepository {

    companion object {
        private const val TAG = "DataStoreRepositoryTag"
        private val Context.settingsDataStore: DataStore<ApplicationInfo> by dataStore(
            fileName = DATASTORE_NAME,
            serializer = ApplicationInfoSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler {
                logError("corruptionHandler", TAG)
                defaultApplicationInfo
            }
        )
    }


    override suspend fun saveApplicationInfo(value: ApplicationInfo): Boolean {
        return try {
            val saved = context.settingsDataStore.updateData { value }
            val result = saved != defaultApplicationInfo
            logDebug("saveApplicationInfo result: $result", TAG)
            result
        } catch (e: Exception) {
            logError("saveApplicationInfo failed", e, TAG)
            false
        }
    }

    override suspend fun readApplicationInfo(): ApplicationInfo {
        logDebug("readApplicationInfo", TAG)
        return try {
            context.settingsDataStore.data.first()
        } catch (e: Exception) {
            logError("readApplicationInfo failed", e, TAG)
            defaultApplicationInfo
        }
    }

    override suspend fun saveApplicationInfoFromJson(json: String): String? {
        logDebug("saveApplicationInfoFromJson json: $json", TAG)
        return try {
            val applicationInfo = adapter.fromJson(json)
            if (applicationInfo == null) {
                logError("saveApplicationInfoFromJson applicationInfo == null", TAG)
                return "ApplicationInfo is null"
            }
            saveApplicationInfo(applicationInfoMapper.reverse(applicationInfo))
            return null
        } catch (e: Exception) {
            logError(
                "saveApplicationInfoFromJson Exception: ${e.message}", e,
                TAG
            )
            return e.message
        }
    }

    override suspend fun readApplicationInfoToJson(): String? {
        logDebug("readApplicationInfoToJson", TAG)
        return try {
            val info = readApplicationInfo()
            val buffer = okio.Buffer()
            val writer = JsonWriter.of(buffer)
            writer.setIndent(" ")
            adapter.toJson(writer, applicationInfoMapper.map(info))
            buffer.readUtf8()
        } catch (e: Exception) {
            logError("readApplicationInfoToJson error", e, TAG)
            null
        }
    }

    override suspend fun restoreDefault(): Boolean {
        logDebug("logoutFromPreferences", TAG)
        return try {
            context.settingsDataStore.updateData { defaultApplicationInfo }
            true
        } catch (e: Exception) {
            logError("logoutFromPreferences failed", e, TAG)
            false
        }
    }

    override fun subscribeToApplicationInfo(): Flow<ApplicationInfo> {
        logDebug("subscribeToApplicationInfo", TAG)
        return context.settingsDataStore.data
            .catch { exception ->
                logError("observeApplicationInfo error", exception, TAG)
                emit(defaultApplicationInfo)
            }
    }

}