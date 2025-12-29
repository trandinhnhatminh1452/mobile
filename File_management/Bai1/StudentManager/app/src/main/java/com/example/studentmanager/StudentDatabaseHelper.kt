package com.example.studentmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "student_manager.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_STUDENTS = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_MSSV = "mssv"
        const val COLUMN_HO_TEN = "ho_ten"
        const val COLUMN_SO_DIEN_THOAI = "so_dien_thoai"
        const val COLUMN_DIA_CHI = "dia_chi"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_STUDENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MSSV TEXT NOT NULL UNIQUE,
                $COLUMN_HO_TEN TEXT NOT NULL,
                $COLUMN_SO_DIEN_THOAI TEXT NOT NULL,
                $COLUMN_DIA_CHI TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    // Thêm sinh viên mới
    fun insertStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_HO_TEN, student.hoTen)
            put(COLUMN_SO_DIEN_THOAI, student.soDienThoai)
            put(COLUMN_DIA_CHI, student.diaChi)
        }
        return db.insert(TABLE_STUDENTS, null, values)
    }

    // Lấy tất cả sinh viên
    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_HO_TEN ASC"
        )

        with(cursor) {
            while (moveToNext()) {
                val mssv = getString(getColumnIndexOrThrow(COLUMN_MSSV))
                val hoTen = getString(getColumnIndexOrThrow(COLUMN_HO_TEN))
                val soDienThoai = getString(getColumnIndexOrThrow(COLUMN_SO_DIEN_THOAI))
                val diaChi = getString(getColumnIndexOrThrow(COLUMN_DIA_CHI))
                students.add(Student(mssv, hoTen, soDienThoai, diaChi))
            }
            close()
        }
        return students
    }

    // Cập nhật thông tin sinh viên
    fun updateStudent(oldMssv: String, student: Student): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_HO_TEN, student.hoTen)
            put(COLUMN_SO_DIEN_THOAI, student.soDienThoai)
            put(COLUMN_DIA_CHI, student.diaChi)
        }
        return db.update(
            TABLE_STUDENTS,
            values,
            "$COLUMN_MSSV = ?",
            arrayOf(oldMssv)
        )
    }

    // Xóa sinh viên
    fun deleteStudent(mssv: String): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_STUDENTS,
            "$COLUMN_MSSV = ?",
            arrayOf(mssv)
        )
    }

    // Kiểm tra MSSV đã tồn tại chưa
    fun isStudentExists(mssv: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            arrayOf(COLUMN_MSSV),
            "$COLUMN_MSSV = ?",
            arrayOf(mssv),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
