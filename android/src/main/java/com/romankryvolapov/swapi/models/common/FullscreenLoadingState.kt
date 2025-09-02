/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

sealed class FullscreenLoadingState  {

    data class Loading(
        val message: StringSource?,
    ) : FullscreenLoadingState()

    data object Ready : FullscreenLoadingState()

}