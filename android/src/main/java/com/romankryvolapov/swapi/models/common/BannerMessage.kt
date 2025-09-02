/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.romankryvolapov.swapi.R

data class BannerMessage(
    val messageID: String? = null,
    val title: StringSource? = null,
    val message: StringSource,
    @DrawableRes val icon: Int? = null,
    val state: State = State.ERROR,
    val gravity: Gravity = Gravity.START,
    val interval: BannerMessage.Interval = BannerMessage.Interval.MIDDLE,
    val transparent: Boolean = false,
) {

    enum class State {
        ERROR,
        SUCCESS
    }

    enum class Gravity {
        START,
        CENTER
    }

    enum class Interval(
        val milliseconds: Int,
    ) {
        SHORT(500),
        MIDDLE(1000),
        LONG(2000)
    }

    companion object {
        /**
         * Red error banners with [message] and error [icon] and gravity start.
         */
        fun error(
            message: String,
        ): BannerMessage {
            return BannerMessage(
                message = StringSource(message),
                icon = R.drawable.ic_error,
                state = State.ERROR
            )
        }

        fun error(
            @StringRes message: Int,
        ): BannerMessage {
            return BannerMessage(
                message = StringSource(message),
                icon = R.drawable.ic_error,
                state = State.ERROR
            )
        }

        /**
         * Green success banners with [message] and success [icon] and gravity start.
         */
        fun success(
            message: String,
        ): BannerMessage {
            return BannerMessage(
                message = StringSource(message),
                icon = R.drawable.ic_success,
                state = State.SUCCESS
            )
        }

        fun success(
            @StringRes message: Int,
        ): BannerMessage {
            return BannerMessage(
                message = StringSource(message),
                icon = R.drawable.ic_success,
                state = State.SUCCESS
            )
        }

        /**
         * Green success banners with simple [message] in the center of the banner.
         */
        fun successCenter(message: String): BannerMessage {
            return BannerMessage(
                message = StringSource(message),
                icon = null,
                state = State.SUCCESS,
                gravity = Gravity.CENTER
            )
        }

        fun successCenter(@StringRes message: Int): BannerMessage {
            return BannerMessage(
                message = StringSource(message),
                icon = null,
                state = State.SUCCESS,
                gravity = Gravity.CENTER
            )
        }
    }
}