package com.romankryvolapov.swapi.ui.base.viewmodels.activity

import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.ui.base.viewmodels.BaseViewModel
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer

/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
abstract class BaseActivityViewModel(
    currentContext: CurrentContext,
    inactivityTimer: InactivityTimer,
    dataStoreRepository: DataStoreRepository,
) : BaseViewModel(
    currentContext = currentContext,
    inactivityTimer = inactivityTimer,
    dataStoreRepository = dataStoreRepository,
) {

    companion object {
        private const val TAG = "BaseActivityViewModelTag"
    }

}