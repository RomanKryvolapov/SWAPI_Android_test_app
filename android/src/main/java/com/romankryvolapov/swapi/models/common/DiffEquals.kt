/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

interface DiffEquals {

    fun isItemSame(other: Any?): Boolean

    fun isContentSame(other: Any?): Boolean

}