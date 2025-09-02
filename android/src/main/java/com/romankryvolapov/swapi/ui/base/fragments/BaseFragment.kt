/**
 * Created & Copyright 2025 by Roman Kryvolapov
 *
 * Activity lifecycle:
 *
 * onCreate(savedInstanceState: Bundle?)
 * onStart()
 * onRestart()
 * onResume()
 *
 * onPause()
 * onStop()
 * onDestroy()
 *
 * Fragment lifecycle:
 *
 * onAttach(context: Context)
 * onCreate()
 * onCreateView()
 * onViewCreated()
 * onViewStateRestored(savedInstanceState: Bundle?)
 * onStart()
 * onResume()
 *
 * onPause()
 * onStop()
 * onDestroyView()
 * onDestroy()
 * onDetach()
 *
 * View lifecycle:
 *
 * onAttachedToWindow()
 * requestLayout()
 * measure()
 * onMeasure()
 * layout()
 * onLayout()
 * invalidate()
 * dispatchToDraw()
 * draw()
 * onDraw()
 *
 */
package com.romankryvolapov.swapi.ui.base.fragments

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.extensions.findActivityNavController
import com.romankryvolapov.swapi.extensions.hideKeyboard
import com.romankryvolapov.swapi.extensions.pxDimen
import com.romankryvolapov.swapi.models.common.AlertDialogResult
import com.romankryvolapov.swapi.models.common.BannerMessage
import com.romankryvolapov.swapi.models.common.DialogMessage
import com.romankryvolapov.swapi.models.common.FullscreenLoadingState
import com.romankryvolapov.swapi.models.common.KeyboardPressKeyListener
import com.romankryvolapov.swapi.models.common.MessageBannerHolder
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.models.common.UiState
import com.romankryvolapov.swapi.ui.activity.main.MainActivity
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.base.fragments.flow.BaseFlowFragment
import com.romankryvolapov.swapi.ui.base.viewmodels.fragment.BaseFragmentViewModel
import com.romankryvolapov.swapi.ui.view.ComplexGestureRefreshView
import com.romankryvolapov.swapi.ui.view.LoaderView
import com.romankryvolapov.swapi.utils.AlertDialogResultListener
import com.romankryvolapov.swapi.utils.SoftKeyboardStateWatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

