/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.fragments.main

import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.databinding.FragmentFlowContainerBinding
import com.romankryvolapov.swapi.ui.base.fragments.flow.BaseFlowFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainFlowFragment : BaseFlowFragment<FragmentFlowContainerBinding, MainFlowViewModel>() {

    companion object {
        private const val TAG = "MainFlowFragmentTag"
    }

    override fun getViewBinding() = FragmentFlowContainerBinding.inflate(layoutInflater)
    override val viewModel: MainFlowViewModel by viewModel { parametersOf(mainActivityViewModel) }

    override fun getFlowGraph() = R.navigation.nav_main

    override fun getStartDestination() = viewModel.getStartDestination()

}