package com.example.healthappointmentmanagementsystem.data

import android.content.Context
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserDirectory {

    data class Person(val userId: Int, val name: String, val email: String, val specialty: String? = null, val hospital: String? = null)

    suspend fun byRole(context: Context, role: String): List<Person> =
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context).readableDatabase
            db.rawQuery(
                "SELECT userId, name, email, specialty, hospital FROM users WHERE UPPER(role) = ? ORDER BY name",
                arrayOf(role.uppercase())
            ).use { cursor ->
                val people = mutableListOf<Person>()
                while (cursor.moveToNext()) {
                    people.add(
                        Person(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            if (cursor.isNull(3)) null else cursor.getString(3),
                            if (cursor.isNull(4)) null else cursor.getString(4)
                        )
                    )
                }
                people
            }
        }

    suspend fun delete(context: Context, userId: Int, role: String) =
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context).writableDatabase
            db.execSQL(
                "DELETE FROM users WHERE userId = ? AND UPPER(role) = ?",
                arrayOf<Any?>(userId, role.uppercase())
            )
        }

    suspend fun setRole(context: Context, email: String, role: String) =
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context).writableDatabase
            db.execSQL(
                "UPDATE users SET role = ? WHERE email = ?",
                arrayOf<Any?>(role.uppercase(), email)
            )
        }
}
