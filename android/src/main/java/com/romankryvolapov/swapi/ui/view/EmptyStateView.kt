/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.databinding.LayoutEmptyStateBinding
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.extensions.onClickThrottle
import com.romankryvolapov.swapi.extensions.setBackgroundColorResource

class EmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "EmptyStateViewTag"
    }


    private val binding = LayoutEmptyStateBinding.inflate(LayoutInflater.from(context), this)

    var buttonClickListener: (() -> Unit)? = null

    init {
        setBackgroundColorResource(R.color.color_background)
        setupAttributes(attrs)
        setupControls()
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.EmptyStateView) {
            getBoolean(R.styleable.EmptyStateView_empty_state_view_show_title, true).let {
                logDebug("tvTitle isVisible: $it", TAG)
                binding.tvTitle.isVisible = it
            }
            getBoolean(R.styleable.EmptyStateView_empty_state_view_show_description, true).let {
                logDebug("tvDesctiption isVisible: $it", TAG)
                binding.tvDesctiption.isVisible = it
            }
            getBoolean(R.styleable.EmptyStateView_empty_state_view_show_button, true).let {
                logDebug("btnEmptyStateViewReload isVisible: $it", TAG)
                binding.btnEmptyStateViewReload.isVisible = it
            }
            getString(R.styleable.EmptyStateView_empty_state_view_title)?.let {
                logDebug("tvTitle text: $it", TAG)
                binding.tvTitle.text = it
            }
            getString(R.styleable.EmptyStateView_empty_state_view_description)?.let {
                logDebug("tvDesctiption text: $it", TAG)
                binding.tvDesctiption.text = it
            }
            getString(R.styleable.EmptyStateView_empty_state_view_button_title)?.let {
                logDebug("btnEmptyStateViewReload text: $it", TAG)
                binding.btnEmptyStateViewReload.text = it
            }
        }
    }

    private fun setupControls() {
        binding.btnEmptyStateViewReload.onClickThrottle {
            buttonClickListener?.invoke()
        }
    }

}