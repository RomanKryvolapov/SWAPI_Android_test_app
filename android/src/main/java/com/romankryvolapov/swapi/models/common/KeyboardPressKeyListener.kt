/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

import android.view.KeyEvent

interface KeyboardPressKeyListener {
    fun onEnterPressed()
    fun onKeyboardPressed(event: KeyEvent)
}