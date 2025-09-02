package com.romankryvolapov.swapi.ui.fragments.persons

import androidx.paging.LoadState
import com.romankryvolapov.swapi.databinding.FragmentPersonsBinding
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.extensions.enableChangeAnimations
import com.romankryvolapov.swapi.extensions.hideKeyboard
import com.romankryvolapov.swapi.extensions.onClickThrottle
import com.romankryvolapov.swapi.models.persons.PersonUi
import com.romankryvolapov.swapi.ui.base.fragments.BaseFragment
import com.romankryvolapov.swapi.ui.fragments.persons.list.PersonsAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PersonsFragment : BaseFragment<FragmentPersonsBinding, PersonsViewModel>(),
    PersonsAdapter.ClickListener {

    companion object {
        private const val TAG = "PersonsFragmentTag"
    }

    override fun getViewBinding() = FragmentPersonsBinding.inflate(layoutInflater)

    override val viewModel: PersonsViewModel by viewModel { parametersOf(mainActivityViewModel) }

    private val adapter: PersonsAdapter by inject()

    override fun setupControls() {
        logDebug("setupControls", TAG)
        setupAdapter()
        binding.errorView.actionOneClickListener = {
            hideKeyboard()
            viewModel.refreshList(
                person = binding.etPerson.text.toString()
            )
        }
        binding.emptyStateView.buttonClickListener = {
            hideKeyboard()
            viewModel.refreshList(
                person = binding.etPerson.text.toString()
            )
        }
        binding.btnUpdate.onClickThrottle {
            hideKeyboard()
            viewModel.refreshList(
                person = binding.etPerson.text.toString()
            )
        }
    }

    private fun setupAdapter() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.enableChangeAnimations(false)
        adapter.clickListener = this
        adapter.addLoadStateListener { loadState ->
            val isListEmpty = adapter.itemCount == 0
            val isRefreshing = loadState.refresh is LoadState.Loading
            val isAppending = loadState.append is LoadState.Loading
            val isPrepending = loadState.prepend is LoadState.Loading
            if (isRefreshing && isListEmpty) {
                showLoader()
            } else {
                hideLoader()
            }
            if (isListEmpty && !isRefreshing && !isAppending && !isPrepending) {
                showEmptyState()
            } else {
                hideEmptyState()
            }
            try {
                val currentBinding = binding ?: return@addLoadStateListener
                if (isRefreshing || isAppending || isPrepending) {
                    currentBinding.btnUpdate.text = "Refreshing..."
                    currentBinding.root.post {
                        hideKeyboard()
                    }
                } else {
                    currentBinding.btnUpdate.text = "Refresh"
                }
            } catch (e: Exception) {
                logError("addLoadStateListener exception: ${e.message}", e, TAG)
            }
        }
    }

    override fun subscribeToLiveData() {
        viewModel.adapterLiveData.observe(viewLifecycleOwner) { list ->
            logDebug("modelsPagingLiveData", TAG)
            adapter.submitData(lifecycle, list)
        }
        viewModel.searchLiveData.observe(viewLifecycleOwner) { text ->
            binding.etPerson.setText(text ?: "")
        }
    }

    override fun onPersonClicked(model: PersonUi) {
        viewModel.onPersonClicked(model)
    }

}