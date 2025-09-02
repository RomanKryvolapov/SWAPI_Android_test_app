/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.fragments.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.romankryvolapov.swapi.NavActivityDirections
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.extensions.readOnly
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel.Companion.EnterKeyAction
import com.romankryvolapov.swapi.ui.base.viewmodels.fragment.BaseFragmentViewModel
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class StartViewModel(
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
        private const val TAG = "SplashViewModelTag"
    }

    override val enterKeyAction = EnterKeyAction.NOTHING

    private val _messageLiveData = MutableLiveData<String>()
    val messageLiveData = _messageLiveData.readOnly()

    override fun onResumed() {
        logDebug("onResumed", TAG)
    }

    override fun onFirstAttach() {
        logDebug("onFirstAttach", TAG)
        viewModelScope.launch(Dispatchers.IO) {
            navigateInActivity(
                NavActivityDirections.toMainFlowFragment()
            )
        }
    }

    override fun onBackPressed() {
        logDebug("onBackPressed", TAG)
        exitProcess(0)
    }

}