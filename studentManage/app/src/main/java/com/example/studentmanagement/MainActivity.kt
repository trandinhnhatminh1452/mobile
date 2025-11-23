package com.example.studentmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentManager.Student
import com.example.studentManager.StudentAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var edtMssv: EditText
    private lateinit var edtHoten: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val studentList = mutableListOf<Student>()
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupListeners()

        // Thêm dữ liệu mẫu
        addSampleData()
    }

    private fun initViews() {
        edtMssv = findViewById(R.id.edtMssv)
        edtHoten = findViewById(R.id.edtHoten)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            studentList,
            onItemClick = { position ->
                selectedPosition = position
                val student = studentList[position]
                edtMssv.setText(student.mssv)
                edtHoten.setText(student.hoten)
            },
            onDeleteClick = { position ->
                studentList.removeAt(position)
                adapter.notifyItemRemoved(position)
                clearInputs()
                selectedPosition = -1
                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            val mssv = edtMssv.text.toString().trim()
            val hoten = edtHoten.text.toString().trim()

            if (mssv.isEmpty() || hoten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(mssv, hoten)
            studentList.add(student)
            adapter.notifyItemInserted(studentList.size - 1)
            clearInputs()
            Toast.makeText(this, "Đã thêm sinh viên", Toast.LENGTH_SHORT).show()
        }

        btnUpdate.setOnClickListener {
            if (selectedPosition == -1) {
                Toast.makeText(this, "Vui lòng chọn sinh viên để cập nhật", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mssv = edtMssv.text.toString().trim()
            val hoten = edtHoten.text.toString().trim()

            if (mssv.isEmpty() || hoten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            studentList[selectedPosition] = Student(mssv, hoten)
            adapter.notifyItemChanged(selectedPosition)
            clearInputs()
            selectedPosition = -1
            Toast.makeText(this, "Đã cập nhật sinh viên", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputs() {
        edtMssv.text.clear()
        edtHoten.text.clear()
        selectedPosition = -1
    }

    private fun addSampleData() {
        studentList.addAll(listOf(
            Student("20200001", "Nguyễn Văn A"),
            Student("20200002", "Trần Thị B"),
            Student("20200003", "Lê Văn C"),
            Student("20200004", "Phạm Thị D"),
            Student("20200005", "Hoàng Văn E"),
            Student("20200006", "Vũ Thị F"),
            Student("20200007", "Đặng Văn G"),
            Student("20200008", "Bùi Thị H"),
            Student("20200009", "Hồ Văn I")
        ))
        adapter.notifyDataSetChanged()
    }
}