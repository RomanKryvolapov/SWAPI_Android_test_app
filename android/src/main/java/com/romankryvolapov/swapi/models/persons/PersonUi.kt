package com.romankryvolapov.swapi.models.persons

import android.os.Parcelable
import com.romankryvolapov.swapi.extensions.equalTo
import com.romankryvolapov.swapi.models.common.DiffEquals
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonUi(
    val name: String,
    val homeworldID: Int?,
) : DiffEquals, Parcelable {

    override fun isItemSame(other: Any?): Boolean {
        return equalTo(
            other,
            { name },
        )
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(
            other,
            { name },
            { homeworldID },
        )
    }


}