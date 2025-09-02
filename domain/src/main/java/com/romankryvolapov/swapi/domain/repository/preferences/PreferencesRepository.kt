/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.repository.preferences

interface PreferencesRepository {

    fun getOpenOnTab(): Int

    fun saveOpenOnTab(openOnTab: Int)

    fun getLightNightMode(): Int

    fun saveLightNightMode(lightNightMode: Int)

}