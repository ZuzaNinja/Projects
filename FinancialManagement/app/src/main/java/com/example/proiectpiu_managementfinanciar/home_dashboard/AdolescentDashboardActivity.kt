package com.example.proiectpiu_managementfinanciar.home_dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdolescent
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivityTeen
import com.example.proiectpiu_managementfinanciar.settings.NotificationActivityAdolescent

class AdolescentDashboardActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var notificationIcon: ImageView

    private lateinit var profileSection: View
    private lateinit var homeButton: View
    private lateinit var pusculitaButton: View
    private lateinit var goalsButton: View
    private lateinit var learnButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adolescent_dashboard)

        initializeViews()

    }

    private fun initializeViews() {
        profileSection = findViewById(R.id.profile_section)
        homeButton = findViewById(R.id.homeButton)
        pusculitaButton = findViewById(R.id.pusculitaButton)
        goalsButton = findViewById(R.id.goalsButton)
        learnButton = findViewById(R.id.learnButton)
        notificationIcon = findViewById(R.id.notification_icon)

        profileSection.setOnClickListener(this)
        homeButton.setOnClickListener(this)
        pusculitaButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        learnButton.setOnClickListener(this)
        notificationIcon.setOnClickListener {
            startActivity(Intent(this, NotificationActivityAdolescent::class.java))
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.homeButton -> {
                Toast.makeText(this, getString(R.string.already_on_homepage), Toast.LENGTH_SHORT).show()
            }
            R.id.pusculitaButton -> {
                startActivity(Intent(this, ReportsActivityTeen::class.java))
            }
            R.id.goalsButton -> {
                startActivity(Intent(this, ObjectiveStartPageActivityAdolescent::class.java))
            }
            R.id.learnButton -> {
                Toast.makeText(this, getString(R.string.feature_in_development), Toast.LENGTH_SHORT).show()
            }
            R.id.profile_section -> {
                startActivity(Intent(this, MyAccountActivity::class.java))
            }
        }
    }
}
