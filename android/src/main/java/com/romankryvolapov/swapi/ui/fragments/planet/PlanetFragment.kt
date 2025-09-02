package com.romankryvolapov.swapi.ui.fragments.planet

import androidx.navigation.fragment.navArgs
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.databinding.FragmentPlanetBinding
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.extensions.enableChangeAnimations
import com.romankryvolapov.swapi.extensions.hideKeyboard
import com.romankryvolapov.swapi.extensions.onClickThrottle
import com.romankryvolapov.swapi.models.common.BannerMessage
import com.romankryvolapov.swapi.models.common.StringSource
import com.romankryvolapov.swapi.ui.base.fragments.BaseFragment
import com.romankryvolapov.swapi.ui.fragments.planet.list.PlanetAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlanetFragment : BaseFragment<FragmentPlanetBinding, PlanetViewModel>() {

    companion object {
        private const val TAG = "PlanetFragmentTag"
    }

    override fun getViewBinding() = FragmentPlanetBinding.inflate(layoutInflater)
    override val viewModel: PlanetViewModel by viewModel { parametersOf(mainActivityViewModel) }
    private val args: PlanetFragmentArgs by navArgs()
    private val adapter: PlanetAdapter by inject()

    override fun parseArguments() {
        try {
            viewModel.getPlanetDetails(args.planetID)
        } catch (e: IllegalStateException) {
            logError("get args Exception: ${e.message}", e, TAG)
            showBannerMessage(
                state = BannerMessage.State.ERROR,
                message = StringSource(R.string.unknown_error),
                gravity = BannerMessage.Gravity.CENTER,
            )
            viewModel.onBackPressed()
        }
    }

    override fun setupControls() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.enableChangeAnimations(false)
        binding.errorView.actionOneClickListener = {
            hideKeyboard()
            hideErrorState()
            viewModel.onBackPressed()
        }
        binding.emptyStateView.buttonClickListener = {
            hideKeyboard()
            hideErrorState()
            viewModel.onBackPressed()
        }
        binding.toolbar.onClickThrottle {
            hideKeyboard()
            viewModel.onBackPressed()
        }
    }


    override fun subscribeToLiveData() {
        viewModel.adapterLiveData.observe(viewLifecycleOwner) { list ->
            logDebug("subscribeToLiveData size: ${list.size}", TAG)
            adapter.items = list
        }
    }


}