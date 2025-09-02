/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.withStyledAttributes
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.databinding.LayoutToolbarBinding
import com.romankryvolapov.swapi.extensions.onClickThrottle
import com.romankryvolapov.swapi.extensions.setTextResource
import com.romankryvolapov.swapi.extensions.setTextSource
import com.romankryvolapov.swapi.extensions.tintRes
import com.romankryvolapov.swapi.models.common.StringSource

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ToolbarViewTag"
    }

    private val binding = LayoutToolbarBinding.inflate(LayoutInflater.from(context), this)

    var clickListener: (() -> Unit)? = null

    init {
        setupView()
        setupAttributes(attrs)
        setupControls()
    }

    private fun setupView() {
        orientation = HORIZONTAL
        setBackgroundResource(R.drawable.bg_top_bar)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.ToolbarView) {
            val title = getString(R.styleable.ToolbarView_toolbar_title)
            if (!title.isNullOrEmpty()) {
                binding.tvTitle.visibility = View.VISIBLE
                binding.tvTitle.text = title
            } else {
                binding.tvTitle.visibility = View.INVISIBLE
            }
            val titleSecondLine = getString(R.styleable.ToolbarView_toolbar_title_second_line)
            if (!titleSecondLine.isNullOrEmpty()) {
                binding.tvTitleSecondLine.visibility = View.VISIBLE
                binding.tvTitleSecondLine.text = titleSecondLine
            } else {
                binding.tvTitleSecondLine.visibility = View.GONE
            }
            val showNavigationIcon = getBoolean(
                R.styleable.ToolbarView_toolbar_show_navigation_icon,
                false
            )
            binding.icNavigation.visibility = if (showNavigationIcon) View.VISIBLE
            else View.GONE
        }
    }

    private fun setupControls() {
        binding.root.onClickThrottle {
            clickListener?.invoke()
        }
    }

    fun setTitleText(text: StringSource) {
        binding.tvTitle.setTextSource(text)
        binding.tvTitle.visibility = View.VISIBLE
    }

    fun setTitleText(
        text: String,
    ) {
        binding.tvTitle.text = text
        binding.tvTitle.visibility = View.VISIBLE
    }


    fun setTitleText(
        text: String,
        secondLineText: String?,
    ) {
        binding.tvTitle.text = text
        binding.tvTitle.visibility = View.VISIBLE
        if (!secondLineText.isNullOrEmpty()) {
            binding.tvTitleSecondLine.text = secondLineText
            binding.tvTitleSecondLine.visibility = View.VISIBLE
        } else {
            binding.tvTitleSecondLine.visibility = View.GONE
        }
    }

    fun setTitleText(
        @StringRes resource: Int,
        arg: String?
    ) {
        logDebug("setTitleText $resource arg: $arg", TAG)
        binding.tvTitle.setTextResource(resource, arg)
        binding.tvTitle.visibility = View.VISIBLE
    }

    fun setNavigationIcon(
        @DrawableRes navigationIconRes: Int = R.drawable.ic_arrow_left,
        @ColorRes navigationIconColorRes: Int = R.color.color_white,
    ) {
        binding.icNavigation.setImageResource(navigationIconRes)
        binding.icNavigation.tintRes(navigationIconColorRes)
        binding.icNavigation.visibility = View.VISIBLE
    }

    fun hideNavigationIcon() {
        binding.icNavigation.visibility = View.INVISIBLE
        clickListener = null
    }

}