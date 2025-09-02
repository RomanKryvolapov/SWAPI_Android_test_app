/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.models.common

import com.romankryvolapov.swapi.domain.models.common.TypeEnum

enum class SelectionState (override val type: String) : TypeEnum {
    SELECTED("SELECTED"),
    SELECTED_NOT_ACTIVE("SELECTED_NOT_ACTIVE"),
    NOT_SELECTED("NOT_SELECTED"),
    NOT_SELECTED_NOT_ACTIVE("NOT_SELECTED_NOT_ACTIVE"),
    INDETERMINATE("INDETERMINATE"),
    INDETERMINATE_NOT_ACTIVE("INDETERMINATE_NOT_ACTIVE"),
}