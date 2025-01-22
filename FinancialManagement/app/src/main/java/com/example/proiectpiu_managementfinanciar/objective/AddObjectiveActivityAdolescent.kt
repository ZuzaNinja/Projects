package com.example.proiectpiu_managementfinanciar.objective

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.adapters.IconSpinnerAdapter
import com.example.proiectpiu_managementfinanciar.home_dashboard.AdolescentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.models.Notification
import com.example.proiectpiu_managementfinanciar.models.Objective
import com.example.proiectpiu_managementfinanciar.settings.NotificationActivityAdolescent
import com.example.proiectpiu_managementfinanciar.util.NotificationManagerAdolescent
import com.example.proiectpiu_managementfinanciar.util.ObjectiveManagerAdolescent
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivityTeen

class AddObjectiveActivityAdolescent : AppCompatActivity(), View.OnClickListener {

    private lateinit var denumireInput: EditText
    private lateinit var sumaInput: EditText
    private lateinit var iconitaSpinner: Spinner
    private lateinit var addObjectiveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var homeButton: View
    private lateinit var pusculitaButton: View
    private lateinit var goalsButton: View
    private lateinit var learnButton: View

    private lateinit var profileSection: View

    private lateinit var errorMessageSection: LinearLayout
    private lateinit var errorMessageText: TextView
    private lateinit var successMessageSection: LinearLayout
    private lateinit var successMessageText: TextView

    private lateinit var notificationIcon: ImageView

    private lateinit var iconLabels: List<String>

    private val iconResources = listOf(
        R.drawable.object_icon,
        R.drawable.headphones_icon2,
        R.drawable.sneakers_icon3,
        R.drawable.smartphone_icon4,
        R.drawable.tablet_icon5,
        R.drawable.bicycle_icon6,
        R.drawable.ski_icon7,
        R.drawable.roller_skate_icon8,
        R.drawable.concert_icon10,
        R.drawable.candy_icon11,
        R.drawable.controller_icon12,
        R.drawable.camera_icon13,
        R.drawable.skateboard_icon14,
        R.drawable.books_icon15,
        R.drawable.parfume_icon16,
        R.drawable.jacket_icon18,
        R.drawable.airpods_icon19,
        R.drawable.laptop_icon20
    )

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_adolescent_add_activity)

        ObjectiveManagerAdolescent.initialize(this)

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
        pusculitaButton = findViewById(R.id.pusculitaButton)
        goalsButton = findViewById(R.id.goalsButton)
        learnButton = findViewById(R.id.learnButton)

        profileSection = findViewById(R.id.profile_section)

        errorMessageSection = findViewById(R.id.errorMessageSection)
        errorMessageText = findViewById(R.id.errorMessageText)
        successMessageSection = findViewById(R.id.successMessageSection)
        successMessageText = findViewById(R.id.successMessageText)

        notificationIcon = findViewById(R.id.notification_icon)

        iconLabels = listOf(
            getString(R.string.select_icon),
            getString(R.string.headphones),
            getString(R.string.sneakers),
            getString(R.string.smartphone),
            getString(R.string.tablet),
            getString(R.string.bicycle),
            getString(R.string.ski),
            getString(R.string.roller_skate),
            getString(R.string.concert),
            getString(R.string.candy),
            getString(R.string.controller),
            getString(R.string.camera),
            getString(R.string.skateboard),
            getString(R.string.books),
            getString(R.string.perfume),
            getString(R.string.jacket),
            getString(R.string.airpods),
            getString(R.string.laptop)
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
        pusculitaButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        learnButton.setOnClickListener(this)

        profileSection.setOnClickListener(this)

        notificationIcon.setOnClickListener {
            startActivity(Intent(this, NotificationActivityAdolescent::class.java))
        }
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
            R.id.cancelButton -> startActivity(Intent(this, ObjectiveStartPageActivityAdolescent::class.java))
            R.id.homeButton -> {
                startActivity(Intent(this, AdolescentDashboardActivity::class.java))
            }
            R.id.pusculitaButton -> {
                startActivity(Intent(this, ReportsActivityTeen::class.java))
            }
            R.id.goalsButton -> {
                startActivity(Intent(this, ObjectiveStartPageActivityAdolescent::class.java))
            }
            R.id.learnButton -> {
                Toast.makeText(this, getString(R.string.learning_not_ready), Toast.LENGTH_SHORT).show()
            }
            R.id.profile_section -> {
                startActivity(Intent(this, MyAccountActivity::class.java))
            }
            else -> {
                Toast.makeText(this, getString(R.string.unknown_action), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addObjective() {
        val denumire = denumireInput.text.toString().trim()
        val sumaTotala = sumaInput.text.toString().toDoubleOrNull()
        val iconitaIndex = iconitaSpinner.selectedItemPosition

        if (denumire.isNotEmpty() && sumaTotala != null && iconitaIndex in iconLabels.indices && iconitaIndex > 0) {
            val newObjective = Objective(denumire, 0.0, sumaTotala, iconResources[iconitaIndex])
            ObjectiveManagerAdolescent.addObjective(newObjective)

            NotificationManagerAdolescent.addNotification(
                Notification(
                    R.drawable.notification_icon,
                    getString(R.string.objective_created),
                    getString(R.string.objective_created_message, denumire)
                )
            )


            resetFields()
            showSuccessMessage(getString(R.string.objective_added_success))
            startActivity(Intent(this, ObjectiveStartPageActivityAdolescent::class.java))
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