abstract class BaseFragment<VB : ViewBinding, VM : BaseFragmentViewModel> : Fragment(),
    MessageBannerHolder,
    AlertDialogResultListener,
    KeyboardPressKeyListener {

    companion object {
        private const val TAG = "BaseFragmentTag"
        const val DIALOG_EXIT = "DIALOG_EXIT"
    }

    abstract val viewModel: VM

    private var viewBinding: VB? = null

    private var popupWindow: PopupWindow? = null

    private var listPopupWindow: ListPopupWindow? = null

    protected val mainActivityViewModel: MainActivityViewModel by activityViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = viewBinding!!

    private var keyboardIsOpened = false
    private var keyboardOpenJob: Job? = null

    private var keyboardStateWatcher: SoftKeyboardStateWatcher? = null
    private var keyboardStateListener =
        object : SoftKeyboardStateWatcher.SoftKeyboardStateListener {
            override fun onSoftKeyboardOpened(keyboardHeight: Int) {
                keyboardOpenJob?.cancel()
                keyboardOpenJob = lifecycleScope.launch {
                    if (!keyboardIsOpened) {
                        keyboardIsOpened = true
                        onKeyboardStateChanged(true)
                    }
                }
            }

            override fun onSoftKeyboardClosed() {
                keyboardOpenJob?.cancel()
                keyboardOpenJob = lifecycleScope.launch {
                    if (keyboardIsOpened) {
                        keyboardIsOpened = false
                        onKeyboardStateChanged(false)
                    }
                }
            }
        }

    abstract fun getViewBinding(): VB

    // lifecycle

    final override fun onAttach(context: Context) {
        super.onAttach(context)
        logDebug("onAttach", TAG)
    }

    final override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        logDebug("onAttach", TAG)
    }

    final override fun onStart() {
        super.onStart()
        logDebug("onStart", TAG)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        logDebug("onCreateView", TAG)
        viewBinding = getViewBinding()
        return viewBinding?.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logDebug("onViewCreated", TAG)
        setupNavControllers()
        subscribeToBaseViewModel()
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated()
        (activity as? MainActivity)?.alertDialogResultListener = this
        setupKeyboardStateListener()
        onCreated()
        parseArguments()
        setupView()
        setupControls()
        subscribeToLiveData()
    }

    private fun setupKeyboardStateListener() {
        keyboardStateWatcher = SoftKeyboardStateWatcher(requireActivity())
        keyboardStateWatcher?.setStatusBarOffset(getStatusBarHeight())
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.pxDimen(resourceId)
        } else 0
    }

    final override fun onResume() {
        logDebug("onResume", TAG)
        super.onResume()
        keyboardStateWatcher?.addSoftKeyboardStateListener(keyboardStateListener)
        if (this !is BaseFlowFragment) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        }
        (activity as? MainActivity)?.keyboardPressKeyListener = this
        onResumed()
        viewModel.fragmentOnResume()
        viewModel.onResumed()
    }

    final override fun onPause() {
        logDebug("onPause", TAG)
        super.onPause()
        hideSettingsMenu()
        onPaused()
        viewModel.onPaused()
        keyboardStateWatcher?.removeSoftKeyboardStateListener(keyboardStateListener)
        onKeyboardStateChanged(false)
    }

    final override fun onStop() {
        logDebug("onStop", TAG)
        super.onStop()
        onStopped()
        viewModel.onStopped()
    }

    final override fun onDestroyView() {
        logDebug("onDestroyView", TAG)
        viewBinding = null
        listPopupWindow?.dismiss()
        popupWindow?.dismiss()
        viewModel.unbindFlowNavController()
        viewModel.unbindActivityNavController()
        super.onDestroyView()
    }

    final override fun onDestroy() {
        logDebug("onDestroy", TAG)
        super.onDestroy()
        onDestroyed()
        viewModel.onDestroyed()
    }

    final override fun onDetach() {
        logDebug("onDetach", TAG)
        super.onDetach()
        onDetached()
        viewModel.onDetached()
    }

    final override fun onAlertDialogResultReady(result: AlertDialogResult) {
        logDebug("onAlertDialogResult", TAG)
        onAlertDialogResult(result)
        viewModel.onAlertDialogResult()
        viewModel.onAlertDialogResult(result)
    }

    final override fun onEnterPressed() {
        logDebug("onEnterPressed", TAG)
        viewModel.onEnterPressed()
    }

    final override fun onKeyboardPressed(event: KeyEvent) {
        viewModel.onKeyboardClicked(event)
    }

    protected open fun onCreated() {
        // Override when needed
    }

    protected open fun setupView() {
        // Override when needed
    }

    protected open fun setupControls() {
        // Override when needed
    }

    protected open fun subscribeToLiveData() {
        // Override when needed
    }

    open fun onAlertDialogResult(result: AlertDialogResult) {
        // Override when needed
    }

    protected open fun onResumed() {
        // Override when needed
    }

    protected open fun parseArguments() {
        // Override when needed
    }

    protected open fun onPaused() {
        // Override when needed
    }

    protected open fun onStopped() {
        // Override when needed
    }

    protected open fun onDestroyed() {
        // Override when needed
    }

    protected open fun onDetached() {
        // Override when needed
    }

    protected open fun onKeyboardStateChanged(isOpened: Boolean) {
        logDebug("onKeyboardStateChanged isOpened: $isOpened", TAG)
        // Override when needed
    }

    protected open fun setupNavControllers() {
        setupActivityNavController()
        viewModel.bindFlowNavController(findNavController())
    }

    protected fun setupActivityNavController() {
        viewModel.bindActivityNavController(findActivityNavController())
    }

    private fun subscribeToBaseViewModel() {
        viewModel.closeActivityLiveData.observe(viewLifecycleOwner) {
            activity?.finish()
        }
        viewModel.backPressedFailedLiveData.observe(viewLifecycleOwner) {
            try {
                ((parentFragment as? NavHostFragment)?.parentFragment as? BaseFlowFragment<*, *>)?.onExit()
            } catch (e: Exception) {
                logError("backPressedFailedLiveData Exception: ${e.message}", e, TAG)
            }
        }
        viewModel.hideKeyboardLiveData.observe(viewLifecycleOwner) {
            hideKeyboard()
        }
        // fragments
        viewModel.showBannerMessageLiveData.observe(viewLifecycleOwner) {
            showBannerMessage(
                message = it.message,
                state = it.state,
                messageID = it.messageID,
                title = it.title,
                gravity = it.gravity,
                anchorView = null,
            )
        }
        viewModel.showDialogMessageLiveData.observe(viewLifecycleOwner) {
            showDialogMessage(
                message = it.message,
                state = it.state,
                messageID = it.messageID,
                title = it.title,
                gravity = it.gravity,
                positiveButtonText = it.positiveButtonText,
                negativeButtonText = it.negativeButtonText,
                icon = it.icon,
                dialogData = it.dialogData,
            )
        }
        mainActivityViewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Ready -> {
                    hideEmptyState()
                    hideErrorState()
                    hideLoader()
                }

                is UiState.Empty -> {
                    showEmptyState(
                        title = it.title,
                        description = it.description,
                        actionButtonText = it.actionButtonText,
                    )
                    hideErrorState()
                    hideLoader()
                }

                is UiState.Loading -> {
                    hideEmptyState()
                    hideErrorState()
                    showLoader(
                        message = it.message,
                    )
                }

                is UiState.Error -> {
                    hideEmptyState()
                    hideLoader()
                    showErrorState(
                        title = it.title,
                        iconRes = it.iconRes,
                        description = it.description,
                        actionOneButtonText = it.actionOneButtonText,
                        actionTwoButtonText = it.actionTwoButtonText,
                    )
                }
            }
        }
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Ready -> {
                    hideEmptyState()
                    hideErrorState()
                    hideLoader()
                }

                is UiState.Empty -> {
                    showEmptyState(
                        title = it.title,
                        description = it.description,
                        actionButtonText = it.actionButtonText,
                    )
                    hideErrorState()
                    hideLoader()
                }

                is UiState.Loading -> {
                    hideEmptyState()
                    hideErrorState()
                    showLoader(
                        message = it.message,
                    )
                }

                is UiState.Error -> {
                    hideEmptyState()
                    hideLoader()
                    showErrorState(
                        title = it.title,
                        iconRes = it.iconRes,
                        description = it.description,
                        actionOneButtonText = it.actionOneButtonText,
                        actionTwoButtonText = it.actionTwoButtonText,
                    )
                }
            }
        }
        viewModel.showLoadingDialogLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is FullscreenLoadingState.Loading -> (activity as? MessageBannerHolder)?.showFullscreenLoader(
                    it.message
                )

                is FullscreenLoadingState.Ready -> (activity as? MessageBannerHolder)?.hideFullscreenLoader()
            }
        }
        // activity
        mainActivityViewModel.showBannerMessageLiveData.observe(viewLifecycleOwner) {
            showBannerMessage(
                message = it.message,
                state = it.state,
                messageID = it.messageID,
                title = it.title,
                gravity = it.gravity,
                anchorView = null,
            )
        }
        mainActivityViewModel.showDialogMessageLiveData.observe(viewLifecycleOwner) {
            showDialogMessage(
                message = it.message,
                state = it.state,
                messageID = it.messageID,
                title = it.title,
                gravity = it.gravity,
                positiveButtonText = it.positiveButtonText,
                negativeButtonText = it.negativeButtonText,
                icon = it.icon,
                dialogData = it.dialogData,
            )
        }

        mainActivityViewModel.showLoadingDialogLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is FullscreenLoadingState.Loading -> (activity as? MessageBannerHolder)?.showFullscreenLoader(
                    it.message
                )

                is FullscreenLoadingState.Ready -> (activity as? MessageBannerHolder)?.hideFullscreenLoader()
            }
        }
    }

    final override fun showBannerMessage(
        icon: Int?,
        message: StringSource,
        state: BannerMessage.State,
        messageID: String?,
        title: StringSource?,
        gravity: BannerMessage.Gravity,
        anchorView: View?,
        interval: BannerMessage.Interval,
        transparent: Boolean
    ) {
        try {
            logDebug("showBannerMessage", TAG)
            (activity as? MessageBannerHolder)?.showBannerMessage(
                icon = icon,
                message = message,
                state = state,
                messageID = messageID,
                title = title,
                gravity = gravity,
                anchorView = anchorView,
                interval = interval,
                transparent = transparent,
            )
        } catch (e: Exception) {
            logError("showBannerMessage Exception: ${e.message}", e, TAG)
        }
    }

    final override fun showDialogMessage(
        messageID: String?,
        title: StringSource?,
        message: StringSource,
        state: DialogMessage.State,
        gravity: DialogMessage.Gravity,
        positiveButtonText: StringSource?,
        negativeButtonText: StringSource?,
        icon: Int?,
        dialogData: String?
    ) {
        try {
            logDebug(
                "showMessage DialogMessage: ${message.getString(requireContext())}",
                TAG
            )
            (activity as? MessageBannerHolder)?.showDialogMessage(
                messageID = messageID,
                title = title,
                message = message,
                state = state,
                gravity = gravity,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                icon = icon,
                dialogData = dialogData,
            )
        } catch (e: Exception) {
            logError("showMessage DialogMessage Exception: ${e.message}", e, TAG)
        }
    }

    // hierarchy for view -> content, empty state, error state, loader

    fun showLoader(message: String? = null) {
        try {
            val loaderView = view?.findViewById<LoaderView>(R.id.loaderView)
            if (loaderView?.visibility != View.VISIBLE) {
                loaderView?.visibility = View.VISIBLE
            }
            if (!message.isNullOrEmpty() && loaderView?.visibility == View.VISIBLE) {
                logDebug("showLoader message: $message", TAG)
                loaderView.setMessage(message)
            }
        } catch (e: Exception) {
            logError("showLoader Exception: ${e.message}", e, TAG)
        }
    }

    fun hideLoader() {
        try {
//            view?.findViewById<ComplexGestureRefreshView>(R.id.refreshLayout)?.isRefreshing = false
            val loaderView = view?.findViewById<FrameLayout>(R.id.loaderView)
            if (loaderView?.visibility != View.GONE) {
                loaderView?.visibility = View.GONE
            }
        } catch (e: Exception) {
            logError("hideLoader Exception: ${e.message}", e, TAG)
        }
    }

    override fun showFullscreenLoader(message: StringSource?) {
        (activity as MessageBannerHolder).showFullscreenLoader(message = message)
    }

    override fun hideFullscreenLoader() {
        (activity as MessageBannerHolder).hideFullscreenLoader()
    }

    protected fun showEmptyState(
        title: StringSource? = null,
        description: StringSource? = null,
        actionButtonText: StringSource? = null,
    ) {
        logDebug("showEmptyState", TAG)
        try {
            val emptyStateView = view?.findViewById<FrameLayout>(R.id.emptyStateView)
            if (emptyStateView?.visibility != View.VISIBLE) {
                emptyStateView?.visibility = View.VISIBLE
            }
            if (title != null) {
                emptyStateView?.findViewById<AppCompatTextView>(R.id.tvTitle)?.let {
                    it.text = title.getString(requireContext())
                }
            }
            if (description != null) {
                emptyStateView?.findViewById<AppCompatTextView>(R.id.tvDesctiption)?.let {
                    it.text = description.getString(requireContext())
                }
            }
            if (actionButtonText != null) {
                emptyStateView?.findViewById<AppCompatButton>(R.id.btnEmptyStateViewReload)?.let {
                    it.text = actionButtonText.getString(requireContext())
                }
            }
        } catch (e: Exception) {
            logError("showEmptyState Exception: ${e.message}", e, TAG)
        }
    }

    protected fun hideEmptyState() {
        logDebug("hideEmptyState", TAG)
        try {
            val emptyStateView = view?.findViewById<FrameLayout>(R.id.emptyStateView)
            if (emptyStateView?.visibility != View.GONE) {
                emptyStateView?.visibility = View.GONE
            }
        } catch (e: Exception) {
            logError("hideEmptyState Exception: ${e.message}", e, TAG)
        }
    }

    fun showErrorState(
        iconRes: Int? = null,
        title: StringSource? = null,
        description: StringSource? = null,
        actionOneButtonText: StringSource? = null,
        actionTwoButtonText: StringSource? = null,
    ) {
        logDebug("showErrorState", TAG)
        try {
            logDebug("showErrorState description: $description", TAG)
            val errorView = view?.findViewById<View>(R.id.errorView)
            val btnErrorActionOne = errorView?.findViewById<AppCompatButton>(R.id.btnErrorActionOne)
            val btnErrorActionTwo = errorView?.findViewById<AppCompatButton>(R.id.btnErrorActionTwo)
            val tvDescription =
                errorView?.findViewById<AppCompatTextView>(R.id.tvErrorViewDescription)
            val tvTitle = errorView?.findViewById<AppCompatTextView>(R.id.tvErrorViewTitle)
            val ivIcon = errorView?.findViewById<AppCompatImageView>(R.id.ivErrorIcon)
            tvTitle?.isVisible = title != null
            tvDescription?.isVisible = description != null
            btnErrorActionOne?.isVisible = actionOneButtonText != null
            btnErrorActionTwo?.isVisible = actionTwoButtonText != null
            ivIcon?.isVisible = iconRes != null
            title?.let {
                tvTitle?.text = it.getString(requireContext())
            }
            description?.let {
                tvDescription?.text = it.getString(requireContext())
            }
            actionOneButtonText?.let {
                btnErrorActionOne?.text = it.getString(requireContext())
            }
            actionTwoButtonText?.let {
                btnErrorActionTwo?.text = it.getString(requireContext())
            }
            if (iconRes != null && iconRes != 0) {
                ivIcon?.setImageResource(iconRes)
            }
            if (errorView?.visibility != View.VISIBLE) {
                errorView?.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            logError("showErrorState Exception: ${e.message}", e, TAG)
        }
    }

    protected fun hideErrorState() {
        try {
            val errorView = view?.findViewById<FrameLayout>(R.id.errorView)
            if (errorView?.visibility != View.GONE) {
                errorView?.visibility = View.GONE
            }
        } catch (e: Exception) {
            logError("hideErrorState Exception: ${e.message}", e, TAG)
        }
    }

    open fun onBackPressed() {
        // Default on back pressed implementation for fragments.
        logDebug("onBackPressed", TAG)
        viewModel.onBackPressed()
    }

    private fun hideSettingsMenu() {
        popupWindow?.setOnDismissListener {
            popupWindow = null
        }
        popupWindow?.dismiss()
    }


}