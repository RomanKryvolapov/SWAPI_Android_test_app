/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.utils

import android.content.res.Resources
import com.romankryvolapov.swapi.domain.models.application.ApplicationLanguage
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.extensions.readOnly
import java.util.Locale

class LocalizationManager(
    private val currentContext: CurrentContext,
    private val dataStoreRepository: DataStoreRepository,
) {

    companion object {
        private const val TAG = "LocalizationManagerTag"
    }

    @Volatile
    private var inChange = false

    private val _readyLiveData = SingleLiveEventLiveData<Unit>()
    val readyLiveData = _readyLiveData.readOnly()

    var currentLocale: Locale? = null

    suspend fun applyLanguage(language: ApplicationLanguage? = null) {
        logDebug("applyLanguage language: $language inChange: $inChange", TAG)
        if (inChange) return
        inChange = true
        var applicationInfo = dataStoreRepository.readApplicationInfo()
        val systemLocale: Locale = Resources.getSystem().configuration.locales.get(0)
        val newLanguage = when {
            language != null && language != ApplicationLanguage.DEVICE -> language
            language != null && language == ApplicationLanguage.DEVICE -> {
                logDebug("applyLanguage DEVICE systemLocale: ${systemLocale.language}", TAG)
                ApplicationLanguage.entries.firstOrNull { systemLocale.language == it.type }
                    ?: ApplicationLanguage.ENGLISH
            }

            applicationInfo.applicationLanguage != ApplicationLanguage.DEVICE ->
                applicationInfo.applicationLanguage

            else -> {
                logDebug("applyLanguage else systemLocale: ${systemLocale.language}", TAG)
                ApplicationLanguage.entries.firstOrNull { systemLocale.language == it.type }
                    ?: ApplicationLanguage.ENGLISH
            }
        }
        currentLocale = Locale(newLanguage.type)
        val context = currentContext.get()
        val configuration = context.resources.configuration
        val displayMetrics = context.resources.displayMetrics
        Locale.setDefault(currentLocale!!)
        configuration.setLocale(currentLocale)
        context.createConfigurationContext(configuration)
        context.resources.updateConfiguration(configuration, displayMetrics)
        if (language != null) {
            logDebug("saveLanguage", TAG)
            dataStoreRepository.saveApplicationInfo(
                applicationInfo.copy(
                    applicationLanguage = language,
                )
            )
            _readyLiveData.callOnMainThread()
        }
        inChange = false
        logDebug("applyLanguage ready", TAG)
    }

}