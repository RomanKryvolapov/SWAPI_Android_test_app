package com.romankryvolapov.swapi.ui.fragments.persons.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.models.common.onFailure
import com.romankryvolapov.swapi.domain.models.common.onSuccess
import com.romankryvolapov.swapi.domain.models.person.PersonsModel
import com.romankryvolapov.swapi.domain.usecase.GetPersonsUseCase
import com.romankryvolapov.swapi.mappers.persons.PersonsUiMapper
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.models.persons.PersonUi
import kotlin.coroutines.cancellation.CancellationException

class PersonsDataSource(
    private val person: String?,
    private val personsUiMapper: PersonsUiMapper,
    private val getPersonsUseCase: GetPersonsUseCase,
    private val showErrorState: (message: StringSource) -> Unit,
) : PagingSource<Int, PersonUi>() {

    companion object {
        private const val TAG = "PersonsDataSourceTag"
    }

    override fun getRefreshKey(state: PagingState<Int, PersonUi>): Int? {
        logDebug("getRefreshKey", TAG)
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonUi> {
        val page = params.key ?: 1
        return try {
            val result = getPersonsUseCase.invoke(page = page, people = person)
            var loadResult: LoadResult<Int, PersonUi>? = null
            result.collect { dataResult ->
                dataResult.onSuccess { model: PersonsModel, _, _ ->
                    logDebug("load onSuccess", TAG)
                    val items = personsUiMapper.mapList(modelList = model.results)
                    loadResult = LoadResult.Page(
                        data = items,
                        prevKey = model.previous,
                        nextKey = model.next
                    )
                }.onFailure { _, title, message, _, _, _ ->
                    logError("load onFailure: $title $message", TAG)
                    showErrorState.invoke(StringSource(R.string.get_models_data_error, message))
                    loadResult = LoadResult.Error(Exception(message))
                }
            }
            loadResult ?: LoadResult.Error(Exception("Empty result"))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logError("exception: ${e.message}", TAG)
            showErrorState(StringSource(e.message))
            LoadResult.Error(e)
        }
    }


}