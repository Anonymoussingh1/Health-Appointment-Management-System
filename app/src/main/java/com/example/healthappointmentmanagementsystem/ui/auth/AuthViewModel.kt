package com.example.healthappointmentmanagementsystem.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthappointmentmanagementsystem.data.database.AppDatabase
import com.example.healthappointmentmanagementsystem.data.entities.User
import com.example.healthappointmentmanagementsystem.util.PasswordHasher
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun register(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            if (userDao.findByEmail(email) != null) {
                _errorMessage.postValue("An account with this email already exists")
                return@launch
            }
            val user = User(
                name = name,
                email = email,
                passwordHash = PasswordHasher.hash(password),
                role = role
            )
            val id = userDao.insert(user)
            _currentUser.postValue(user.copy(userId = id.toInt()))
            _errorMessage.postValue(null)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userDao.findByEmail(email)
            if (user == null || !PasswordHasher.verify(password, user.passwordHash)) {
                _errorMessage.postValue("Invalid email or password")
                return@launch
            }
            _currentUser.postValue(user)
            _errorMessage.postValue(null)
        }
    }

    fun logout() {
        _currentUser.postValue(null)
    }
}