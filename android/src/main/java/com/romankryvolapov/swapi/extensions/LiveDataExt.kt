/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.extensions

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.readOnly(): LiveData<T> = this

@MainThread
fun MutableLiveData<Unit>.call() {
    this.value = Unit
}

fun <T> MutableLiveData<T>.setValueOnMainThread(value: T?) {
    if (isMainThread()) {
        this.value = value
    } else {
        Handler(Looper.getMainLooper()).post {
            this.value = value
        }
    }
}

fun <T> MutableLiveData<T>.postValueOnMainThread(value: T?) {
    if (isMainThread()) {
        postValue(value)
    } else {
        Handler(Looper.getMainLooper()).post {
            postValue(value)
        }
    }
}