/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.fragments.start.flow

import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.databinding.FragmentFlowContainerBinding
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.models.common.StartDestination
import com.romankryvolapov.swapi.ui.base.fragments.flow.BaseFlowFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class StartFlowFragment : BaseFlowFragment<FragmentFlowContainerBinding, StartFlowViewModel>() {

    companion object {
        private const val TAG = "StartFlowFragmentTag"
    }

    override fun getViewBinding() = FragmentFlowContainerBinding.inflate(layoutInflater)

    override val viewModel: StartFlowViewModel by viewModel { parametersOf(mainActivityViewModel) }

    override fun getFlowGraph() = R.navigation.nav_start

    override fun getStartDestination(): StartDestination {
        return StartDestination(R.id.startFragment)
    }

    override fun onCreated() {
        super.onCreated()
        logDebug("onCreated", TAG)
    }

}