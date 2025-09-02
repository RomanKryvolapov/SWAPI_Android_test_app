/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.repository.preferences

import androidx.security.crypto.EncryptedSharedPreferences
import com.romankryvolapov.swapi.domain.repository.preferences.PreferencesRepository

class PreferencesRepositoryImpl(
    private val preferences: EncryptedSharedPreferences
) : PreferencesRepository {

    companion object {
        private const val TAG = "PreferencesRepositoryTag"
        private const val OPEN_ON_TAB_KEY = "OPEN_ON_TAB_KEY"
        private const val LIGHT_NIGHT_MODE_KEY = "LIGHT_NIGHT_MODE_KEY"
    }

    override fun getOpenOnTab(): Int {
        return preferences.getInt(OPEN_ON_TAB_KEY, 1)
    }

    override fun saveOpenOnTab(openOnTab: Int) {
        preferences.edit().putInt(OPEN_ON_TAB_KEY, openOnTab).apply()
    }

    override fun getLightNightMode(): Int {
        return preferences.getInt(LIGHT_NIGHT_MODE_KEY, -1)
    }

    override fun saveLightNightMode(lightNightMode: Int) {
        preferences.edit().putInt(LIGHT_NIGHT_MODE_KEY, lightNightMode).apply()
    }


}