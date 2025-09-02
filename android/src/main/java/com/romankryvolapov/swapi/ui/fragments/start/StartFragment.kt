/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.fragments.start

import com.romankryvolapov.swapi.databinding.FragmentStartBinding
import com.romankryvolapov.swapi.ui.base.fragments.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class StartFragment : BaseFragment<FragmentStartBinding, StartViewModel>() {

    companion object {
        private const val TAG = "SplashFragmentTag"
    }

    override fun getViewBinding() = FragmentStartBinding.inflate(layoutInflater)

    override val viewModel: StartViewModel by viewModel { parametersOf(mainActivityViewModel) }

    override fun subscribeToLiveData() {
        viewModel.messageLiveData.observe(viewLifecycleOwner) { message ->
            binding.tvTitle.text = message
        }
    }

}