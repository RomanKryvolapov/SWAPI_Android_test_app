/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.ui.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.romankryvolapov.swapi.R
import com.romankryvolapov.swapi.databinding.ListItemSpinnerBinding
import com.romankryvolapov.swapi.extensions.inflateBinding
import com.romankryvolapov.swapi.extensions.setTextColorResource
import com.romankryvolapov.swapi.extensions.setTextResource
import com.romankryvolapov.swapi.extensions.setTextSource
import com.romankryvolapov.swapi.models.list.CommonSimpleSpinnerColour
import com.romankryvolapov.swapi.models.list.CommonSimpleSpinnerUi

class SimpleSpinnerAdapter<T>(
    context: Context,
    private val clickListener: ((model: CommonSimpleSpinnerUi<T>) -> Unit)
) : ArrayAdapter<CommonSimpleSpinnerUi<T>>(context, 0, mutableListOf()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return parent.inflateBinding(ListItemSpinnerBinding::inflate).run {
            val item = getItem(position)!!
            if (item.valueFirstLine.getString(root.context).isEmpty()) {
                tvValueFirstLine.setTextResource(R.string.empty)
            } else {
                tvValueFirstLine.setTextSource(item.valueFirstLine)
            }
            if (item.valueSecondLine != null) {
                tvValueSecondLine.setTextSource(item.valueSecondLine)
                tvValueSecondLine.visibility = View.VISIBLE
            } else {
                tvValueSecondLine.text = ""
                tvValueSecondLine.visibility = View.GONE
            }
            if (item.valueThirdLine != null) {
                tvValueThirdLine.setTextSource(item.valueThirdLine)
                tvValueThirdLine.visibility = View.VISIBLE
            } else {
                tvValueThirdLine.text = ""
                tvValueThirdLine.visibility = View.GONE
            }
            tvValueFirstLine.setTextColorResource(
                when (item.colour) {
                    CommonSimpleSpinnerColour.WHITE -> R.color.color_text
                    CommonSimpleSpinnerColour.GREEN -> R.color.color_text_light_green
                    CommonSimpleSpinnerColour.RED -> R.color.color_error_light
                }
            )
            root.setOnClickListener {
                clickListener.invoke(item)
            }
            root
        }
    }

    fun swapItems(items: List<CommonSimpleSpinnerUi<T>>) {
        clear()
        addAll(items)
        notifyDataSetChanged()
    }
}