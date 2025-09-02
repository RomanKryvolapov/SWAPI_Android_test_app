package com.romankryvolapov.swapi.ui.fragments.planet.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.romankryvolapov.swapi.databinding.ListItemPlanetFieldBinding
import com.romankryvolapov.swapi.extensions.inflateBinding
import com.romankryvolapov.swapi.models.planets.PlanetAdapterMarker
import com.romankryvolapov.swapi.models.planets.PlanetUiField

class PlanetFieldDelegate() : AdapterDelegate<MutableList<PlanetAdapterMarker>>() {

    companion object {
        private const val TAG = "PlanetFieldDelegateTag"
    }

    override fun isForViewType(
        items: MutableList<PlanetAdapterMarker>,
        position: Int
    ): Boolean {
        return items[position] is PlanetUiField
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflateBinding(ListItemPlanetFieldBinding::inflate))
    }

    override fun onBindViewHolder(
        items: MutableList<PlanetAdapterMarker>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as PlanetUiField)
    }

    private inner class ViewHolder(
        private val binding: ListItemPlanetFieldBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PlanetUiField) {
            binding.tvTitle.text = model.title
            binding.tvDescription.text = model.description
        }
    }

}