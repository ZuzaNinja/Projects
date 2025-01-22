package com.example.proiectpiu_managementfinanciar.objective

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.adapters.IconSpinnerAdapter
import com.example.proiectpiu_managementfinanciar.budget.MainBudgetActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.models.Notification
import com.example.proiectpiu_managementfinanciar.models.Objective
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivity
import com.example.proiectpiu_managementfinanciar.util.NotificationManager
import com.example.proiectpiu_managementfinanciar.util.ObjectiveManager

class AddObjectiveActivityAdult : AppCompatActivity(), View.OnClickListener {

    private lateinit var denumireInput: EditText
    private lateinit var sumaInput: EditText
    private lateinit var iconitaSpinner: Spinner
    private lateinit var addObjectiveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var homeButton: View
    private lateinit var budgetButton: View
    private lateinit var goalsButton: View
    private lateinit var reportsButton: View
    private lateinit var settingsButton: View


    private lateinit var errorMessageSection: LinearLayout
    private lateinit var errorMessageText: TextView
    private lateinit var successMessageSection: LinearLayout
    private lateinit var successMessageText: TextView

    private lateinit var profile: View

    private lateinit var iconLabels: List<String>

    private val iconResources = listOf(
        R.drawable.object_icon,
        R.drawable.car_icon1,
        R.drawable.headphones_icon2,
        R.drawable.sneakers_icon3,
        R.drawable.smartphone_icon4,
        R.drawable.tablet_icon5,
        R.drawable.bicycle_icon6,
        R.drawable.ski_icon7,
        R.drawable.vacation_icon9,
        R.drawable.concert_icon10,
        R.drawable.camera_icon13,
        R.drawable.parfume_icon16,
        R.drawable.house_icon17,
        R.drawable.jacket_icon18,
        R.drawable.airpods_icon19,
        R.drawable.laptop_icon20
    )

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_add_activity)

        ObjectiveManager.init(this)

        initializeViews()

        setupSpinner()
        setListeners()
    }

    private fun initializeViews() {
        denumireInput = findViewById(R.id.denumire_input)
        sumaInput = findViewById(R.id.suma_input)
        iconitaSpinner = findViewById(R.id.iconita_spinner)
        addObjectiveButton = findViewById(R.id.addObjectiveButton)
        cancelButton = findViewById(R.id.cancelButton)

        homeButton = findViewById(R.id.homeButton)
        budgetButton = findViewById(R.id.budgetButton)
        goalsButton = findViewById(R.id.goalsButton)
        reportsButton = findViewById(R.id.reportsButton)
        settingsButton = findViewById(R.id.settingsButton)

        errorMessageSection = findViewById(R.id.errorMessageSection)
        errorMessageText = findViewById(R.id.errorMessageText)
        successMessageSection = findViewById(R.id.successMessageSection)
        successMessageText = findViewById(R.id.successMessageText)

        profile = findViewById(R.id.profile)

        iconLabels = listOf(
            getString(R.string.icon_select),
            getString(R.string.icon_car),
            getString(R.string.icon_headphones),
            getString(R.string.icon_sneakers),
            getString(R.string.icon_smartphone),
            getString(R.string.icon_tablet),
            getString(R.string.icon_bicycle),
            getString(R.string.icon_ski),
            getString(R.string.icon_vacation),
            getString(R.string.icon_concert),
            getString(R.string.icon_camera),
            getString(R.string.icon_perfume),
            getString(R.string.icon_house),
            getString(R.string.icon_jacket),
            getString(R.string.icon_airpods),
            getString(R.string.icon_laptop)
        )

    }

    private fun setupSpinner() {
        val adapter = IconSpinnerAdapter(this, iconResources, iconLabels)
        iconitaSpinner.adapter = adapter
    }


    private fun setListeners() {
        addObjectiveButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)

        homeButton.setOnClickListener(this)
        budgetButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        reportsButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)

        profile.setOnClickListener(this)

    }

    private fun afisareTemporaraMesaje(view: View, durata: Long) {
        view.visibility = View.VISIBLE
        handler.postDelayed({
            view.visibility = View.GONE
        }, durata)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.addObjectiveButton -> addObjective()
            R.id.cancelButton -> startActivity(Intent(this, ObjectiveStartPageActivityAdult::class.java))
            R.id.homeButton -> startActivity(Intent(this, ParentDashboardActivity::class.java))
            R.id.budgetButton -> startActivity(Intent(this, MainBudgetActivity::class.java))
            R.id.goalsButton -> startActivity(Intent(this, ObjectiveStartPageActivityAdult::class.java))
            R.id.reportsButton -> startActivity(Intent(this, ReportsActivity::class.java))
            R.id.settingsButton -> Toast.makeText(this, getString(R.string.settings_in_progress), Toast.LENGTH_SHORT).show()
            R.id.profile -> {
                startActivity(Intent(this, MyAccountActivity::class.java))
            }
        }
    }

private fun addObjective() {
    val denumire = denumireInput.text.toString().trim()
    val sumaTotala = sumaInput.text.toString().toDoubleOrNull()
    val iconitaIndex = iconitaSpinner.selectedItemPosition

    if (denumire.isNotEmpty() && sumaTotala != null && iconitaIndex in iconLabels.indices && iconitaIndex > 0) {
        val newObjective = Objective(denumire, 0.0, sumaTotala, iconResources[iconitaIndex])
        ObjectiveManager.addObjective(newObjective)

        NotificationManager.addNotification(
            Notification(
                R.drawable.notification_icon,
                getString(R.string.objective_created),
                getString(R.string.objective_created_success, denumire)
            )
        )

        resetFields()
        showSuccessMessage(getString(R.string.objective_added_success))
        startActivity(Intent(this, ViewObjectivesActivity::class.java))
    } else {
        showErrorMessage(getString(R.string.complete_all_fields))
    }
}


    private fun resetFields() {
        denumireInput.text.clear()
        sumaInput.text.clear()
        iconitaSpinner.setSelection(0)
    }

    private fun showErrorMessage(message: String) {
        errorMessageText.text = message
        errorMessageSection.visibility = View.VISIBLE
        successMessageSection.visibility = View.GONE
        afisareTemporaraMesaje(errorMessageSection, 3000)
    }

    private fun showSuccessMessage(message: String) {
        successMessageText.text = message
        successMessageSection.visibility = View.VISIBLE
        errorMessageSection.visibility = View.GONE
        afisareTemporaraMesaje(successMessageSection, 3000)
    }

}
