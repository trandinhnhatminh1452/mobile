package com.example.email

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmailAdapter
    private val emailList = mutableListOf<Email>()
    private val selectedEmails = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ẩn ActionBar
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        setupRecyclerView()
        loadSampleData()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)

        adapter = EmailAdapter(
            emailList,
            onItemClick = { position ->
                Toast.makeText(this, "Clicked: ${emailList[position].sender}", Toast.LENGTH_SHORT).show()
            },
            onItemLongClick = { position ->
                toggleSelection(position)
            },
            onStarClick = { position ->
                emailList[position].isStarred = !emailList[position].isStarred
                adapter.notifyItemChanged(position)
                Toast.makeText(
                    this,
                    if (emailList[position].isStarred) "Added to starred" else "Removed from starred",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun toggleSelection(position: Int) {
        if (selectedEmails.contains(position)) {
            selectedEmails.remove(position)
        } else {
            selectedEmails.add(position)
        }
        adapter.setSelectedPositions(selectedEmails)
        adapter.notifyItemChanged(position)
    }

    private fun loadSampleData() {
        emailList.addAll(listOf(
            Email(
                sender = "Edurila.com",
                subject = "$19 Only (First 10 spots) - Bestselling...",
                preview = "Are you looking to Learn Web Designin...",
                time = "12:34 PM",
                avatarColor = "#4285F4",
                isUnread = true
            ),
            Email(
                sender = "Chris Abad",
                subject = "Help make Campaign Monitor better",
                preview = "Let us know your thoughts! No Images...",
                time = "11:22 AM",
                avatarColor = "#EA4335",
                isUnread = true
            ),
            Email(
                sender = "Tuto.com",
                subject = "8h de formation gratuite et les nouvea...",
                preview = "Photoshop, SEO, Blender, CSS, WordPre...",
                time = "11:04 AM",
                avatarColor = "#34A853",
                isUnread = true
            ),
            Email(
                sender = "support",
                subject = "Société Ovh : suivi de vos services - hp...",
                preview = "SAS OVH - http://www.ovh.com 2 rue K...",
                time = "10:26 AM",
                avatarColor = "#9AA0A6",
                isUnread = true
            ),
            Email(
                sender = "Matt from Ionic",
                subject = "The New Ionic Creator Is Here!",
                preview = "Announcing the all-new Creator, build...",
                time = "9:15 AM",
                avatarColor = "#34A853",
                isUnread = false
            )
        ))
        adapter.notifyDataSetChanged()
    }
}