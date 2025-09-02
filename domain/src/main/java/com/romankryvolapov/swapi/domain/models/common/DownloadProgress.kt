/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.models.common

sealed class DownloadProgress {

    data class Loading(
        val message: String? = null,
    ) : DownloadProgress()

    data object Ready : DownloadProgress()

}