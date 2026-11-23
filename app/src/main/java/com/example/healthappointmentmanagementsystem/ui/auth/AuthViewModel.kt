package com.example.healthappointmentmanagementsystem.ui.auth

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

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        // Default admin account, created on first launch. Change these before release.
        const val DEFAULT_ADMIN_EMAIL = "admin@health.com"
        const val DEFAULT_ADMIN_PASSWORD = "Admin@123"
    }

    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        viewModelScope.launch {
            val existing = userDao.findByEmail(DEFAULT_ADMIN_EMAIL)
            if (existing == null) {
                userDao.insert(
                    User(
                        name = "Admin",
                        email = DEFAULT_ADMIN_EMAIL,
                        passwordHash = PasswordHasher.hash(DEFAULT_ADMIN_PASSWORD),
                        role = "ADMIN"
                    )
                )
            } else if (!existing.role.equals("ADMIN", ignoreCase = true)) {
                // The account was created earlier as a patient. Promote it.
                UserDirectory.setRole(getApplication(), DEFAULT_ADMIN_EMAIL, "ADMIN")
            }
        }
    }

    fun register(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            // Nobody can sign up as an admin or take the admin email.
            if (role.equals("ADMIN", ignoreCase = true) ||
                email.equals(DEFAULT_ADMIN_EMAIL, ignoreCase = true)
            ) {
                _errorMessage.postValue("This email can't be used for registration")
                return@launch
            }
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

    // expectedRole is the option chosen on the login screen.
    // If it is null, the role is not checked.
    fun login(email: String, password: String, expectedRole: String? = null) {
        viewModelScope.launch {
            val user = userDao.findByEmail(email)
            if (user == null || !PasswordHasher.verify(password, user.passwordHash)) {
                _errorMessage.postValue("Invalid email or password")
                return@launch
            }
            if (expectedRole != null && !user.role.equals(expectedRole, ignoreCase = true)) {
                _errorMessage.postValue("This account is not registered as $expectedRole")
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