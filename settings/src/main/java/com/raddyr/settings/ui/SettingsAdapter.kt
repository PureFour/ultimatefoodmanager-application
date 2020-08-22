package com.raddyr.settings.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raddyr.settings.R
import kotlinx.android.synthetic.main.settings_item.view.*

class SettingsAdapter(private val list: List<Pair<String, () -> Unit>>) :
    RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder =
        SettingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.settings_item, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(list[position].first, list[position].second)
    }

    inner class SettingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: String, listener: () -> Unit) {
            with(itemView) {
                label.text = item
                setOnClickListener {
                    listener.invoke()
                }
            }
        }
    }
}