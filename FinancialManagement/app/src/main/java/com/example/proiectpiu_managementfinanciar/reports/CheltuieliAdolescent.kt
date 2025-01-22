package com.example.proiectpiu_managementfinanciar.reports

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.home_dashboard.AdolescentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.models.BudgetItem
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdolescent

class CheltuieliAdolescent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cheltuieli_adolescent)

        val pieChartViewAdolescent = findViewById<CustomPieChart>(R.id.pieChartViewAdolescent)
        val selectedSliceInfoAdolescent = findViewById<TextView>(R.id.selectedSliceInfoAdolescent)
        val totalAmountText = findViewById<TextView>(R.id.totalAmountText)

        val adolescentBudgets = listOf(
            BudgetItem("Mâncare", 150),
            BudgetItem("Haine", 100),
            BudgetItem("Jocuri", 50),
            BudgetItem("Transport", 70)
        )

        val colorsAdolescent = listOf(
            Color.parseColor("#F59227"),
            Color.parseColor("#F8BC79"),
            Color.parseColor("#FCDB80"),
            Color.parseColor("#D76A00")
        )

        val dataForChartAdolescent = adolescentBudgets.mapIndexed { index, budget ->
            Triple(budget.amount.toFloat(), colorsAdolescent[index % colorsAdolescent.size], budget.name)
        }

        pieChartViewAdolescent.setData(dataForChartAdolescent)

        val totalAmount = adolescentBudgets.sumOf { it.amount }
        totalAmountText.text = "Total: $totalAmount lei"

        pieChartViewAdolescent.setOnSliceSelectedListener { _, slice ->
            val value = slice.first
            val color = slice.second
            val name = slice.third

            val totalSum = dataForChartAdolescent.sumOf { it.first.toDouble() }

            selectedSliceInfoAdolescent.text = String.format(
                "Categorie: %s\nSuma: %.0f lei\nProcent: %.1f%%",
                name,
                value,
                (value / totalSum) * 100
            )
            selectedSliceInfoAdolescent.setTextColor(color)
        }
    }

    fun onClick(view: View?) {
        when (view?.id) {
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
                Toast.makeText(this, getString(R.string.feature_in_development), Toast.LENGTH_SHORT).show()
            }
            R.id.profile_section -> {
                startActivity(Intent(this, MyAccountActivity::class.java))
            }
        }
    }
}
