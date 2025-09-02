/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.databinding.LayoutLoaderBinding
import com.romankryvolapov.swapi.extensions.setBackgroundColorResource

class LoaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "LoaderViewTag"
    }

    private val binding = LayoutLoaderBinding.inflate(LayoutInflater.from(context), this)

    init {
        setBackgroundColorResource(R.color.color_background)
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.LoaderView) {
            getString(R.styleable.LoaderView_loader_view_message)?.let {
                logDebug("tvMessage text: $it", TAG)
                binding.tvMessage.text = it
            }
        }
    }

    fun setMessage(message: String) {
        binding.tvMessage.text = message
    }

}