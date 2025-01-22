package com.example.proiectpiu_managementfinanciar.reports

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.home_dashboard.AdolescentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdolescent

class ReportsActivityTeen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pusculita_adolescent)
    }

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.bani_economisiti_section -> {
                val intent = Intent(this, SavingsTeen::class.java)
                startActivity(intent)
            }
            R.id.cheltuieli_section -> {
                val intent = Intent(this, CheltuieliAdolescent::class.java)
               startActivity(intent)
           }
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
}