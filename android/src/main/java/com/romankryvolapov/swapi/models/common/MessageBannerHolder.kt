/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

import android.view.View
import androidx.annotation.DrawableRes
import com.romankryvolapov.swapi.R

interface MessageBannerHolder {

    fun showBannerMessage(
        icon: Int? = null,
        message: StringSource,
        state: BannerMessage.State,
        messageID: String? = null,
        title: StringSource? = null,
        gravity: BannerMessage.Gravity = BannerMessage.Gravity.START,
        anchorView: View? = null,
        interval: BannerMessage.Interval = BannerMessage.Interval.MIDDLE,
        transparent: Boolean = false,
    )

    fun showDialogMessage(
        messageID: String? = null,
        title: StringSource? = null,
        message: StringSource,
        state: DialogMessage.State = DialogMessage.State.ERROR,
        gravity: DialogMessage.Gravity = DialogMessage.Gravity.START,
        positiveButtonText: StringSource? = StringSource(R.string.yes),
        negativeButtonText: StringSource? = StringSource(R.string.no),
        @DrawableRes icon: Int? = R.drawable.ic_error,
        dialogData: String? = null,
    )

    fun showFullscreenLoader(message: StringSource?)

    fun hideFullscreenLoader()

}