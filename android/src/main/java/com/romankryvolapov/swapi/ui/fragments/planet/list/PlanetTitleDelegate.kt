package com.romankryvolapov.swapi.ui.fragments.planet.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.romankryvolapov.swapi.databinding.ListItemPlanetTitleBinding
import com.romankryvolapov.swapi.extensions.inflateBinding
import com.romankryvolapov.swapi.models.planets.PlanetAdapterMarker
import com.romankryvolapov.swapi.models.planets.PlanetUiTitle

class PlanetTitleDelegate() : AdapterDelegate<MutableList<PlanetAdapterMarker>>() {

    companion object {
        private const val TAG = "PlanetTitleDelegateTag"
    }

    override fun isForViewType(
        items: MutableList<PlanetAdapterMarker>,
        position: Int
    ): Boolean {
        return items[position] is PlanetUiTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflateBinding(ListItemPlanetTitleBinding::inflate))
    }

    override fun onBindViewHolder(
        items: MutableList<PlanetAdapterMarker>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as PlanetUiTitle)
    }

    private inner class ViewHolder(
        private val binding: ListItemPlanetTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PlanetUiTitle) {
            binding.tvTitle.text = model.title
            binding.tvDescription.text = model.description
        }
    }

}