/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.fragments.start.flow

import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel.Companion.EnterKeyAction
import com.romankryvolapov.swapi.ui.base.fragments.flow.BaseFlowViewModel
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer

class StartFlowViewModel(
    currentContext: CurrentContext,
    inactivityTimer: InactivityTimer,
    dataStoreRepository: DataStoreRepository,
    mainActivityViewModel: MainActivityViewModel,
) : BaseFlowViewModel(
    currentContext = currentContext,
    inactivityTimer = inactivityTimer,
    dataStoreRepository = dataStoreRepository,
    mainActivityViewModel = mainActivityViewModel,
) {

    companion object {
        private const val TAG = "StartFlowViewModelTag"
    }

    override val enterKeyAction = EnterKeyAction.NOTHING

}