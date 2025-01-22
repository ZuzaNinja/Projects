package com.example.proiectpiu_managementfinanciar.settings

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.budget.MainBudgetActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivity
import java.util.*

class LanguageSelectionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var languageSpinner: Spinner
    private lateinit var confirmButton: Button
    private lateinit var errorMessageTextView: TextView

    private lateinit var homeButton: View
    private lateinit var budgetButton: View
    private lateinit var goalsButton: View
    private lateinit var reportsButton: View
    private lateinit var settingsButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection_settings)

        initializeViews()
        setListeners()

        languageSpinner = findViewById(R.id.language_spinner)
        confirmButton = findViewById(R.id.confirm_button)
        errorMessageTextView = findViewById(R.id.errorMessageTextView)

        confirmButton.setOnClickListener {
            val selectedPosition = languageSpinner.selectedItemPosition
            val languages = resources.getStringArray(R.array.language_options)

            if (selectedPosition == 0) {
                errorMessageTextView.visibility = TextView.VISIBLE
            } else {
                val selectedLanguage = languageSpinner.selectedItem.toString()
                errorMessageTextView.visibility = TextView.GONE
                when (selectedLanguage) {
                    languages[1] -> setLocale("ro")
                    languages[2] -> setLocale("en")
                    languages[3] -> setLocale("es")
                    else -> Toast.makeText(this, getString(R.string.select_language_warning), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        Toast.makeText(this, getString(R.string.language_updated), Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SettingsStartActivity::class.java))
        finish()
    }

    private fun initializeViews() {
        homeButton = findViewById(R.id.homeButton)
        budgetButton = findViewById(R.id.budgetButton)
        goalsButton = findViewById(R.id.goalsButton)
        reportsButton = findViewById(R.id.reportsButton)
        settingsButton = findViewById(R.id.settingsButton)
    }

    private fun setListeners() {
        homeButton.setOnClickListener(this)
        budgetButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        reportsButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.homeButton -> startActivity(Intent(this, ParentDashboardActivity::class.java))
            R.id.budgetButton -> startActivity(Intent(this, MainBudgetActivity::class.java))
            R.id.goalsButton -> startActivity(Intent(this, ObjectiveStartPageActivityAdult::class.java))
            R.id.reportsButton -> startActivity(Intent(this, ReportsActivity::class.java))
            R.id.settingsButton -> startActivity(Intent(this, SettingsStartActivity::class.java))
            else -> Toast.makeText(this, getString(R.string.unknown_action), Toast.LENGTH_SHORT).show()
        }
    }
}
