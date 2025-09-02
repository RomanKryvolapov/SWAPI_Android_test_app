/**
 * single<Class>(named("name")){Class()} -> for creating a specific instance in module
 * single<Class1>{Class1(get<Class2>(named("name")))} -> for creating a specific instance in module
 * val nameOfVariable: Class by inject(named("name")) -> for creating a specific instance in class
 * get<Class>{parametersOf("param")} -> parameter passing in module
 * single<Class>{(param: String)->Class(param)} -> parameter passing in module
 * val name: Class by inject(parameters={parametersOf("param")}) -> parameter passing in class
 * Created & Copyright 2025 by Roman Kryvolapov
 */
package com.romankryvolapov.swapi.di

import android.app.ActivityManager
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.os.Handler
import android.os.Looper
import android.speech.SpeechRecognizer
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.romankryvolapov.swapi.data.utils.CoroutineContextProvider
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer
import com.romankryvolapov.swapi.utils.InactivityTimerImpl
import com.romankryvolapov.swapi.utils.LocalizationManager
import com.romankryvolapov.swapi.utils.LoginTimer
import com.romankryvolapov.swapi.utils.LoginTimerImpl
import com.romankryvolapov.swapi.utils.NotificationHelper
import com.romankryvolapov.swapi.utils.RecyclerViewAdapterDataObserver
import com.romankryvolapov.swapi.utils.SocialNetworksHelper
import com.romankryvolapov.swapi.utils.SupportBiometricManager
import com.romankryvolapov.swapi.utils.SupportBiometricManagerImpl
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single<ClipboardManager> {
        ContextCompat.getSystemService(
            androidContext(),
            ClipboardManager::class.java
        ) as ClipboardManager
    }

    single<DownloadManager> {
        ContextCompat.getSystemService(
            androidContext(),
            DownloadManager::class.java
        ) as DownloadManager
    }

    single<InputMethodManager> {
        ContextCompat.getSystemService(
            androidContext(),
            InputMethodManager::class.java
        ) as InputMethodManager
    }

    single<NotificationManager> {
        ContextCompat.getSystemService(
            androidContext(),
            NotificationManager::class.java
        ) as NotificationManager
    }

    single<ActivityManager> {
        ContextCompat.getSystemService(
            androidContext(),
            ActivityManager::class.java
        ) as ActivityManager
    }

    singleOf(::CoroutineContextProvider)

    singleOf(::NotificationHelper)

    single<Handler> {
        Handler(Looper.getMainLooper())
    }

    singleOf(::SocialNetworksHelper)

    singleOf(::SupportBiometricManagerImpl) bind SupportBiometricManager::class

    singleOf(::RecyclerViewAdapterDataObserver)

    single<CurrentContext> {
        CurrentContext(
            context = androidContext()
        )
    }

    singleOf(::LocalizationManager)

    singleOf(::LoginTimerImpl) bind LoginTimer::class

    singleOf(::InactivityTimerImpl) bind InactivityTimer::class

    single<LanguageIdentifier> {
        LanguageIdentification.getClient()
    }

    single<SpeechRecognizer> {
        SpeechRecognizer.createSpeechRecognizer(androidContext())
    }

    single<AuthorizationService>{
        AuthorizationService(androidContext())
    }

}