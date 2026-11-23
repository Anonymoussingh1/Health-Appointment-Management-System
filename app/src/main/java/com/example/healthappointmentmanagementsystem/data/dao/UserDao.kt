package com.example.healthappointmentmanagementsystem.data.dao

import android.content.Context
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import com.example.healthappointmentmanagementsystem.data.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDao(private val context: Context) {
    suspend fun insert(user: User): Long = withContext(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(context).writableDatabase
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS users (userId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, passwordHash TEXT, role TEXT, hospital TEXT, specialty TEXT)",
            arrayOf()
        )
        val stmt = db.compileStatement("INSERT INTO users (name, email, passwordHash, role, hospital, specialty) VALUES (?, ?, ?, ?, ?, ?)")
        stmt.bindString(1, user.name)
        stmt.bindString(2, user.email)
        stmt.bindString(3, user.passwordHash)
        stmt.bindString(4, user.role)
        stmt.bindString(5, user.hospital ?: "")
        stmt.bindString(6, user.specialty ?: "")
        stmt.executeInsert()
    }

    suspend fun findByEmail(email: String): User? = withContext(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(context).readableDatabase
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS users (userId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, passwordHash TEXT, role TEXT, hospital TEXT, specialty TEXT)",
            arrayOf()
        )
        db.rawQuery("SELECT userId, name, email, passwordHash, role, hospital, specialty FROM users WHERE email = ? LIMIT 1", arrayOf(email)).use { cursor ->
            if (cursor.moveToNext()) {
                User(
                    userId = cursor.getInt(0),
                    name = cursor.getString(1),
                    email = cursor.getString(2),
                    passwordHash = cursor.getString(3),
                    role = cursor.getString(4),
                    hospital = if (cursor.isNull(5)) null else cursor.getString(5),
                    specialty = if (cursor.isNull(6)) null else cursor.getString(6)
                )
            } else {
                null
            }
        }
    }

    suspend fun findById(id: Int): User? = withContext(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(context).readableDatabase
        db.rawQuery("SELECT userId, name, email, passwordHash, role, hospital, specialty FROM users WHERE userId = ? LIMIT 1", arrayOf(id.toString())).use { cursor ->
            if (cursor.moveToNext()) {
                User(
                    userId = cursor.getInt(0),
                    name = cursor.getString(1),
                    email = cursor.getString(2),
                    passwordHash = cursor.getString(3),
                    role = cursor.getString(4),
                    hospital = if (cursor.isNull(5)) null else cursor.getString(5),
                    specialty = if (cursor.isNull(6)) null else cursor.getString(6)
                )
            } else {
                null
            }
        }
    }
}
