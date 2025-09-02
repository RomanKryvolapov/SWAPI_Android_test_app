/**
 * Please follow code style when editing project
 * Please follow principles of clean architecture
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.base.fragments.flow

import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.base.viewmodels.fragment.BaseFragmentViewModel
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer

abstract class BaseFlowViewModel(
    currentContext: CurrentContext,
    inactivityTimer: InactivityTimer,
    dataStoreRepository: DataStoreRepository,
    mainActivityViewModel: MainActivityViewModel,
) : BaseFragmentViewModel(
    currentContext = currentContext,
    inactivityTimer = inactivityTimer,
    dataStoreRepository = dataStoreRepository,
    mainActivityViewModel = mainActivityViewModel,
) {

    companion object {
        private const val TAG = "BaseFlowViewModelTag"
    }

    final override fun onBackPressed() {
        logError("onBackPressed", TAG)
        // not need
    }

}