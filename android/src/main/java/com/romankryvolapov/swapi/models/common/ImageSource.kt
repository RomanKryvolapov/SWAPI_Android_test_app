/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

import androidx.annotation.DrawableRes

sealed interface ImageSource {

    data class Url(
        val url: String?,
        @DrawableRes val placeholder: Int,
        @DrawableRes val error: Int,
    ) : ImageSource

    data class Res(
        @DrawableRes val res: Int,
    ) : ImageSource

}