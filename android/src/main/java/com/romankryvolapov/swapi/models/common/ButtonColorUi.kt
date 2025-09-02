/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

import com.romankryvolapov.swapi.domain.models.common.TypeEnum

enum class ButtonColorUi(
    override val type: String,
) : TypeEnum {
    BLUE("BLUE"),
    RED("RED"),
    GREEN("GREEN"),
    ORANGE("ORANGE"),
    TRANSPARENT("TRANSPARENT"),
}