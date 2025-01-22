package com.example.proiectpiu_managementfinanciar.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.util.RegistrationList

class MyAccountActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var profileImage: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var passwordEditText: EditText
    private lateinit var togglePasswordButton: Button
    private lateinit var changePasswordButton: Button
    private lateinit var savePasswordButton: Button

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        initializeViews()
        loadUserData()
        setupListeners()
    }

    private fun initializeViews() {
        profileImage = findViewById(R.id.profile_image)
        usernameTextView = findViewById(R.id.username_text)
        emailTextView = findViewById(R.id.email_text)
        phoneTextView = findViewById(R.id.phone_text)
        passwordEditText = findViewById(R.id.password_text)
        logoutButton = findViewById(R.id.logout_button)
        togglePasswordButton = findViewById(R.id.toggle_password_visibility)
        changePasswordButton = findViewById(R.id.change_password_button)
        savePasswordButton = findViewById(R.id.save_password_button)

        passwordEditText.isEnabled = false
        passwordEditText.isFocusable = false
        passwordEditText.isFocusableInTouchMode = false
        setPasswordMask(true)
    }

    private fun setPasswordMask(mask: Boolean) {
        passwordEditText.inputType = if (mask) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT
        }
    }

    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "N/A")
        val userType = sharedPreferences.getString("USER_TYPE", "N/A")
        val phoneNumber = sharedPreferences.getString("USER_PHONE", "0000000000")
        val storedPassword = sharedPreferences.getString("USER_PASSWORD", "******")

        val registeredUser = RegistrationList.findUserByEmailAndType(userEmail ?: "", storedPassword ?: "", userType ?: "")

        if (registeredUser != null) {
            emailTextView.text = registeredUser.email
            usernameTextView.text = registeredUser.name
            phoneTextView.text = registeredUser.phone
            passwordEditText.setText("*******")
        } else {
            emailTextView.text = userEmail
            usernameTextView.text = userType
            phoneTextView.text = phoneNumber
            passwordEditText.setText("*******")
        }

        if (userType?.trim() == getString(R.string.role_parent).trim()){
            profileImage.setImageResource(R.drawable.adults)
        } else {
            profileImage.setImageResource(R.drawable.teenagers)
        }
    }



    private fun setupListeners() {
        logoutButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putBoolean("IS_LOGGED_IN", false)
            editor.apply()

            startActivity(Intent(this, StartActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }


        togglePasswordButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val password = sharedPreferences.getString("USER_PASSWORD", "******")

            passwordEditText.setText(password)
            setPasswordMask(false)
            togglePasswordButton.isEnabled = false

            handler.postDelayed({
                passwordEditText.setText("*******")
                setPasswordMask(true)
                togglePasswordButton.isEnabled = true
            }, 1000)
        }

        changePasswordButton.setOnClickListener {
            passwordEditText.isEnabled = true
            passwordEditText.isFocusable = true
            passwordEditText.isFocusableInTouchMode = true
            setPasswordMask(false)
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val password = sharedPreferences.getString("USER_PASSWORD", "******")
            passwordEditText.setText(password)
            passwordEditText.requestFocus()
            Toast.makeText(this, getString(R.string.edit_password_message), Toast.LENGTH_SHORT).show()
        }

        savePasswordButton.setOnClickListener {
            val newPassword = passwordEditText.text.toString()
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("USER_EMAIL", "")
            val userType = sharedPreferences.getString("USER_TYPE", "")

            if (newPassword.length >= 5) {
                val passwordUpdated = RegistrationList.updateUserPassword(userEmail ?: "", newPassword, userType ?: "")

                if (passwordUpdated) {
                    val editor = sharedPreferences.edit()
                    editor.putString("USER_PASSWORD", newPassword)
                    editor.apply()

                    passwordEditText.isEnabled = false
                    passwordEditText.setText("*******")
                    setPasswordMask(true)

                    val updatedUser = RegistrationList.findUserByEmailAndType(userEmail ?: "", newPassword, userType ?: "")
                    if (updatedUser != null) {
                        Toast.makeText(this, getString(R.string.password_updated), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.error_updating_password), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error_updating_password), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.password_minimum_length), Toast.LENGTH_SHORT).show()
            }
        }

    }
}
