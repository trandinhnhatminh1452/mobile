package com.example.filemanagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(private val context: Context, val files: List<File>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    var listener: OnFileClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.tvName.text = file.name
        if (file.isDirectory) {
            holder.ivIcon.setImageResource(R.drawable.ic_folder)
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_file)
        }

        holder.itemView.setOnClickListener {
            listener?.onFileClick(file)
        }

        holder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            if (file.isDirectory) {
                menu.add(position, 0, 0, "Rename")
                menu.add(position, 1, 0, "Delete")
            } else {
                menu.add(position, 0, 0, "Rename")
                menu.add(position, 1, 0, "Delete")
                menu.add(position, 2, 0, "Copy")
            }
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
    }

    interface OnFileClickListener {
        fun onFileClick(file: File)
    }
}