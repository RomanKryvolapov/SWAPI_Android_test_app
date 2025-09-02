/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.utils

import android.text.Editable
import android.text.TextWatcher

abstract class AfterTextChangedWatcher : TextWatcher {

    abstract fun afterTextChanged(text: String)

    final override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    final override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    final override fun afterTextChanged(s: Editable?) {
        afterTextChanged(s?.toString().orEmpty())
    }

}