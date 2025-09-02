/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.extensions

import androidx.lifecycle.LifecycleCoroutineScope
import com.romankryvolapov.swapi.utils.SingleLiveEventFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

fun <T> Flow<T>.launchInScope(
    lifecycleScope: LifecycleCoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    lifecycleScope.launch {
        this@launchInScope.flowOn(dispatcher)
            .collect()
    }
}

fun <T> Flow<T>.launchInScope(
    viewModelScope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    viewModelScope.launch {
        this@launchInScope.flowOn(dispatcher)
            .collect()
    }
}

fun <T> Flow<T>.launchInJob(
    lifecycleScope: LifecycleCoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
): Job {
    return lifecycleScope.launch {
        this@launchInJob.flowOn(dispatcher)
            .collect()
    }
}

fun <T> Flow<T>.launchInJob(
    viewModelScope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Job {
    return viewModelScope.launch {
        this@launchInJob.flowOn(dispatcher)
            .collect()
    }
}

fun SingleLiveEventFlow<Unit>.call() {
    emit(Unit)
}