package com.example.studentmanager

import android.content.Context

class StudentRepository(context: Context) {
    private val dbHelper = StudentDatabaseHelper(context)

    fun addStudent(student: Student): Boolean {
        return dbHelper.insertStudent(student) != -1L
    }

    fun getAllStudents(): List<Student> {
        return dbHelper.getAllStudents()
    }

    fun updateStudent(oldMssv: String, student: Student): Boolean {
        return dbHelper.updateStudent(oldMssv, student) > 0
    }

    fun deleteStudent(mssv: String): Boolean {
        return dbHelper.deleteStudent(mssv) > 0
    }

    fun isStudentExists(mssv: String): Boolean {
        return dbHelper.isStudentExists(mssv)
    }
}