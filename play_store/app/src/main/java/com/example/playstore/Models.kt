package com.example.playstore

data class App(
    val name: String,
    val category: String,
    val rating: Float,
    val size: String,
    val iconColor: String,
    val iconText: String
)

data class Section(
    val title: String,
    val isSponsored: Boolean,
    val apps: List<App>,
    val layoutType: LayoutType
)

enum class LayoutType {
    LIST,   // Vertical list
    GRID    // Horizontal grid
}