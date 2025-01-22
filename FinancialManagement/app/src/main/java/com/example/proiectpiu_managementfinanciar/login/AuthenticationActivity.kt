package com.example.proiectpiu_managementfinanciar.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.home_dashboard.AdolescentDashboardActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.util.RegistrationList

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var authenticateButton: Button
    private lateinit var googleIcon: ImageView
    private lateinit var registerLink: TextView
    private lateinit var forgotPassword: TextView

    private lateinit var textViewLoginFailed: TextView
    private lateinit var textViewSignInMessage: TextView

    private val handler = Handler(Looper.getMainLooper())

    private val parentEmail = "parent@gmail.com"
    private val parentPassword = "12345"

    private val adolescentEmail = "adolescent@gmail.com"
    private val adolescentPassword = "12345"

    private var selectedUserType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        initializeViews()
        setListeners()

        val email = intent.getStringExtra("EXTRA_EMAIL")
        val password = intent.getStringExtra("EXTRA_PASSWORD")

        emailInput.setText(email)
        passwordInput.setText(password)

        selectedUserType = intent.getStringExtra("USER_TYPE") ?: run {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            sharedPreferences.getString("USER_TYPE", "")
        }

    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        authenticateButton = findViewById(R.id.authenticate_button)
        googleIcon = findViewById(R.id.google_icon)
        registerLink = findViewById(R.id.register_link)
        forgotPassword = findViewById(R.id.forgot_password)

        textViewLoginFailed = findViewById(R.id.textViewLoginFailed)
        textViewSignInMessage = findViewById(R.id.textViewSignInMessage)
    }

    private fun setListeners() {
        authenticateButton.setOnClickListener {
            val validInputs = validateInputs()
            textViewLoginFailed.visibility = View.INVISIBLE
            textViewSignInMessage.visibility = View.INVISIBLE

            if (validInputs) {
                val enteredEmail = emailInput.text.toString()
                val enteredPassword = passwordInput.text.toString()

                selectedUserType = intent.getStringExtra("USER_TYPE") ?: ""

                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                if (enteredEmail == parentEmail && enteredPassword == parentPassword) {
                    if(selectedUserType == "Parent"){
                        editor.putString("USER_EMAIL", enteredEmail)
                        editor.putString("USER_TYPE", "Parent")

                        editor.putString("USER_PHONE", "0712345678")
                        editor.putString("USER_PASSWORD", enteredPassword)

                        editor.apply()
                        afisareTemporaraMesaje(textViewSignInMessage, 3000)
                        handler.postDelayed({
                            startActivity(Intent(this, ParentDashboardActivity::class.java))
                        }, 1000)
                    }
                    else{
                        afisareTemporaraMesaje(textViewLoginFailed, 3000)
                    }
                } else if (enteredEmail == adolescentEmail && enteredPassword == adolescentPassword) {
                    if(selectedUserType == "Adolescent"){
                        editor.putString("USER_EMAIL", enteredEmail)
                        editor.putString("USER_TYPE", "Adolescent")

                        editor.putString("USER_PHONE", "0723456789")
                        editor.putString("USER_PASSWORD", enteredPassword)

                        editor.apply()

                        afisareTemporaraMesaje(textViewSignInMessage, 3000)
                        handler.postDelayed({
                            startActivity(Intent(this, AdolescentDashboardActivity::class.java))
                        }, 1000)
                    }
                    else{
                        afisareTemporaraMesaje(textViewLoginFailed, 3000)
                    }
                } else {
                    val registeredUser = RegistrationList.findUserByEmailAndType(
                        enteredEmail.trim(),
                        enteredPassword.trim(),
                        selectedUserType?.trim() ?: ""
                    )

                    if (registeredUser != null) {
                        val isParent = registeredUser.userType.trim().equals(getString(R.string.role_parent).trim())
                        val isAdolescent = registeredUser.userType.trim() == getString(R.string.role_adolescent).trim()

                        if (isParent) {
                            editor.putString("USER_EMAIL", registeredUser.email)
                            editor.putString("USER_TYPE", getString(R.string.role_parent).trim())
                            editor.apply()
                            startActivity(Intent(this, ParentDashboardActivity::class.java))
                        } else if (isAdolescent) {
                            editor.putString("USER_EMAIL", registeredUser.email)
                            editor.putString("USER_TYPE", getString(R.string.role_adolescent).trim())
                            editor.apply()
                            startActivity(Intent(this, AdolescentDashboardActivity::class.java))
                        } else {
                            afisareTemporaraMesaje(textViewLoginFailed, 3000)
                        }
                    } else {
                        afisareTemporaraMesaje(textViewLoginFailed, 3000)
                    }

                }
            }
        }

        googleIcon.setOnClickListener {
            Toast.makeText(this, getString(R.string.redirect_to_google), Toast.LENGTH_SHORT).show()
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotPassword.setOnClickListener {
            Toast.makeText(this, getString(R.string.check), Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.verify_all), Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.contains("@") || (!email.endsWith("gmail.com") && !email.endsWith("yahoo.com") && !email.endsWith("gmail.ro") && !email.endsWith("yahoo.ro"))) {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 5) {
            Toast.makeText(this, getString(R.string.password_minimum_length), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun afisareTemporaraMesaje(textView: TextView, durata: Long) {
        textView.visibility = View.VISIBLE
        handler.postDelayed({
            textView.visibility = View.INVISIBLE
        }, durata)
    }
}
