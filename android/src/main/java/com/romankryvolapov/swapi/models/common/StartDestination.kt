/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

import android.os.Bundle
import androidx.annotation.IdRes

data class StartDestination(
    @IdRes val destination: Int,

    /**
     * Use this variable to pass arguments to the start destination of
     * the [destination] graph.
     */
    val arguments: Bundle? = null
)