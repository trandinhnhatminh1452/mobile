package com.example.playstore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SectionAdapter
    private val sections = mutableListOf<Section>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ẩn ActionBar
        supportActionBar?.hide()

        setContentView(R.layout.acitvity_main)

        setupRecyclerView()
        loadSampleData()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)

        adapter = SectionAdapter(sections)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadSampleData() {
        // Section 1: Suggested for you
        sections.add(
            Section(
                title = "Suggested for you",
                isSponsored = true,
                apps = listOf(
                    App(
                        name = "Mech Assemble: Zombie Swarm",
                        category = "Action • Role Playing • Roguelike • Zombie",
                        rating = 4.8f,
                        size = "624 MB",
                        iconColor = "#8B0000",
                        iconText = "💀"
                    ),
                    App(
                        name = "MU: Hồng Hoá Bảo",
                        category = "Role Playing",
                        rating = 4.8f,
                        size = "339 MB",
                        iconColor = "#2C1810",
                        iconText = "MU"
                    ),
                    App(
                        name = "War Inc: Rising",
                        category = "Strategy • Tower defense",
                        rating = 4.9f,
                        size = "231 MB",
                        iconColor = "#1E88E5",
                        iconText = "⚔️"
                    )
                ),
                layoutType = LayoutType.LIST
            )
        )

        // Section 2: Recommended for you
        sections.add(
            Section(
                title = "Recommended for you",
                isSponsored = false,
                apps = listOf(
                    App(
                        name = "Suno - AI Music &",
                        category = "Music",
                        rating = 4.6f,
                        size = "45 MB",
                        iconColor = "#FF6B35",
                        iconText = "SUNO"
                    ),
                    App(
                        name = "Claude by",
                        category = "Productivity",
                        rating = 4.9f,
                        size = "32 MB",
                        iconColor = "#D4A373",
                        iconText = "✨"
                    ),
                    App(
                        name = "DramaBox -",
                        category = "Entertainment",
                        rating = 4.5f,
                        size = "78 MB",
                        iconColor = "#E91E63",
                        iconText = "▶"
                    ),
                    App(
                        name = "Pixel Studio",
                        category = "Art & Design",
                        rating = 4.7f,
                        size = "25 MB",
                        iconColor = "#9C27B0",
                        iconText = "🎨"
                    )
                ),
                layoutType = LayoutType.GRID
            )
        )

        adapter.notifyDataSetChanged()
    }
}