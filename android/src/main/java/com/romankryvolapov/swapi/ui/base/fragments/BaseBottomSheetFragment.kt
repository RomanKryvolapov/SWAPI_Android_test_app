/**
 * This base class already has a State Holder Layout inside, but it is on
 * the full size of the modal, so if the embedded state holder is needed,
 * the other layout should be used and the base methods like,
 * [showLoader], [showReady], [showNetworkError] etc. should be overridden.
 * Please follow code style when editing project
 * Please follow principles of clean architecture
 * Created & Copyright 2025 by Roman Kryvolapov
 */
package com.romankryvolapov.swapi.ui.base.fragments

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.core.animation.addListener
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.databinding.BottomSheetBaseLayoutBinding
import com.romankryvolapov.swapi.extensions.pxDimen
import com.romankryvolapov.swapi.models.common.BannerMessage
import com.romankryvolapov.swapi.models.common.DialogMessage
import com.romankryvolapov.swapi.models.common.MessageBannerHolder
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.base.viewmodels.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseBottomSheetFragment<VB : ViewBinding, VM : BaseViewModel> :
    BottomSheetDialogFragment(),
    MessageBannerHolder {

    companion object {
        private const val TAG = "BaseBottomSheetFragmentTag"
        private const val ANIMATION_DURATION = 250L
    }

    protected open val isDraggableByUser: Boolean = true
    protected open val isCancelableFromOutside: Boolean = true

    protected val mainActivityViewModel: MainActivityViewModel by activityViewModel()

    protected open val maxHeight = Resources.getSystem().displayMetrics.heightPixels * 80 / 100

    private var isPostponedBottomSheet: Boolean = false

    abstract val viewModel: VM

    private var viewBinding: VB? = null

    private var rootViewBinding: BottomSheetBaseLayoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = viewBinding!!

    abstract fun getViewBinding(): VB

    private var behavior: BottomSheetBehavior<FrameLayout>? = null
    private var sheetFrame: FrameLayout? = null
    private var mode = BottomDialogMode.DEFAULT
    private var isAnimated = false
    private var currentAnimation: ValueAnimator? = null
    private var sheetHeight = 0
    private val layoutChangeListener = ViewTreeObserver.OnPreDrawListener {
        onLayoutMeasuresChanged()
    }

    final override fun onResume() {
        onResumed()
        super.onResume()
    }

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        logDebug("onCreateDialog", TAG)
        return CustomDialog(requireContext(), R.style.AppBottomSheetDialogTheme).apply {
            setCanceledOnTouchOutside(isCancelableFromOutside)
            setOnShowListener {
                // Find a Frame layout with Bottom sheet behavior in the dialog
                (it as? BottomSheetDialog)
                    ?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?.let { sheet ->
                        setupBottomSheet(sheet)
                    }

                if (mode != BottomDialogMode.DEFAULT) {
                    setMode(mode)
                }

                onBottomSheetDialogShown()
            }
        }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logDebug("onCreateView", TAG)
        rootViewBinding = BottomSheetBaseLayoutBinding.inflate(layoutInflater)
        viewBinding = getViewBinding()
        rootViewBinding?.flBottomSheetContainer?.addView(viewBinding!!.root)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        if (!showsDialog) {
            setMode(mode)
        }
        return rootViewBinding!!.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logDebug("onViewCreated", TAG)
        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        setupNavControllers()
        subscribeToBaseLiveData()
        onCreated()
        setupView()
        setupControls()
        subscribeToLiveData()
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated()
//        viewModel.trackScreenOpenningAnalyticEvent(
//            this::class.java.simpleName, tryToFindScreenTitle()
//        )
        // Setup Watchers
        val layoutParams = binding.root.layoutParams
        layoutParams.height = maxHeight
        binding.root.layoutParams = layoutParams
    }

    final override fun onStop() {
        super.onStop()
        onStopped()
    }

    protected open fun setupNavControllers() {
        setupActivityNavController()
    }

    private fun setupActivityNavController() {
        // Search for the activity controller
        val host = requireActivity().supportFragmentManager
            .findFragmentById(R.id.navigationContainer) as NavHostFragment
        viewModel.bindActivityNavController(host.navController)
    }

    protected open fun onCreated() {
        // Override when needed
    }

    protected open fun onResumed() {
        // Override when needed
    }

    protected open fun onStopped() {
        // Override when needed
    }

    protected open fun onDismissed() {
        // Override when needed
    }

    protected open fun onDismissed(dialog: DialogInterface) {
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

    protected open fun onBottomSheetDialogShown() {
        // Override when needed
    }

    private fun subscribeToBaseLiveData() {
        viewModel.closeActivityLiveData.observe(viewLifecycleOwner) {
            requireActivity().finish()
        }
        viewModel.backPressedFailedLiveData.observe(viewLifecycleOwner) {
            dismiss()
        }
        viewModel.showBannerMessageLiveData.observe(viewLifecycleOwner) { message ->
            showBannerMessage(
                message = message.message,
                state = message.state,
                messageID = message.messageID,
                title = message.title,
                gravity = message.gravity,
            )
        }
    }

    open fun onBackPressed() {
        logDebug("onBackPressed", TAG)
        dismiss()
    }

    open fun onKeyBoardOpened(keyboardHeight: Int) {
        logDebug("onKeyBoardOpened", TAG)
        rootViewBinding?.apply {
            rootLayout.post {
                rootLayout.setPadding(0, 0, 0, keyboardHeight)
            }
        }
    }

    open fun onKeyboardClosed() {
        logDebug("onKeyboardClosed", TAG)
        rootViewBinding?.apply {
            rootLayout.post {
                rootLayout.setPadding(0, 0, 0, 0)
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
            logDebug("showMessage DialogMessage: ${message.getString(requireContext())}", TAG)
            (activity as? MessageBannerHolder)?.showDialogMessage(
                messageID = messageID,
                title = title,
                message = message,
                state = state,
                gravity = gravity,
            )
        } catch (e: Exception) {
            logError("showMessage DialogMessage Exception: ${e.message}", e, TAG)
        }
    }

    fun logoutFromApplication() {
        // TODO
    }

    override fun showFullscreenLoader(message: StringSource?) {
        try {
            (requireActivity() as MessageBannerHolder)
                .showFullscreenLoader(message = message)
        } catch (e: Exception) {
            logError("showBeta Exception: ${e.message}", e, TAG)
        }
    }

    override fun hideFullscreenLoader() {
        try {
            (requireActivity() as MessageBannerHolder)
                .hideFullscreenLoader()
        } catch (e: Exception) {
            logError("showBeta Exception: ${e.message}", e, TAG)
        }
    }

    /**
     * Setup bottom sheet behaviour and save a link to the sheet layout
     *
     * @param sheet - bottom sheet frame layout
     */
    private fun setupBottomSheet(sheet: FrameLayout) {
        sheetFrame = sheet
        behavior = BottomSheetBehavior.from(sheetFrame!!)
        behavior?.apply {
            peekHeight = 0
            isHideable = true
            skipCollapsed = true
            isDraggable = isDraggableByUser
            if (!isPostponedBottomSheet) {
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    fun postponeEnterBottomSheet() {
        this.isPostponedBottomSheet = true
    }

    fun startPostponedEnterBottomSheet() {
        if (isPostponedBottomSheet) {
            isPostponedBottomSheet = false
            sheetFrame?.post { behavior?.state = BottomSheetBehavior.STATE_EXPANDED }
        }
    }

    /**
     * Change current dialog mode. Its changed the height calculation
     * mechanic.
     *
     * @param mode - a new mode of the dialog
     * @see BottomDialogMode
     */
    fun setMode(mode: BottomDialogMode) {
        this.mode = mode
        when (mode) {
            BottomDialogMode.DEFAULT -> {
                subscribeToLayoutChanges()
            }

            BottomDialogMode.FULL_SCREEN -> {
                releaseLayoutChangesListener()
                sheetFrame?.let {
                    it.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
                    it.requestLayout()
                    it.viewTreeObserver.addOnPreDrawListener(layoutChangeListener)
                }
            }
        }
    }

    private fun subscribeToLayoutChanges() {
        sheetFrame?.let {
            it.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
            it.requestLayout()
            it.viewTreeObserver.addOnPreDrawListener(layoutChangeListener)
        }
    }

    private fun releaseLayoutChangesListener() {
        sheetFrame?.viewTreeObserver?.removeOnPreDrawListener(layoutChangeListener)
    }

    /**
     * Listen the layout changes in pre draw method and handle it
     *
     * @return true when we have to cancel a system draw event and
     *         recalculate own height, false otherwise
     */
    private fun onLayoutMeasuresChanged(): Boolean {
        val newHeight = sheetFrame?.measuredHeight ?: 0
        // Check that height changed
        if (sheetHeight != newHeight && newHeight != 0) {
            if (isAnimated) {
                currentAnimation?.cancel()
            }

            animateContainerHeight(sheetHeight, newHeight)
            return false
        }
        return true
    }

    /**
     * Resize bottom dialog container to appropriate view height.
     * Resizing include a simple animation transition
     *
     * @param oldHeight - the old height of the container
     * @param height - a new view height, that of course also a new container height
     */
    private fun animateContainerHeight(oldHeight: Int, height: Int) {
        sheetFrame?.let { frame ->
            currentAnimation = ValueAnimator.ofInt(oldHeight, height)?.apply {
                addUpdateListener { valueAnimator ->
                    sheetHeight = valueAnimator.animatedValue as Int
                    val layoutParams = frame.layoutParams
                    layoutParams.height = sheetHeight
                    frame.layoutParams = layoutParams
                }
                addListener(
                    { onEndAnimationListener() },
                    { onStartAnimationListener() },
                    { onCancelAnimationListener() }
                )
                duration = ANIMATION_DURATION
                start()
            }
        }
    }

    private fun onStartAnimationListener() {
        releaseLayoutChangesListener()
        isAnimated = true
    }

    private fun onEndAnimationListener() {
        isAnimated = false
        if (mode == BottomDialogMode.DEFAULT) {
            subscribeToLayoutChanges()
        }
    }

    private fun onCancelAnimationListener() {
        isAnimated = false
        if (mode == BottomDialogMode.DEFAULT) {
            subscribeToLayoutChanges()
        }
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.pxDimen(resourceId)
        } else 0
    }

    final override fun onDismiss(dialog: DialogInterface) {
        // Prevent memory leak
        (dialog as? BottomSheetDialog)?.setOnShowListener(null)
        onDismissed()
        onDismissed(dialog)
        super.onDismiss(dialog)
    }

    final override fun onDestroyView() {
        viewBinding = null
        viewModel.unbindActivityNavController()
        dialog?.setOnShowListener(null)
        releaseLayoutChangesListener()
        super.onDestroyView()
    }

    // Handles back press
    inner class CustomDialog(context: Context, theme: Int) : BottomSheetDialog(context, theme) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setupOnBackPressedCallback()
            if (isPostponedBottomSheet) {
                // to prevent auto displaying bottom sheet content when created
                // in postponed mode only
                behavior.peekHeight = 0
            }
        }

        private fun setupOnBackPressedCallback() {
            onBackPressedDispatcher.addCallback(this) {
                this@BaseBottomSheetFragment.onBackPressed()
            }
        }

        override fun onStart() {
            super.onStart()
            if (!isPostponedBottomSheet) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    /**
     * This is the modes for a dialog.
     * FULL_SCREEN - set to the dialog a MATCH_PARENT size and fix it.
     * DEFAULT - set to the dialog a WRAP_CONTENT size and allow to change it
     *           according to content with the animated transitions.
     */
    enum class BottomDialogMode {
        FULL_SCREEN,
        DEFAULT
    }

}