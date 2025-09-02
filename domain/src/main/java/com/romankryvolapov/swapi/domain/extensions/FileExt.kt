/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.extensions

import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun File.getSizeString(): String {
    val bytes = length()
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }
    val format = DecimalFormat("#,##0.#", symbols)
    return when {
        gb >= 1 -> "${format.format(gb)} GB"
        mb >= 1 -> "${format.format(mb)} MB"
        kb >= 1 -> "${format.format(kb)} KB"
        else -> "${format.format(bytes)} B"
    }
}