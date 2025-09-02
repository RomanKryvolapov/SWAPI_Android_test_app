/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.utils

import androidx.recyclerview.widget.DiffUtil
import com.romankryvolapov.swapi.models.common.DiffEquals

class DefaultDiffUtilCallback<T: DiffEquals> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isContentSame(newItem)
    }

}