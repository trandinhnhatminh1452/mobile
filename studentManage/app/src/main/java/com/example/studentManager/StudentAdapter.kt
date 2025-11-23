package com.example.studentManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagement.R

class StudentAdapter(
    private val students: MutableList<Student>,
    private val onItemClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHoten: TextView = view.findViewById(R.id.tvHoten)
        val tvMssv: TextView = view.findViewById(R.id.tvMssv)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvHoten.text = student.hoten
        holder.tvMssv.text = student.mssv

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount() = students.size
}