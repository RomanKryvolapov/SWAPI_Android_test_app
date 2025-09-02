/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.activity.main

import android.content.Intent
import androidx.annotation.CallSuper
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.models.common.StartDestination
import com.romankryvolapov.swapi.ui.base.viewmodels.activity.BaseActivityViewModel
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer
import java.util.UUID

class MainActivityViewModel(
    currentContext: CurrentContext,
    inactivityTimer: InactivityTimer,
    dataStoreRepository: DataStoreRepository,
) : BaseActivityViewModel(
    currentContext = currentContext,
    inactivityTimer = inactivityTimer,
    dataStoreRepository = dataStoreRepository,
) {

    companion object {
        private const val TAG = "MainActivityViewModelTag"
        private var dialogID = UUID.randomUUID()

        enum class EnterKeyAction {
            ENTER,
            ACTION,
            NOTHING,
        }
    }

    var enterKeyAction = EnterKeyAction.ENTER

    fun getStartDestination(intent: Intent): StartDestination {
        return StartDestination(R.id.startFlowFragment)
    }

    suspend fun getApplicationInfo(): ApplicationInfo {
        return dataStoreRepository.readApplicationInfo()
    }

    override fun onFirstAttach() {
        logDebug("onFirstAttach", TAG)
    }

    override fun onCreated(){
        logDebug("onCreated", TAG)
    }

    override fun onResumed() {
        logDebug("onResumed", TAG)
        inactivityTimer.activityOnResume()
    }

    override fun onPaused() {
        logDebug("onPaused", TAG)
        inactivityTimer.activityOnPause()
    }

    override fun onDestroyed() {
        logDebug("onDestroyed", TAG)
        inactivityTimer.activityOnDestroy()
    }

    fun dispatchTouchEvent() {
        inactivityTimer.dispatchTouchEvent()
    }

    override fun onBackPressed() {
        logDebug("onBackPressed", TAG)
        if (!findActivityNavController().popBackStack()) {
            closeActivity()
        }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
    }

}