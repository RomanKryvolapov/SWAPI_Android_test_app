/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.base.viewmodels

import android.os.Bundle
import android.os.Looper
import android.view.KeyEvent
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.extensions.launchWithDispatcher
import com.romankryvolapov.swapi.extensions.navigateNewRoot
import com.romankryvolapov.swapi.extensions.navigateTo
import com.romankryvolapov.swapi.extensions.popBackStackToFragment
import com.romankryvolapov.swapi.extensions.readOnly
import com.romankryvolapov.swapi.extensions.setValueOnMainThread
import com.romankryvolapov.swapi.models.common.AlertDialogResult
import com.romankryvolapov.swapi.models.common.BannerMessage
import com.romankryvolapov.swapi.models.common.DialogMessage
import com.romankryvolapov.swapi.models.common.FullscreenLoadingState
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.models.common.UiState
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer
import com.romankryvolapov.swapi.utils.SingleLiveEventLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
abstract class BaseViewModel(
    val inactivityTimer: InactivityTimer,
    protected val dataStoreRepository: DataStoreRepository,
    protected val currentContext: CurrentContext,
) : ViewModel() {

    companion object {
        private const val TAG = "BaseViewModelTag"
    }

    protected open val isAuthorizationActive: Boolean = true


    private var flowNavControllerRef: WeakReference<NavController>? = null
    private var activityNavControllerRef: WeakReference<NavController>? = null
    private var tabNavControllerRef: WeakReference<NavController>? = null

    private var isAlreadyInitialized = false

    private val _closeActivityLiveData = SingleLiveEventLiveData<Unit>()
    val closeActivityLiveData = _closeActivityLiveData.readOnly()

    private val _backPressedFailedLiveData = SingleLiveEventLiveData<Unit>()
    val backPressedFailedLiveData = _backPressedFailedLiveData.readOnly()

    private val _hideKeyboardLiveData = SingleLiveEventLiveData<Unit>()
    val hideKeyboardLiveData = _hideKeyboardLiveData.readOnly()

    private val _showBannerMessageLiveData = SingleLiveEventLiveData<BannerMessage>()
    val showBannerMessageLiveData = _showBannerMessageLiveData.readOnly()

    private val _showDialogMessageLiveData = SingleLiveEventLiveData<DialogMessage>()
    val showDialogMessageLiveData = _showDialogMessageLiveData.readOnly()

    private val _showLoadingDialogLiveData = SingleLiveEventLiveData<FullscreenLoadingState>()
    val showLoadingDialogLiveData = _showLoadingDialogLiveData.readOnly()

    private val _uiStateLiveData = MutableLiveData<UiState>()
    val uiStateLiveData = _uiStateLiveData.readOnly()

    abstract fun onBackPressed()

    fun hideKeyboard() {
        _hideKeyboardLiveData.callOnMainThread()
    }

    private fun isMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    fun showLoader(message: String? = null) {
        _uiStateLiveData.setValueOnMainThread(
            UiState.Loading(
                message = message,
            )
        )
    }

    fun showFullscreenLoader(message: StringSource?) {
        _showLoadingDialogLiveData.setValueOnMainThread(
            FullscreenLoadingState.Loading(
                message = message,
            )
        )
    }

    fun hideFullscreenLoader(delay: Long? = null) {
        if (delay == null || delay == 0L) {
            _showLoadingDialogLiveData.setValueOnMainThread(FullscreenLoadingState.Ready)
        } else {
            viewModelScope.launchWithDispatcher {
                delay(delay)
                _showLoadingDialogLiveData.setValueOnMainThread(FullscreenLoadingState.Ready)
            }
        }
    }

    fun showEmptyState(
        title: StringSource? = null,
        description: StringSource? = null,
        actionButtonText: StringSource? = null,
    ) {
        logDebug("showEmptyState", TAG)
        _uiStateLiveData.setValueOnMainThread(
            UiState.Empty(
                title = title,
                description = description,
                actionButtonText = actionButtonText,
            )
        )
    }

    fun showReadyState() {
        logDebug("showReadyState", TAG)
        _uiStateLiveData.setValueOnMainThread(UiState.Ready)
    }

    fun showErrorState(
        iconRes: Int? = R.drawable.ic_error_red,
        title: StringSource? = StringSource(R.string.error_internal_error_short),
        description: StringSource? = null,
        actionOneButtonText: StringSource? = StringSource(R.string.close),
        actionTwoButtonText: StringSource? = null,
    ) {
        logDebug("showErrorState", TAG)
        _uiStateLiveData.setValueOnMainThread(
            UiState.Error(
                title = title,
                iconRes = iconRes,
                description = description,
                actionOneButtonText = actionOneButtonText,
                actionTwoButtonText = actionTwoButtonText,
            )
        )
    }

    fun hideErrorState() {
        logDebug("hideErrorState", TAG)
        _uiStateLiveData.setValueOnMainThread(UiState.Ready)
    }

    // Should be called only by Base
    fun onViewCreated() {
        logDebug("attachView isAlreadyInitialized: $isAlreadyInitialized", TAG)
        _uiStateLiveData.value = UiState.Ready
        onCreated()
        inactivityTimer.setTimerCoroutineScope(viewModelScope)
        if (isAlreadyInitialized) return
        isAlreadyInitialized = true
        onFirstAttach()
    }

    protected open fun setupEnterAction() {
        // Override when needed
    }

    /**
     * You should override this instead of init.
     * This method will be called after [BaseFragment::initViews] and
     * [BaseFlowFragment::onViewCreated] and also after [BaseActivity::onCreate]
     */
    open fun onEnterPressed() {
        // Override when needed
    }

    open fun onKeyboardClicked(event: KeyEvent) {
        // Override when needed
    }

    protected open fun onFirstAttach() {
        // Override when needed
    }

    open fun onCreated() {
        // Override when needed
    }

    open fun onResumed() {
        // Override when needed
    }

    open fun onPaused() {
        // Override when needed
    }

    open fun onStopped() {
        // Override when needed
    }

    open fun onDestroyed() {
        // Override when needed
    }

    open fun onDetached() {
        // Override when needed, viewModelScope will not work
    }

    open fun onAlertDialogResult() {
        // Override when needed
    }

    open fun onAlertDialogResult(result: AlertDialogResult) {
        // Override when needed
    }

    fun bindFlowNavController(navController: NavController) {
        flowNavControllerRef = WeakReference(navController)
    }

    fun unbindFlowNavController() {
        flowNavControllerRef?.clear()
        flowNavControllerRef = null
    }

    fun bindTabNavController(navController: NavController) {
        logDebug("bindTabNavController", TAG)
        tabNavControllerRef = WeakReference(navController)
    }

    fun unbindTabNavController() {
        logDebug("unbindTabNavController", TAG)
        tabNavControllerRef?.clear()
        tabNavControllerRef = null
    }

    fun bindActivityNavController(navController: NavController) {
        activityNavControllerRef = WeakReference(navController)
    }

    fun unbindActivityNavController() {
        activityNavControllerRef?.clear()
        activityNavControllerRef = null
    }

    private fun findFlowNavController(): NavController {
        return flowNavControllerRef?.get()
            ?: throw IllegalArgumentException("Flow Navigation controller is not set!")
    }

    protected fun findActivityNavController(): NavController {
        return activityNavControllerRef?.get()
            ?: throw IllegalArgumentException("Activity Navigation controller is not set!")
    }

    protected fun findTabNavController(): NavController {
        return tabNavControllerRef?.get()
            ?: throw IllegalArgumentException("Tab Navigation controller is not set!")
    }

    @CallSuper
    open fun consumeException(exception: Throwable, isSilent: Boolean = false) {
        logDebug(exception.toString(), TAG)
    }

    protected fun showMessage(message: BannerMessage) {
        logDebug("showMessage BannerMessage", TAG)
        _showBannerMessageLiveData.setValueOnMainThread(message)
    }

    protected fun showBannerMessage(
        message: StringSource,
        state: BannerMessage.State,
        messageId: String? = null,
        title: StringSource? = null,
        gravity: BannerMessage.Gravity = BannerMessage.Gravity.START,
    ) {
        logDebug("showBannerMessage", TAG)
        _showBannerMessageLiveData.setValueOnMainThread(
            BannerMessage(
                title = title,
                state = state,
                message = message,
                gravity = gravity,
                messageID = messageId,
            )
        )
    }

    protected fun showMessage(message: DialogMessage) {
        logDebug("showMessage DialogMessage", TAG)
        _showDialogMessageLiveData.setValueOnMainThread(message)
    }

    protected fun showDialogMessage(
        messageID: String? = null,
        title: StringSource? = null,
        message: StringSource,
        state: DialogMessage.State = DialogMessage.State.ERROR,
        gravity: DialogMessage.Gravity = DialogMessage.Gravity.START,
        positiveButtonText: StringSource? = StringSource(R.string.yes),
        negativeButtonText: StringSource? = StringSource(R.string.no),
        @DrawableRes icon: Int? = R.drawable.ic_error,
        dialogData: String? = null,
    ) {
        logDebug("showDialogMessage", TAG)
        _showDialogMessageLiveData.setValueOnMainThread(
            DialogMessage(
                icon = icon,
                title = title,
                state = state,
                message = message,
                gravity = gravity,
                messageID = messageID,
                dialogData = dialogData,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
            )
        )
    }

    protected fun popBackStack() {
        try {
            if (isMainThread()) {
                if (!findFlowNavController().popBackStack()) {
                    _backPressedFailedLiveData.call()
                }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    try {
                        if (!findFlowNavController().popBackStack()) {
                            _backPressedFailedLiveData.call()
                        }
                    } catch (e: Exception) {
                        logError("bobBackStack Exception: ${e.message}", e, "NavController")
                    }
                }
            }
        } catch (e: Exception) {
            logError("bobBackStack Exception: ${e.message}", e, "NavController")
        }
    }

    protected fun navigateUp() {
        try {
            if (isMainThread()) {
                if (!findFlowNavController().navigateUp()) {
                    _backPressedFailedLiveData.call()
                }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    try {
                        if (!findFlowNavController().navigateUp()) {
                            _backPressedFailedLiveData.call()
                        }
                    } catch (e: Exception) {
                        logError("bobBackStack Exception: ${e.message}", e, "NavController")
                    }
                }
            }
        } catch (e: Exception) {
            logError("bobBackStack Exception: ${e.message}", e, "NavController")
        }
    }

    // activity

    protected fun navigateInActivity(directions: NavDirections) {
        findActivityNavController().navigateTo(
            directions = directions,
            viewModelScope = viewModelScope,
        )
    }

    protected fun navigateInActivity(@IdRes fragment: Int) {
        findActivityNavController().navigateTo(
            fragment = fragment,
            viewModelScope = viewModelScope,
        )
    }

    fun navigateInActivity(
        @IdRes fragment: Int,
        bundle: Bundle,
    ) {
        findActivityNavController().navigateTo(
            fragment = fragment,
            bundle = bundle,
            viewModelScope = viewModelScope,
        )
    }

    // activity

    protected fun navigateInTab(directions: NavDirections) {
        findTabNavController().navigateTo(
            directions = directions,
            viewModelScope = viewModelScope,
        )
    }

    protected fun navigateInTab(@IdRes fragment: Int) {
        findTabNavController().navigateTo(
            fragment = fragment,
            viewModelScope = viewModelScope,
        )
    }

    // activity

    protected fun navigateInFlow(directions: NavDirections) {
        findFlowNavController().navigateTo(
            directions = directions,
            viewModelScope = viewModelScope,
        )
    }

    protected fun navigateInFlow(@IdRes fragment: Int) {
        findFlowNavController().navigateTo(
            fragment = fragment,
            viewModelScope = viewModelScope,
        )
    }

    fun navigateInFlow(
        @IdRes fragment: Int,
        bundle: Bundle,
    ) {
        findFlowNavController().navigateTo(
            fragment = fragment,
            bundle = bundle,
            viewModelScope = viewModelScope,
        )
    }

    protected fun navigateNewRootInActivity(directions: NavDirections) {
        findActivityNavController().navigateNewRoot(
            directions = directions,
            viewModelScope = viewModelScope,
        )
    }

    protected fun navigateNewRootInFlow(directions: NavDirections) {
        findFlowNavController().navigateNewRoot(
            directions = directions,
            viewModelScope = viewModelScope,
        )
    }

    protected fun popBackStackToFragment(@IdRes fragment: Int) {
        logDebug("popBackStackToFragment", TAG)
        findFlowNavController().popBackStackToFragment(
            fragment = fragment,
            viewModelScope = viewModelScope,
        )
    }

    protected fun navigateNewRootInActivity(
        @IdRes fragment: Int,
        bundle: Bundle? = null
    ) {
        findActivityNavController().navigateNewRoot(
            fragment = fragment,
            bundle = bundle,
            viewModelScope = viewModelScope,
        )
    }

    protected fun navigateNewRootInFlow(
        @IdRes fragment: Int,
        bundle: Bundle? = null
    ) {
        findFlowNavController().navigateNewRoot(
            fragment = fragment,
            bundle = bundle,
            viewModelScope = viewModelScope,
        )
    }

    protected fun closeActivity() {
        _closeActivityLiveData.callOnMainThread()
    }

    @CallSuper
    @VisibleForTesting(otherwise = VisibleForTesting.Companion.PROTECTED)
    public override fun onCleared() {
        logDebug("onCleared", TAG)
        unbindFlowNavController()
        unbindActivityNavController()
        unbindTabNavController()
//        disposables.clear()
        super.onCleared()
    }

    fun logout() {
        showMessage(BannerMessage.Companion.error("User not setup, please go through the registration process"))

    }

    fun fragmentOnResume() {
        logDebug("fragmentOnResume isAuthorizationActive: $isAuthorizationActive", TAG)
        inactivityTimer.fragmentOnResume(isAuthorizationActive)
        setupEnterAction()
    }

    suspend fun saveApplicationInfo(info: ApplicationInfo) {
        logDebug("saveApplicationInfo", TAG)
        dataStoreRepository.saveApplicationInfo(info)
    }

}