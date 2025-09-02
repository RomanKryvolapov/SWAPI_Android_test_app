/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.utils

import android.os.Looper
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError

private const val TAG = "ThreadHelperTag"

fun checkNotMainThread() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        val stackTraceElement = Thread.currentThread().stackTrace
        val className = stackTraceElement[2].className.substringAfterLast('.')
        val methodName = stackTraceElement[2].methodName
        logError(
            "Function $methodName of class $className should not be called from the main thread",
            TAG
        )
    }
}