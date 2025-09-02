/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.models.application

data class ApplicationInfoNullable(
    val author: String?,
    val version: String?,
    val searchText: String?,
    val accessToken: String?,
    val applicationLanguage: ApplicationLanguage?,
)