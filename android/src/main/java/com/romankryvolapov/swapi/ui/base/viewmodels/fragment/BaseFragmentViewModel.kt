package com.romankryvolapov.swapi.ui.base.viewmodels.fragment

import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.base.viewmodels.BaseViewModel
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer

/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
abstract class BaseFragmentViewModel(
    currentContext: CurrentContext,
    inactivityTimer: InactivityTimer,
    dataStoreRepository: DataStoreRepository,
    protected val mainActivityViewModel: MainActivityViewModel
) : BaseViewModel(
    inactivityTimer = inactivityTimer,
    dataStoreRepository = dataStoreRepository,
    currentContext = currentContext,
) {

    companion object {
        private const val TAG = "BaseFragmentViewModelTag"
    }

    abstract val enterKeyAction: MainActivityViewModel.Companion.EnterKeyAction

    suspend fun getApplicationInfo(): ApplicationInfo {
        return mainActivityViewModel.getApplicationInfo()
    }

    override fun setupEnterAction() {
        mainActivityViewModel.enterKeyAction = enterKeyAction
    }

}