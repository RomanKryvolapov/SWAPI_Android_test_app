/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.models.application

import com.romankryvolapov.swapi.domain.models.common.TypeEnum
import java.util.Locale

/**
 * Please follow code style when editing project
 * Please follow principles of clean architecture
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
enum class ApplicationLanguage(
    override val type: String,
    val nameString: String
) : TypeEnum {
    DEVICE("", "System"),
    ENGLISH("en", "English"),
    GERMAN("de", "Deutsch"),
    FRENCH("fr", "Français"),
    SPANISH("es", "Española"),
    POLISH("pl", "Polski"),
    RUSSIAN("ru", "Русский"),
    UKRAINIAN("uk", "Українська");

    fun getLocale(): Locale =
        if (type.isBlank()) Locale.getDefault()
        else Locale.forLanguageTag(type)
}