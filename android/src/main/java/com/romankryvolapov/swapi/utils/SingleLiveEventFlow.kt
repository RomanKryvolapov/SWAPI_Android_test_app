/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

class SingleLiveEventFlow<T> : Flow<T> {

    private val internalFlow = MutableSharedFlow<T>(
        replay = 0,
        extraBufferCapacity = 1
    )

    override suspend fun collect(collector: FlowCollector<T>) {
        internalFlow.collect(collector)
    }

    fun emit(value: T) {
        internalFlow.tryEmit(value)
    }

    fun readOnly(): Flow<T> = this

}