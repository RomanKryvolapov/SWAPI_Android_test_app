/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

data class CardScanBottomSheetContent(
    val cardCurrentPin: String,
    val cardNewPin: String? = null,
)
