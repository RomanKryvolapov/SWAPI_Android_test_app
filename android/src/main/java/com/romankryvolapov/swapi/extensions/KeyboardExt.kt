/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.extensions

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError

private const val TAG = "KeyboardExt"

/**
 * This extension changes keyboard to numberPassword but without
 * password conversion into dots if the [isPassword] is false.
 * The password keyboard is more cool the the number one.
 */
fun AppCompatEditText.applyCustomNumberKeyboardOption(isPassword: Boolean = false) {
    inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_CLASS_NUMBER
    if (!isPassword) {
        transformationMethod = HideReturnsTransformationMethod.getInstance()
    }
}

/**
 * Block a keyboard closing after done action of the edit text
 * @param listener - the done action listener
 */
fun AppCompatEditText.setOnKeyBlockedListener(listener: () -> Unit) {
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            listener.invoke()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
}

// Show/Hide keyboard methods for different sources.

fun AppCompatEditText.showKeyboard(activity: Activity) {
    showKeyboard(activity.window)
}

fun AppCompatEditText.showKeyboard() {
    if (requestFocus()) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        post {
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    isCursorVisible = true
}

fun AppCompatEditText.hideKeyboard() {
    clearFocus()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (windowToken != null) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } else {
        logError("Window token is null", TAG)
    }
}

fun AppCompatEditText.showKeyboard(window: Window) {
    requestFocus()
    WindowCompat.getInsetsController(window, window.decorView).show(WindowInsetsCompat.Type.ime())
}

fun Activity.hideKeyboardCompat() {
    window.decorView.post {
        WindowCompat.getInsetsController(window, window.decorView)
            .hide(WindowInsetsCompat.Type.ime())
    }
}

fun Activity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    if (view.windowToken != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    } else {
        logError("Window token is null", TAG)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (windowToken != null) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } else {
        logError("Window token is null", TAG)
    }
    clearFocus()
}

fun View.showKeyboard() {
    if (requestFocus()) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        post {
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

fun View.hideKeyboard(window: Window) {
    WindowCompat.getInsetsController(window, this).hide(WindowInsetsCompat.Type.ime())
}