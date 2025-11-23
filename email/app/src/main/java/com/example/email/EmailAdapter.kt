package com.example.email

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class EmailAdapter(
    private val emails: List<Email>,
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit,
    private val onStarClick: (Int) -> Unit
) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()

    class EmailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardAvatar: CardView = view.findViewById(R.id.cardAvatar)
        val tvAvatarLetter: TextView = view.findViewById(R.id.tvAvatarLetter)
        val tvSender: TextView = view.findViewById(R.id.tvSender)
        val tvSubject: TextView = view.findViewById(R.id.tvSubject)
        val tvPreview: TextView = view.findViewById(R.id.tvPreview)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val imgStar: ImageView = view.findViewById(R.id.imgStar)
        val imgCheck: ImageView = view.findViewById(R.id.imgCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_email, parent, false)
        return EmailViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        val email = emails[position]

        // Avatar
        holder.tvAvatarLetter.text = email.sender.first().toString().uppercase()
        holder.cardAvatar.setCardBackgroundColor(Color.parseColor(email.avatarColor))

        // Sender
        holder.tvSender.text = email.sender
        holder.tvSender.setTypeface(null, if (email.isUnread) Typeface.BOLD else Typeface.NORMAL)

        // Subject with unread indicator
        val subjectText = if (email.isUnread) " ${email.subject}" else email.subject
        holder.tvSubject.text = subjectText
        holder.tvSubject.setTypeface(null, if (email.isUnread) Typeface.BOLD else Typeface.NORMAL)

        // Preview
        holder.tvPreview.text = email.preview

        // Time
        holder.tvTime.text = email.time
        holder.tvTime.setTypeface(null, if (email.isUnread) Typeface.BOLD else Typeface.NORMAL)

        // Star
        holder.imgStar.setImageResource(
            if (email.isStarred) android.R.drawable.star_big_on
            else android.R.drawable.star_big_off
        )

        // Selection state
        if (selectedPositions.contains(position)) {
            holder.imgCheck.visibility = View.VISIBLE
            holder.cardAvatar.visibility = View.GONE
        } else {
            holder.imgCheck.visibility = View.GONE
            holder.cardAvatar.visibility = View.VISIBLE
        }

        // Click listeners
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(position)
            true
        }

        holder.imgStar.setOnClickListener {
            onStarClick(position)
        }
    }

    override fun getItemCount() = emails.size

    fun setSelectedPositions(positions: Set<Int>) {
        selectedPositions.clear()
        selectedPositions.addAll(positions)
    }
}