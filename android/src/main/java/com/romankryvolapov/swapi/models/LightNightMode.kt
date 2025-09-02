package com.romankryvolapov.swapi.models

import androidx.appcompat.app.AppCompatDelegate
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.TypeEnumInt
import com.romankryvolapov.swapi.models.common.StringSource

enum class LightNightMode(
    override val type: Int,
    val nameString: StringSource,
) : TypeEnumInt {
    LIGHT(
        type = AppCompatDelegate.MODE_NIGHT_NO,
        nameString = StringSource(R.string.view_mode_light),
    ),
    NIGHT(
        type = AppCompatDelegate.MODE_NIGHT_YES,
        nameString = StringSource(R.string.view_mode_night),
    ),
    SYSTEM(
        type = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        nameString = StringSource(R.string.view_mode_by_system),
    )
}