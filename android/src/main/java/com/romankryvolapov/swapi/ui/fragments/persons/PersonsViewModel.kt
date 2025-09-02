package com.romankryvolapov.swapi.ui.fragments.persons

import android.R.id.message
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import com.romankryvolapov.swapi.domain.usecase.GetPersonsUseCase
import com.romankryvolapov.swapi.extensions.postValueOnMainThread
import com.romankryvolapov.swapi.extensions.readOnly
import com.romankryvolapov.swapi.extensions.setValueOnMainThread
import com.romankryvolapov.swapi.mappers.persons.PersonsUiMapper
import com.romankryvolapov.swapi.models.common.BannerMessage
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.models.persons.PersonUi
import com.romankryvolapov.swapi.ui.activity.main.MainActivityViewModel
import com.romankryvolapov.swapi.ui.base.fragments.BaseFragment.Companion.DIALOG_EXIT
import com.romankryvolapov.swapi.ui.base.viewmodels.fragment.BaseFragmentViewModel
import com.romankryvolapov.swapi.ui.fragments.persons.list.PersonsDataSource
import com.romankryvolapov.swapi.utils.CurrentContext
import com.romankryvolapov.swapi.utils.InactivityTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PersonsViewModel(
    private val personsUiMapper: PersonsUiMapper,
    private val getPersonsUseCase: GetPersonsUseCase,
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
        private const val TAG = "PersonsViewModelTag"
    }

    override val enterKeyAction = MainActivityViewModel.Companion.EnterKeyAction.ACTION

    private val _adapterLiveData = MutableLiveData<PagingData<PersonUi>>()
    val adapterLiveData = _adapterLiveData.readOnly()

    private val _searchLiveData = MutableLiveData<String>()
    val searchLiveData = _searchLiveData.readOnly()

    private var updatePersonsJob: Job? = null

    override fun onFirstAttach() {
        logDebug("onFirstAttach", TAG)
        viewModelScope.launch(Dispatchers.IO) {
            refreshList()
        }
    }

    fun refreshList(person: String? = null) {
        logDebug("refreshList person: $person", TAG)
        updatePersonsJob?.cancel()
        updatePersonsJob = viewModelScope.launch(Dispatchers.Main) {
            _searchLiveData.setValueOnMainThread(person)
            createPager(
                person = person,
            )
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _adapterLiveData.postValueOnMainThread(pagingData)
                }
        }
    }

    private fun createPager(person: String?): Pager<Int, PersonUi> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 10,
                maxSize = 200
            ),
            pagingSourceFactory = {
                PersonsDataSource(
                    person = person,
                    personsUiMapper = personsUiMapper,
                    getPersonsUseCase = getPersonsUseCase,
                    showErrorState = { message ->
                        hideErrorState()
                        showErrorState(
                            description = message,
                            actionOneButtonText = StringSource(R.string.retry),
                            actionTwoButtonText = StringSource(R.string.close),
                        )
                    }
                )
            }
        )
    }

    fun onPersonClicked(model: PersonUi) {
        logDebug("onRepositoryClicked", TAG)
        if (model.homeworldID == null) {
            showBannerMessage(
                state = BannerMessage.State.ERROR,
                message = StringSource("Home planet not found"),
                gravity = BannerMessage.Gravity.CENTER,
            )
            return
        }
        navigateInFlow(
            PersonsFragmentDirections.toPlanetsFragment(
                planetID = model.homeworldID
            )
        )
    }


    override fun onBackPressed() {
        logDebug("onBackPressed", TAG)
        showDialogMessage(
            messageID = DIALOG_EXIT,
            message = StringSource(R.string.do_you_want_to_close_application),
            title = StringSource(R.string.information),
            positiveButtonText = StringSource(R.string.yes),
            negativeButtonText = StringSource(R.string.no),
        )
    }

    override fun onEnterPressed() {
        hideKeyboard()
    }

}
