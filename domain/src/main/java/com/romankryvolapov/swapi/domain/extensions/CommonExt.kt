/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.extensions

import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.models.common.TypeEnum
import com.romankryvolapov.swapi.domain.models.common.TypeEnumInt

inline fun <reified T : Enum<T>> getEnumValue(type: String): T? {
    val values = enumValues<T>()
    return values.firstOrNull {
        it is TypeEnum && (it as TypeEnum).type.equals(type, true)
    }
}

inline fun <reified T : Enum<T>> getEnumIntValue(type: Int): T? {
    val values = enumValues<T>()
    return values.firstOrNull {
        it is TypeEnumInt && (it as TypeEnumInt).type == type
    }
}

fun <T> List<T>.nextOrFirst(current: T): T {
    val index = indexOf(current)
    if (index == -1) return current
    return if (index + 1 < size) this[index + 1] else this.first()
}

fun <T> List<T>.nextOrFirstOrCurrent(current: T): T {
    val index = indexOf(current)
    if (index == -1) return current
    return if (size == 1) current else if (index + 1 < size) this[index + 1] else this.first()
}

fun <K, V> Map<K, V>.print(
    tag: String,
    dropAfter: Int = 100,
) {
    if (isEmpty()) {
        logError("Map is empty", tag)
        return
    }
    for ((key, value) in entries) {
        val keyString = key.toString()
            .replace(Regex("[\r\n]+"), " ")
            .replace(Regex(" {2}"), " ")
        val valueString = value?.toString()
            .orEmpty()
            .replace(Regex("[\r\n]+"), " ")
            .replace(Regex(" {2}"), " ")
        val displayValue = if (valueString.length > dropAfter) {
            valueString.take(dropAfter) + "... (${valueString.length} symbols)"
        } else {
            valueString
        }
        logDebug("$keyString : $displayValue", tag)
    }
}

