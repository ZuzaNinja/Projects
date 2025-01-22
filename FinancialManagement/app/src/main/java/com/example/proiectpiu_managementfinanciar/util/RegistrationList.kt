package com.example.proiectpiu_managementfinanciar.util

import android.util.Log
import com.example.proiectpiu_managementfinanciar.models.UserRegistration

object RegistrationList {
    val registrations = mutableListOf<UserRegistration>()

    fun addUser(user: UserRegistration) {
        if (findUserByEmailAndType(user.email, user.password, user.userType) == null) {
            registrations.add(user)
        } else {
            Log.e("ERROR", "User already exists with email: ${user.email} and type: ${user.userType}")
        }
    }

    fun findUserByEmailAndType(email: String, password: String, userType: String): UserRegistration? {
        registrations.forEach {
            Log.d("DEBUG", "Checking against: ${it.email}, ${it.password}, ${it.userType}")
        }

        val user = registrations.find {
            it.email.trim().equals(email.trim()) &&
                    it.password == password &&
                    it.userType.trim().equals(userType.trim())
        }

        if (user != null) {
            Log.d("DEBUG", "User found: ${user.email}")
        } else {
            Log.e("ERROR", "No user found matching the criteria.")
        }

        return user
    }

    fun updateUserPassword(email: String, newPassword: String, userType: String): Boolean {
        val user = registrations.find {
            it.email.trim().equals(email.trim()) &&
                    it.userType.trim().equals(userType.trim())
        }

        return if (user != null) {
            user.password = newPassword
            true
        } else {
            Log.e("ERROR", "User not found for password update.")
            false
        }
    }
}
