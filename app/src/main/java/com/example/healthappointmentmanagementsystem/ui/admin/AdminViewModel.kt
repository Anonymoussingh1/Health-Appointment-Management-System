package com.example.healthappointmentmanagementsystem.ui.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthappointmentmanagementsystem.data.UserDirectory
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import com.example.healthappointmentmanagementsystem.data.entities.User
import com.example.healthappointmentmanagementsystem.util.PasswordHasher
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _doctors = MutableLiveData<List<UserDirectory.Person>>(emptyList())
    val doctors: LiveData<List<UserDirectory.Person>> get() = _doctors

    private val _patients = MutableLiveData<List<UserDirectory.Person>>(emptyList())
    val patients: LiveData<List<UserDirectory.Person>> get() = _patients

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { loadAll() }
    }

    private suspend fun loadAll() {
        val app = getApplication<Application>()
        _doctors.postValue(UserDirectory.byRole(app, "DOCTOR"))
        _patients.postValue(UserDirectory.byRole(app, "PATIENT"))
    }

    // onDone receives an error message, or null when the account was added.
    fun addUser(
        name: String,
        email: String,
        password: String,
        role: String,
        onDone: (String?) -> Unit
    ) {
        viewModelScope.launch {
            if (userDao.findByEmail(email) != null) {
                onDone("An account with this email already exists")
                return@launch
            }
            userDao.insert(
                User(
                    name = name,
                    email = email,
                    passwordHash = PasswordHasher.hash(password),
                    role = role.uppercase()
                )
            )
            loadAll()
            onDone(null)
        }
    }

    fun removeUser(person: UserDirectory.Person, role: String) {
        viewModelScope.launch {
            UserDirectory.delete(getApplication(), person.userId, role)
            loadAll()
        }
    }
}