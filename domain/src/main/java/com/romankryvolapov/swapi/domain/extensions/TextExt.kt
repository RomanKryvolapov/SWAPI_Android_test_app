/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.extensions

import android.util.Base64
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

private const val TAG = "TextExtTag"

fun String.toBase64(): String {
    return try {
        Base64.encodeToString(toByteArray(charset("UTF-8")), Base64.NO_WRAP)
    } catch (e: Exception) {
        logError("toBase64 Exception: ${e.message}", e, TAG)
        ""
    }
}

fun String.fromBase64(): String {
    return try {
        String(Base64.decode(this, Base64.NO_WRAP), charset("UTF-8"))
    } catch (e: Exception) {
        logError("toBase64 Exception: ${e.message}", e, TAG)
        ""
    }
}

fun Key.toBase64(): String {
    return try {
        Base64.encodeToString(encoded, Base64.NO_WRAP)
    } catch (e: Exception) {
        logError("toBase64 Exception: ${e.message}", e, TAG)
        ""
    }
}

fun ByteArray.toBase64(): String {
    return try {
        Base64.encodeToString(this, Base64.NO_WRAP)
    } catch (e: Exception) {
        logError("toBase64 Exception: ${e.message}", e, TAG)
        ""
    }
}

fun String.normalize(): String {
    return trimIndent().replace(Regex("""\A(\s*[\r\n])+|([\r\n]\s*)+\z"""), "")
}

fun String.ellipsize(maxLength: Int): String {
    if (length <= maxLength) return this
    return take(maxLength.coerceAtLeast(0).coerceAtMost(length)) + "..."
}

fun getDeviceUUID(): String {
    return UUID.randomUUID().toString()
}

fun String.toSeparateWorldsList(separator: String = ","): List<String> {
    return split(separator)
        .filter { it.isNotEmpty() }
}

fun String.containsKeyWorld(keyWorlds: String, separator: String = ","): Boolean {
    return keyWorlds.split(separator)
        .filter { it.isNotEmpty() }
        .any {
            this.contains(
                other = it,
                ignoreCase = true
            )
        }
}

fun String.containsKeyWorld(keyWorldsList: List<String>, separator: String = ","): Boolean {
    logDebug("containsKeyWorld key: $this keyWorldsList: $keyWorldsList", TAG)
    return keyWorldsList.filter {
        it.isNotEmpty()
    }.any {
        this.contains(
            other = it,
            ignoreCase = true
        )
    }
}

fun StringBuilder.replaceFirst(oldValue: String, newValue: String): Boolean {
    val index = indexOf(oldValue)
    return if (index != -1) {
        replace(index, index + oldValue.length, newValue)
        true
    } else {
        false
    }
}

fun StringBuilder.replaceAll(oldValue: String, newValue: String): Int {
    var count = 0
    var index = indexOf(oldValue)
    while (index != -1) {
        replace(index, index + oldValue.length, newValue)
        index = indexOf(oldValue, index + newValue.length)
        count++
    }
    return count
}


inline fun String?.notEmpty(
    crossinline text: (String) -> Unit,
) {
    if (!this.isNullOrEmpty()) {
        text(this)
    }
}

inline fun String?.notEmpty(
    crossinline text: (String) -> Unit,
    crossinline empty: () -> Unit,
) {
    if (!this.isNullOrEmpty()) {
        text(this)
    } else {
        empty()
    }
}

fun String.cyrillicToLatin(): String {
    val abcCyr = charArrayOf(
        ' ',
        'а',
        'б',
        'в',
        'г',
        'д',
        'е',
        'ё',
        'ж',
        'з',
        'и',
        'й',
        'к',
        'л',
        'м',
        'н',
        'о',
        'п',
        'р',
        'с',
        'т',
        'у',
        'ф',
        'х',
        'ц',
        'ч',
        'ш',
        'щ',
        'ъ',
        'ы',
        'ь',
        'э',
        'ю',
        'я',
        'А',
        'Б',
        'В',
        'Г',
        'Д',
        'Е',
        'Ё',
        'Ж',
        'З',
        'И',
        'Й',
        'К',
        'Л',
        'М',
        'Н',
        'О',
        'П',
        'Р',
        'С',
        'Т',
        'У',
        'Ф',
        'Х',
        'Ц',
        'Ч',
        'Ш',
        'Щ',
        'Ъ',
        'Ы',
        'Ь',
        'Э',
        'Ю',
        'Я',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z'
    )
    val abcLat = arrayOf(
        " ",
        "a",
        "b",
        "v",
        "g",
        "d",
        "e",
        "e",
        "zh",
        "z",
        "i",
        "y",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "r",
        "s",
        "t",
        "u",
        "f",
        "h",
        "ts",
        "ch",
        "sh",
        "sch",
        "",
        "i",
        "",
        "e",
        "ju",
        "ja",
        "A",
        "B",
        "V",
        "G",
        "D",
        "E",
        "E",
        "Zh",
        "Z",
        "I",
        "Y",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "R",
        "S",
        "T",
        "U",
        "F",
        "H",
        "Ts",
        "Ch",
        "Sh",
        "Sch",
        "",
        "I",
        "",
        "E",
        "Ju",
        "Ja",
        "a",
        "b",
        "c",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "w",
        "x",
        "y",
        "z",
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )
    val builder = StringBuilder()
    for (element in this) {
        for (x in abcCyr.indices) {
            if (element == abcCyr[x]) {
                builder.append(abcLat[x])
            }
        }
    }
    return builder.toString()
}


fun String.capitalized(): String {
    return this.substring(0, 1).uppercase() + this.substring(1).lowercase();
}


@Throws(NoSuchAlgorithmException::class)
fun String.sha256(): String? {
    return try {
        val bytes = this.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val result = StringBuilder()
        for (byte in digest) {
            result.append(String.format("%02x", byte))
        }
        result.toString()
    } catch (e: Exception) {
        logError("sha256() Exception: ${e.message}", e, "sha256Tag")
        null
    }
}

val Long.bytesToMegabytes: Int
    get() = (this / (1024.0 * 1024.0)).toInt()

fun Long.toSpaced() = "%,d".format(this).replace(',', ' ')

fun Int.toSpaced() = "%,d".format(this).replace(',', ' ')

fun String.reformatDateTime(
    inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSX",
    outputPattern: String = "dd.MM.yyyy",
    isZoned: Boolean = true
): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern(inputPattern)
        val outputFormatter = DateTimeFormatter.ofPattern(outputPattern)
        if (isZoned) {
            val zoned = ZonedDateTime.parse(this, inputFormatter)
            zoned.format(outputFormatter)
        } else {
            val local = LocalDateTime.parse(this, inputFormatter)
            local.format(outputFormatter)
        }
    } catch (e: DateTimeParseException) {
        this
    }
}

fun String.containsCaseIgnore(with: String): Boolean {
    return this.lowercase().contains(with.lowercase())
}

fun String.equalsCaseIgnore(with: String): Boolean {
    return this.lowercase() == with.lowercase()
}