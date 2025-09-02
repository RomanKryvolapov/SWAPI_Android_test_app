package com.romankryvolapov.swapi.ui.fragments.planet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.onFailure
import com.romankryvolapov.swapi.domain.models.common.onSuccess
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.domain.usecase.GetPlanetDetailsUseCase
import com.romankryvolapov.swapi.extensions.launchInScope
import com.romankryvolapov.swapi.extensions.postValueOnMainThread
import com.romankryvolapov.swapi.extensions.readOnly
import com.romankryvolapov.swapi.mappers.planets.PlanetUiMapper
import com.romankryvolapov.swapi.models.common.BannerMessage
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.models.planets.PlanetAdapterMarker
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.base.fragments.BaseFragment.Companion.DIALOG_EXIT
import com.romankryvolapov.swapi.ui.base.viewmodels.fragment.BaseFragmentViewModel
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer
import kotlinx.coroutines.flow.onEach

class PlanetViewModel(
    private val planetUiMapper: PlanetUiMapper,
    private val getPlanetDetailsUseCase: GetPlanetDetailsUseCase,
    currentContext: CurrentContext,
    inactivityTimer: InactivityTimer,
    dataStoreRepository: DataStoreRepository,
    mainActivityViewModel: MainActivityViewModel,
) : BaseFragmentViewModel(
    currentContext = currentContext,
    inactivityTimer = inactivityTimer,
    dataStoreRepository = dataStoreRepository,
    mainActivityViewModel = mainActivityViewModel,
) {

    companion object {
        private const val TAG = "PlanetViewModelTag"
    }

    override val enterKeyAction = MainActivityViewModel.Companion.EnterKeyAction.ACTION

    private val _adapterLiveData = MutableLiveData<List<PlanetAdapterMarker>>()
    val adapterLiveData = _adapterLiveData.readOnly()

    fun getPlanetDetails(id: Int) {
        logDebug("getPlanetDetails id: $id", TAG)
        getPlanetDetailsUseCase.invoke(
            id = id,
        ).onEach { result ->
            result.onSuccess { model, _, _ ->
                logDebug("getPlanetDetails onSuccess model: $model", TAG)
                _adapterLiveData.postValueOnMainThread(
                    planetUiMapper.map(model)
                )
            }.onFailure { _, title, message, _, _, _ ->
                logDebug("getPlanetDetails onFailure message: $message", TAG)
                showBannerMessage(
                    state = BannerMessage.State.ERROR,
                    message = StringSource("Model delete error")
                )
            }
        }.launchInScope(viewModelScope)

    }

    override fun onBackPressed() {
        logDebug("onBackPressed", TAG)
        popBackStack()
    }

    override fun onEnterPressed() {
        hideKeyboard()
    }

}