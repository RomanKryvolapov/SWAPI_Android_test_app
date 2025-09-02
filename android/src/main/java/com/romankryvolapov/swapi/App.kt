/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi

import android.app.Application
import com.romankryvolapov.swapi.data.di.dataModules
import com.romankryvolapov.swapi.di.appModules
import com.romankryvolapov.swapi.domain.di.domainModules
import com.romankryvolapov.swapi.domain.externalFilesDirectory
import com.romankryvolapov.swapi.domain.internalFilesDirectory
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    companion object {
        private const val TAG = "AppTag"
    }

    override fun onCreate() {
        super.onCreate()
        logDebug("onCreate", TAG)
        externalFilesDirectory = getExternalFilesDir(null)!!
        internalFilesDirectory = filesDir
        setupKoin()
//        configureLeakCanary()
    }


    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            if (BuildConfig.DEBUG) {
                androidLogger(Level.ERROR)
            }
            allowOverride(override = true)
            workManagerFactory()
            modules(appModules, domainModules, dataModules)
        }
    }

//    private fun configureLeakCanary(isEnable: Boolean = false) {
//        LeakCanary.config = LeakCanary.config.copy(dumpHeap = isEnable)
//        LeakCanary.showLeakDisplayActivityLauncherIcon(isEnable)
//    }

}