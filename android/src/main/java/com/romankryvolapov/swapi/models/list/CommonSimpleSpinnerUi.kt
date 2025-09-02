/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.list

import com.romankryvolapov.swapi.models.common.StringSource
import java.util.UUID

data class CommonSimpleSpinnerUi<T>(
    val id: UUID = UUID.randomUUID(),
    val valueFirstLine: StringSource,
    val valueSecondLine: StringSource? = null,
    val valueThirdLine: StringSource? = null,
    val data: T?,
    val colour: CommonSimpleSpinnerColour = CommonSimpleSpinnerColour.WHITE,
)