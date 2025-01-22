package com.example.proiectpiu_managementfinanciar.reports

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.budget.MainBudgetActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.settings.SettingsStartActivity

class ReportsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
    }

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.profile -> startActivity(Intent(this, MyAccountActivity::class.java))
            R.id.cheltuieliButton -> startActivity(Intent(this, CheltuieliActivity::class.java))
            R.id.economiiButton -> startActivity(Intent(this, SavingsAdult::class.java))
            R.id.comparareButton -> startActivity(Intent(this, ComparareActivity::class.java))
            R.id.homeButton -> startActivity(Intent(this, ParentDashboardActivity::class.java))
            R.id.budgetButton -> startActivity(Intent(this, MainBudgetActivity::class.java))
            R.id.goalsButton -> startActivity(Intent(this, ObjectiveStartPageActivityAdult::class.java))
            R.id.reportsButton -> startActivity(Intent(this, ReportsActivity::class.java))
            R.id.settingsButton -> startActivity(Intent(this, SettingsStartActivity::class.java))
            R.id.profile -> startActivity(Intent(this, MyAccountActivity::class.java))
        }
    }


}