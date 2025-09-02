/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.di

import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.fragments.main.MainFlowViewModel
import com.romankryvolapov.swapi.ui.fragments.persons.PersonsViewModel
import com.romankryvolapov.swapi.ui.fragments.planet.PlanetViewModel
import com.romankryvolapov.swapi.ui.fragments.start.StartViewModel
import com.romankryvolapov.swapi.ui.fragments.start.flow.StartFlowViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {

    // activity
    viewModelOf(::MainActivityViewModel)
    // start
    viewModelOf(::StartFlowViewModel)

    viewModelOf(::StartViewModel)

    // Tabs
    viewModelOf(::MainFlowViewModel)

    viewModel { (mainActivityViewModel: MainActivityViewModel) ->
        PersonsViewModel(
            currentContext = get(),
            inactivityTimer = get(),
            personsUiMapper = get(),
            dataStoreRepository = get(),
            getPersonsUseCase = get(),
            mainActivityViewModel = mainActivityViewModel,
        )
    }

    viewModel { (mainActivityViewModel: MainActivityViewModel) ->
        PlanetViewModel(
            planetUiMapper = get(),
            currentContext = get(),
            inactivityTimer = get(),
            dataStoreRepository = get(),
            getPlanetDetailsUseCase = get(),
            mainActivityViewModel = mainActivityViewModel,
        )
    }

}