package com.example.playstore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SectionAdapter(
    private val sections: MutableList<Section>
) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSectionTitle: TextView = view.findViewById(R.id.tvSectionTitle)
        val tvSponsored: TextView = view.findViewById(R.id.tvSponsored)
        val imgMore: ImageView = view.findViewById(R.id.imgMore)
        val imgArrow: ImageView = view.findViewById(R.id.imgArrow)
        val recyclerViewApps: RecyclerView = view.findViewById(R.id.recyclerViewApps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]

        // Section title
        holder.tvSectionTitle.text = section.title

        // Sponsored badge
        if (section.isSponsored) {
            holder.tvSponsored.visibility = View.VISIBLE
            holder.imgMore.visibility = View.VISIBLE
            holder.imgArrow.visibility = View.GONE
        } else {
            holder.tvSponsored.visibility = View.GONE
            holder.imgMore.visibility = View.GONE
            holder.imgArrow.visibility = View.VISIBLE
        }

        // Setup nested RecyclerView
        val appAdapter = AppAdapter(section.apps, section.layoutType)

        when (section.layoutType) {
            LayoutType.LIST -> {
                holder.recyclerViewApps.layoutManager = LinearLayoutManager(
                    holder.itemView.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
            LayoutType.GRID -> {
                holder.recyclerViewApps.layoutManager = LinearLayoutManager(
                    holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }

        holder.recyclerViewApps.adapter = appAdapter
        holder.recyclerViewApps.setHasFixedSize(true)
    }

    override fun getItemCount() = sections.size
}