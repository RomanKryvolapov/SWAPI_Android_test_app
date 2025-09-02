/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.models.common

data class MockResponse(
    val isEnabled: Boolean,
    val body: String,
    val message: String,
    val serverCode: Int,
    val contentType: String = "application/json"
)