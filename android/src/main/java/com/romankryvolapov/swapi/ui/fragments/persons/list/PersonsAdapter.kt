package com.romankryvolapov.swapi.ui.fragments.persons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.romankryvolapov.swapi.databinding.ListItemPersonBinding
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.extensions.onClickThrottle
import com.romankryvolapov.swapi.models.persons.PersonUi
import com.romankryvolapov.swapi.utils.DefaultDiffUtilCallback

class PersonsAdapter :
    PagingDataAdapter<PersonUi, PersonsAdapter.ListViewHolder>(DefaultDiffUtilCallback()) {

    companion object {
        private const val TAG = "PersonsAdapterTag"
    }

    var clickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ListItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ListViewHolder(
        private val binding: ListItemPersonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PersonUi) {
            logDebug("bind name: ${model.name}", TAG)
            binding.tvName.text = model.name
            binding.root.onClickThrottle {
                clickListener?.onPersonClicked(model)
            }
        }
    }

    interface ClickListener {
        fun onPersonClicked(model: PersonUi)
    }

}