package com.example.studentmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StudentRepository(application)

    private val _students = MutableLiveData<MutableList<Student>>(mutableListOf())
    val students: LiveData<MutableList<Student>> = _students

    init {
        loadStudents()
    }

    fun loadStudents() {
        _students.value = repository.getAllStudents().toMutableList()
    }

    fun addStudent(student: Student): Boolean {
        return if (repository.addStudent(student)) {
            loadStudents()
            true
        } else {
            false
        }
    }

    fun updateStudent(position: Int, student: Student): Boolean {
        val current = _students.value ?: return false
        if (position !in current.indices) return false

        val oldMssv = current[position].mssv
        return if (repository.updateStudent(oldMssv, student)) {
            loadStudents()
            true
        } else {
            false
        }
    }

    fun deleteStudent(position: Int): Boolean {
        val current = _students.value ?: return false
        if (position !in current.indices) return false

        val mssv = current[position].mssv
        return if (repository.deleteStudent(mssv)) {
            loadStudents()
            true
        } else {
            false
        }
    }

    fun isStudentExists(mssv: String): Boolean {
        return repository.isStudentExists(mssv)
    }
}