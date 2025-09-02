package com.romankryvolapov.swapi.ui.fragments.planet.list

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.romankryvolapov.swapi.models.planets.PlanetAdapterMarker
import com.romankryvolapov.swapi.utils.DefaultDiffUtilCallback

class PlanetAdapter (
    private val planetTitleDelegate: PlanetTitleDelegate,
    private val planetFieldDelegate: PlanetFieldDelegate,
) : AsyncListDifferDelegationAdapter<PlanetAdapterMarker>(DefaultDiffUtilCallback()) {

    init {
        delegatesManager.apply {
            addDelegate(planetTitleDelegate)
            addDelegate(planetFieldDelegate)
        }
    }

}