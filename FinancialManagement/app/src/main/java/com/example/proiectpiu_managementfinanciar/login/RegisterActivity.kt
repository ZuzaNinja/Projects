package com.example.proiectpiu_managementfinanciar.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.models.UserRegistration
import com.example.proiectpiu_managementfinanciar.util.RegistrationList


class RegisterActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var googleIcon: ImageView
    private lateinit var roleSelectionGroup: RadioGroup
    private lateinit var radioAdolescent: RadioButton
    private lateinit var radioParent: RadioButton

    private lateinit var textViewSuccess: TextView
    private lateinit var textViewFail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initializeViews()

        val selectedRole = intent.getStringExtra("USER_TYPE")
        when (selectedRole) {
            "Adolescent" -> radioAdolescent.isChecked = true
            "Parent" -> radioParent.isChecked = true
        }

        setListeners()
    }


    private fun initializeViews() {
        nameInput = findViewById(R.id.name_input)
        emailInput = findViewById(R.id.email_input)
        phoneInput = findViewById(R.id.phone_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
        registerButton = findViewById(R.id.register_button)
        googleIcon = findViewById(R.id.google_icon)

        roleSelectionGroup = findViewById(R.id.role_selection_group)
        radioAdolescent = findViewById(R.id.radio_adolescent)
        radioParent = findViewById(R.id.radio_parent)

        textViewSuccess = findViewById(R.id.textViewSignInMessage)
        textViewFail = findViewById(R.id.textViewLoginFailed)
    }

    private fun setListeners() {
        registerButton.setOnClickListener {
            val selectedRole = when {
                radioAdolescent.isChecked -> getString(R.string.role_adolescent)
                radioParent.isChecked -> getString(R.string.role_parent)
                else -> null
            }

            if (selectedRole == null) {
                Toast.makeText(this, getString(R.string.select_account_type), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (validateInputs()) {
                val name = nameInput.text.toString()
                val email = emailInput.text.toString()
                val phone = phoneInput.text.toString()
                val password = passwordInput.text.toString()

                if (RegistrationList.registrations.any { it.email == email && it.userType == selectedRole }) {
                    showMessage(textViewFail, "Acest cont există deja!")
                    return@setOnClickListener
                }


                val newUser = UserRegistration(name, email, phone, password, selectedRole)
                RegistrationList.addUser(newUser)

                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("USER_NAME", name)
                editor.putString("USER_EMAIL", email)
                editor.putString("USER_PHONE", phone)
                editor.putString("USER_PASSWORD", password)
                editor.putString("USER_TYPE", selectedRole.trim())
                editor.apply()


                Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, AuthenticationActivity::class.java)
                intent.putExtra("EXTRA_NAME", name)
                intent.putExtra("EXTRA_EMAIL", email)
                intent.putExtra("EXTRA_PHONE", phone)
                intent.putExtra("EXTRA_PASSWORD", password)
                intent.putExtra("USER_TYPE", selectedRole)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.registration_failed), Toast.LENGTH_SHORT).show()
            }
    }

        googleIcon.setOnClickListener {
            Toast.makeText(this, getString(R.string.redirect_to_google), Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        val phone = phoneInput.text.toString()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()

        if (name.isEmpty()) {
            showMessage(textViewFail, getString(R.string.name_required))
            return false
        }
        if (!name[0].isUpperCase()) {
            showMessage(textViewFail, getString(R.string.name_capital_required))
            return false
        }

        if (email.isEmpty()) {
            showMessage(textViewFail, getString(R.string.email_required))
            return false
        }
        if (!email.contains("@") || (!email.endsWith("gmail.com") && !email.endsWith("yahoo.com") && !email.endsWith("gmail.ro") && !email.endsWith("yahoo.ro"))) {
            showMessage(textViewFail, getString(R.string.invalid_email))
            return false
        }

        if (phone.isEmpty()) {
            showMessage(textViewFail, getString(R.string.phone_required))
            return false
        }
        if (phone.length != 10) {
            showMessage(textViewFail, getString(R.string.phone_invalid))
            return false
        }

        if (password.isEmpty()) {
            showMessage(textViewFail, getString(R.string.password_required))
            return false
        }
        if (password.length < 5) {
            showMessage(textViewFail, getString(R.string.password_minimum_length))
            return false
        }

        if (confirmPassword.isEmpty()) {
            showMessage(textViewFail, getString(R.string.confirm_password_required))
            return false
        }
        if (password != confirmPassword) {
            showMessage(textViewFail, getString(R.string.passwords_do_not_match))
            return false
        }

        val selectedRole = when {
            radioAdolescent.isChecked -> getString(R.string.role_adolescent)
            radioParent.isChecked -> getString(R.string.role_parent)
            else -> null
        }

        if (selectedRole == null) {
            showMessage(textViewFail, getString(R.string.select_account_type))
            return false
        }

        return true
    }

    private fun showMessage(textView: TextView, message: String) {
        textView.text = message
        textView.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            textView.visibility = View.INVISIBLE
        }, 3000)
    }
}