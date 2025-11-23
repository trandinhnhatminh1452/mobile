package com.example.playstore

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AppAdapter(
    private val apps: List<App>,
    private val layoutType: LayoutType
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewHolder for List layout
    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardIcon: CardView = view.findViewById(R.id.cardIcon)
        val tvIconText: TextView = view.findViewById(R.id.tvIconText)
        val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvSize: TextView = view.findViewById(R.id.tvSize)
    }

    // ViewHolder for Grid layout
    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardIcon: CardView = view.findViewById(R.id.cardIconGrid)
        val tvIconText: TextView = view.findViewById(R.id.tvIconTextGrid)
        val tvAppName: TextView = view.findViewById(R.id.tvAppNameGrid)
        val tvCategory: TextView = view.findViewById(R.id.tvCategoryGrid)
    }

    override fun getItemViewType(position: Int): Int {
        return when (layoutType) {
            LayoutType.LIST -> 0
            LayoutType.GRID -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_app_list, parent, false)
                ListViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_app_grid, parent, false)
                GridViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = apps[position]

        if (holder is ListViewHolder) {
            holder.cardIcon.setCardBackgroundColor(Color.parseColor(app.iconColor))
            holder.tvIconText.text = app.iconText
            holder.tvAppName.text = app.name
            holder.tvCategory.text = app.category
            holder.tvRating.text = "⭐ ${app.rating}"
            holder.tvSize.text = app.size
        } else if (holder is GridViewHolder) {
            holder.cardIcon.setCardBackgroundColor(Color.parseColor(app.iconColor))
            holder.tvIconText.text = app.iconText
            holder.tvAppName.text = app.name
            holder.tvCategory.text = app.category
        }
    }

    override fun getItemCount() = apps.size
}