/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

sealed class UiState {

    data object Ready : UiState()

    data class Empty(
        val title: StringSource? = null,
        val description: StringSource? = null,
        val actionButtonText: StringSource? = null,
    ) : UiState()

    data class Loading(
        val message: String? = null,
    ) : UiState()

    data class Error(
        val iconRes: Int? = null,
        val title: StringSource? = null,
        val description: StringSource? = null,
        val actionOneButtonText: StringSource? = null,
        val actionTwoButtonText: StringSource? = null
    ) : UiState()
}