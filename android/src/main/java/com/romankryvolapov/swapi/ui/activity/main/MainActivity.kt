/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.activity.main

import android.Manifest
import android.app.Dialog
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import com.romankryvolapov.swapi.BuildConfig
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.databinding.ActivityMainBinding
import com.romankryvolapov.swapi.databinding.LayoutAlertDialogBinding
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.repository.preferences.PreferencesRepository
import com.romankryvolapov.swapi.extensions.hideKeyboard
import com.romankryvolapov.swapi.extensions.onClickThrottle
import com.romankryvolapov.swapi.extensions.setTextSource
import com.romankryvolapov.swapi.models.common.AlertDialogResult
import com.romankryvolapov.swapi.models.common.BannerMessage
import com.romankryvolapov.swapi.models.common.CardScanBottomSheetContent
import com.romankryvolapov.swapi.models.common.CardScanBottomSheetHolder
import com.romankryvolapov.swapi.models.common.DialogMessage
import com.romankryvolapov.swapi.models.common.KeyboardPressKeyListener
import com.romankryvolapov.swapi.models.common.MessageBannerHolder
import com.romankryvolapov.swapi.models.common.StartDestination
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.ui.view.FullscreenLoaderView
import com.romankryvolapov.swapi.utils.AlertDialogResultListener
import com.romankryvolapov.swapi.utils.AppUncaughtExceptionHandler
import com.romankryvolapov.swapi.utils.BannerMessageWindowManager
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),
    MessageBannerHolder,
    CardScanBottomSheetHolder,
    ComponentCallbacks2 {

    companion object {
        private const val TAG = "MainActivityTag"
    }

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    private fun getStartDestination(): StartDestination {
        return viewModel.getStartDestination(intent)
    }

    private lateinit var bannerMessageWindowManager: BannerMessageWindowManager

    var keyboardPressKeyListener: KeyboardPressKeyListener? = null
    var alertDialogResultListener: AlertDialogResultListener? = null
    private var fullscreenLoaderView: FullscreenLoaderView? = null
    private var messageDialog: Dialog? = null


    private val appContext: Context by inject()
    private val currentContext: CurrentContext by inject()
    private val inactivityTimer: InactivityTimer by inject()
    private val authorizationService: AuthorizationService by inject()
    private val preferencesRepository: PreferencesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        logDebug("onCreate", TAG)
        val lightNightMode = runBlocking {
            preferencesRepository.getLightNightMode()
        }
        AppCompatDelegate.setDefaultNightMode(lightNightMode)
        installSplashScreen()
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler(AppUncaughtExceptionHandler())
        currentContext.attachBaseContext(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        bannerMessageWindowManager = BannerMessageWindowManager(this)
        setupNavController()
        subscribeToLiveData()
        viewModel.onViewCreated()

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            !isDarkTheme()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT <= 29 &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            viewModel.dispatchTouchEvent()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        keyboardPressKeyListener?.onKeyboardPressed(event)
        if (event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            when (viewModel.enterKeyAction) {
                MainActivityViewModel.Companion.EnterKeyAction.ENTER -> {
                    logDebug("dispatchKeyEvent ENTER", TAG)
                    return super.dispatchKeyEvent(event)
                }

                MainActivityViewModel.Companion.EnterKeyAction.ACTION -> {
                    logDebug("dispatchKeyEvent ACTION", TAG)
                    keyboardPressKeyListener?.onEnterPressed()
                    return true
                }

                MainActivityViewModel.Companion.EnterKeyAction.NOTHING -> {
                    logDebug("dispatchKeyEvent NOTHING", TAG)
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun showCardBottomSheet(content: CardScanBottomSheetContent) {
//        ScanCardBottomSheetFragment.newInstance(content = content).also { bottomSheet ->
//            bottomSheet.show(supportFragmentManager, "ScanCardBottomSheetFragmentTag")
//        }
    }

    private fun setupNavController() {
        val host =
            supportFragmentManager.findFragmentById(R.id.navigationContainer) as NavHostFragment
        try {
            // Try to get the current graph, if it is there, nav controller is valid.
            // When there is no graph, it throws IllegalStateException,
            // then we need to create a graph ourselves
            host.navController.graph
        } catch (e: Exception) {
            val graphInflater = host.navController.navInflater
            val graph = graphInflater.inflate(R.navigation.nav_activity)
            val startDestination = getStartDestination()
            graph.setStartDestination(startDestination.destination)
            host.navController.setGraph(graph, startDestination.arguments)
        }
        viewModel.bindActivityNavController(host.navController)
    }

    private fun subscribeToLiveData() {
        viewModel.closeActivityLiveData.observe(this) {
            finish()
        }
        viewModel.showBannerMessageLiveData.observe(this) {
            showBannerMessage(
                message = it.message,
                state = it.state,
                messageId = it.messageID,
                title = it.title,
                gravity = it.gravity,
                anchorView = null,
            )
        }
        viewModel.showDialogMessageLiveData.observe(this) {
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
        inactivityTimer.lockStatusLiveData.observe(this) {
            if (it) {
                logDebug("lockStatusLiveData onLoginTimerExpired", TAG)
                messageDialog?.dismiss()
//                viewModel.toLoginFragment()
            }
        }
    }

    override fun showBannerMessage(
        icon: Int?,
        message: StringSource,
        state: BannerMessage.State,
        messageId: String?,
        title: StringSource?,
        gravity: BannerMessage.Gravity,
        anchorView: View?,
        interval: BannerMessage.Interval,
        transparent: Boolean
    ) {
        logDebug("showMessage message: ${message.getString(this)}", TAG)
        try {
            bannerMessageWindowManager.showMessage(
                icon = icon,
                messageId = messageId,
                transparent = transparent,
                title = title,
                gravity = gravity,
                interval = interval,
                state = state,
                message = message,
                anchorView = anchorView ?: binding.rootLayout,
            )
        } catch (e: Exception) {
            logError("showBannerMessage Exception: ${e.message}", e, TAG)
        }
    }

    override fun showDialogMessage(
        messageID: String?,
        title: StringSource?,
        message: StringSource,
        state: DialogMessage.State,
        gravity: DialogMessage.Gravity,
        positiveButtonText: StringSource?,
        negativeButtonText: StringSource?,
        @DrawableRes icon: Int?,
        dialogData: String?,
    ) {
        logDebug("showDialogMessage", TAG)
        val binding = LayoutAlertDialogBinding.inflate(LayoutInflater.from(this)).apply {
            if (title != null) {
                tvTitle.setTextSource(title)
            }
            tvDescription.setTextSource(message)
            if (positiveButtonText != null) {
                btnPositive.setTextSource(positiveButtonText)
            }
            if (negativeButtonText != null) {
                btnNegative.setTextSource(negativeButtonText)
            }
            btnPositive.onClickThrottle {
                logDebug("alertDialog result positive", TAG)
                messageDialog?.cancel()
                val result = AlertDialogResult(
                    messageId = messageID,
                    isPositive = true,
                    dialogData = dialogData,
                )
                onAlertDialogResult(result)
                alertDialogResultListener?.onAlertDialogResultReady(result)
            }
            btnNegative.onClickThrottle {
                logDebug("alertDialog result negative", TAG)
                messageDialog?.cancel()
                val result = AlertDialogResult(
                    messageId = messageID,
                    isPositive = false,
                    dialogData = dialogData,
                )
                onAlertDialogResult(result)
                alertDialogResultListener?.onAlertDialogResultReady(result)
            }
        }
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setView(binding.root)
            .setOnCancelListener { _: DialogInterface? ->
                logDebug("alertDialog result negative", TAG)
                val result = AlertDialogResult(
                    messageId = messageID,
                    isPositive = false,
                    dialogData = dialogData,
                )
                onAlertDialogResult(result)
                alertDialogResultListener?.onAlertDialogResultReady(result)
            }
            .create()
        messageDialog = builder.apply {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
        messageDialog?.show()
    }

    override fun showFullscreenLoader(message: StringSource?) {
        fullscreenLoaderView?.let { loader ->
            message?.let {
                loader.setMessage(message = it)
            }
        } ?: run {
            fullscreenLoaderView = FullscreenLoaderView(this).also { loader ->
                message?.let {
                    loader.setMessage(message = it)
                }
                loader.show()
            }
        }
    }

    override fun hideFullscreenLoader() {
        fullscreenLoaderView = try {
            fullscreenLoaderView?.dismiss()
            null
        } catch (exception: Exception) {
            logError("Hiding fullscreen loader returned an exception: ${exception.message}", TAG)
            null
        }
    }

    override fun onResume() {
        logDebug("onResume", TAG)
        super.onResume()
        viewModel.onResumed()
    }

    override fun onPause() {
        logDebug("onPause", TAG)
        hideKeyboard()
        super.onPause()
        viewModel.onPaused()
    }

    override fun onStop() {
        logDebug("onStop", TAG)
        super.onStop()
        viewModel.onStopped()
    }

    override fun onDestroy() {
        logDebug("onDestroy", TAG)
        super.onDestroy()
        viewModel.onDestroyed()
        bannerMessageWindowManager.hideWindow()
        // Reset this activity context
        if (currentContext.get() == this) {
            currentContext.attachBaseContext(appContext)
        }
        authorizationService.dispose()
    }

    private fun onAlertDialogResult(result: AlertDialogResult) {

    }

    fun isDarkTheme(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

}