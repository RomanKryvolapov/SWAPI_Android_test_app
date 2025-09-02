/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain

import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import com.romankryvolapov.swapi.domain.models.application.ApplicationLanguage
import com.romankryvolapov.swapi.domain.models.common.MockResponse
import java.io.File

const val DEBUG_LOGOUT_FROM_PREFERENCES = false
const val DEBUG_PRINT_PREFERENCES_INFO = true
const val DEBUG_MOCK_INTERCEPTOR_ENABLED = true
const val DEBUG_FORCE_REPLACE_ASSETS = true
const val DEFAULT_INACTIVITY_TIMEOUT_MILLISECONDS = 120000L
const val DEFAULT_KEY = "Default"

val mockResponses = mutableMapOf<String, MockResponse>().apply {
    put(
        key = "",
        value = MockResponse(
            isEnabled = false,
            body = "",
            message = "",
            serverCode = 200,
        )
    )
}

// filesDir, cacheDir, codeCacheDir, noBackupFilesDir   -> Internal Storage
// getExternalFilesDir, getExternalCacheDir, getExternalMediaDirs -> External Storage

lateinit var externalFilesDirectory: File

lateinit var internalFilesDirectory: File

val defaultApplicationInfo = ApplicationInfo(
    author = "",
    version = "",
    searchText = "",
    accessToken = null,
    applicationLanguage = ApplicationLanguage.DEVICE,
)