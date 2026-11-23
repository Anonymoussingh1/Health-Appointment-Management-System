package com.example.healthappointmentmanagementsystem.data

import android.content.Context
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserDirectory {

    data class Person(val userId: Int, val name: String, val email: String)

    suspend fun byRole(context: Context, role: String): List<Person> =
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context.applicationContext)
            db.query(
                "SELECT userId, name, email FROM users WHERE UPPER(role) = ? ORDER BY name",
                arrayOf<Any?>(role.uppercase())
            ).use { cursor ->
                val people = mutableListOf<Person>()
                while (cursor.moveToNext()) {
                    people.add(
                        Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
                    )
                }
                people
            }
        }

    // Only doctor and patient accounts can be removed this way, never an admin.
    suspend fun delete(context: Context, userId: Int, role: String) =
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context.applicationContext)
            db.openHelper.writableDatabase.execSQL(
                "DELETE FROM users WHERE userId = ? AND UPPER(role) = ?",
                arrayOf<Any?>(userId, role.uppercase())
            )
        }

    // Changes the role of the account with this email.
    suspend fun setRole(context: Context, email: String, role: String) =
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context.applicationContext)
            db.openHelper.writableDatabase.execSQL(
                "UPDATE users SET role = ? WHERE email = ?",
                arrayOf<Any?>(role.uppercase(), email)
            )
        }
}