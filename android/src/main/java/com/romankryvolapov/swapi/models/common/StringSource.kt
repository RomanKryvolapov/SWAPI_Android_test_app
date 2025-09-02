/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

import android.content.Context
import androidx.annotation.StringRes
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.utils.CurrentContext

class StringSource private constructor(
    private val text: String? = null,
    @StringRes private val resId: Int? = null,
    private val sources: List<StringSource>? = null,
    private val separator: CharSequence = ", ",
    private vararg val formatArgs: String?
) {

    constructor(text: String?, vararg formatArgs: String?) : this(
        text = text,
        resId = null,
        sources = null,
        separator = ", ",
        formatArgs = formatArgs
    )

    constructor(@StringRes resId: Int, vararg formatArgs: String?) : this(
        text = null,
        resId = resId,
        sources = null,
        separator = ", ",
        formatArgs = formatArgs
    )

    fun getString(context: Context): String {
        return when {
            text != null -> if (formatArgs.isNotEmpty()) text.format(*formatArgs) else text
            resId != null -> if (formatArgs.isNotEmpty()) context.getString(
                resId,
                *formatArgs
            ) else context.getString(resId)

            sources != null -> sources.joinToString(separator) { it.getString(context) }
            else -> context.getString(R.string.unknown)
        }
    }

    fun getString(context: CurrentContext): String {
        return when {
            text != null -> if (formatArgs.isNotEmpty()) text.format(*formatArgs) else text
            resId != null -> if (formatArgs.isNotEmpty()) context.getString(
                resId,
                *formatArgs
            ) else context.getString(resId)

            sources != null -> sources.joinToString(separator) { it.getString(context) }
            else -> context.getString(R.string.unknown)
        }
    }

    fun addTextEnd(text: String): StringSource {
        return StringSource(
            text = this.text + text,
            resId = this.resId,
            sources = null,
            separator = separator,
            formatArgs = formatArgs
        )
    }

    fun addTextStart(text: String): StringSource {
        return StringSource(
            text = text + this.text,
            resId = this.resId,
            sources = null,
            separator = separator,
            formatArgs = formatArgs
        )
    }

    fun addTextEnd(text: StringSource): StringSource {
        return StringSource(
            text = this.text + text.text,
            resId = this.resId,
            sources = null,
            separator = separator,
            formatArgs = formatArgs
        )
    }

    fun addTextStart(text: StringSource): StringSource {
        return StringSource(
            text = text.text + this.text,
            resId = this.resId,
            sources = null,
            separator = separator,
            formatArgs = formatArgs
        )
    }
}
