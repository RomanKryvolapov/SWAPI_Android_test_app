package com.romankryvolapov.swapi.models.planets

import android.os.Parcelable
import com.romankryvolapov.swapi.extensions.equalTo
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlanetUiTitle(
    val title: String,
    val description: String,
): PlanetAdapterMarker, Parcelable {

    override fun isItemSame(other: Any?): Boolean {
        return equalTo(
            other,
            { title },
        )
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(
            other,
            { title },
            { description },
        )
    }

}