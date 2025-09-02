/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.extensions.readOnly
import javax.crypto.Cipher

class SupportBiometricManagerImpl(
    private val currentContext: CurrentContext
) : SupportBiometricManager {

    companion object {
        private const val TAG = "SupportBiometricManagerTag"
    }

    private val _onBiometricErrorLiveData = SingleLiveEventLiveData<Unit>()
    override val onBiometricErrorLiveData = _onBiometricErrorLiveData.readOnly()

    private val _onBiometricTooManyAttemptsLiveData = SingleLiveEventLiveData<Unit>()
    override val onBiometricTooManyAttemptsLiveData = _onBiometricTooManyAttemptsLiveData.readOnly()

    private val _onBiometricSuccessLiveData = SingleLiveEventLiveData<Cipher?>()
    override val onBiometricSuccessLiveData = _onBiometricSuccessLiveData.readOnly()

    private var biometricPromptInfo: BiometricPrompt.PromptInfo? = null
    private var biometricPrompt: BiometricPrompt? = null

    private val promptCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            logError("onAuthenticationError", TAG)
            when {
                errorCode == BiometricPrompt.ERROR_LOCKOUT ||
                        errorCode == BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                    _onBiometricErrorLiveData.call()
                }

                errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                        errorCode != BiometricPrompt.ERROR_CANCELED -> {
                    _onBiometricErrorLiveData.call()
                }
            }
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            logDebug("onAuthenticationSucceeded", TAG)
            val cipher = result.cryptoObject?.cipher
            _onBiometricSuccessLiveData.value = cipher
        }

        override fun onAuthenticationFailed() {
            logError("onAuthenticationFailed", TAG)
        }
    }

    override fun setupBiometricManager(fragment: Fragment) {
        logDebug("setupBiometricManager", TAG)
        if (biometricPromptInfo == null) {
            logDebug("setupBiometricManager setup biometricPromptInfo", TAG)
            biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(fragment.getString(R.string.auth_biometric_scanner_title))
                .setDescription(fragment.getString(R.string.auth_biometric_scanner_description))
                .setNegativeButtonText(fragment.getString(R.string.cancel))
                .build()
        }
        biometricPrompt?.cancelAuthentication()
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())
        biometricPrompt = BiometricPrompt(fragment, executor, promptCallback)
    }

    override fun authenticate(cipher: Cipher?) {
        if (biometricPrompt == null) {
            logError("authenticate biometricPrompt == null", TAG)
            return
        }
        if (biometricPromptInfo == null) {
            logError("authenticate biometricPromptInfo == null", TAG)
            return
        }
        if (cipher == null) {
            logError("authenticate cipher == null", TAG)
            biometricPrompt?.authenticate(biometricPromptInfo!!)
        } else {
            logDebug("authenticate cipher != null", TAG)
            biometricPrompt?.authenticate(
                biometricPromptInfo!!,
                BiometricPrompt.CryptoObject(cipher)
            )
        }
    }

    override fun cancelAuthentication() {
        logDebug("cancelAuthentication", TAG)
        biometricPrompt?.cancelAuthentication()
    }

    override fun hasBiometrics(): Boolean {
        return BiometricManager.from(currentContext.get()).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

}