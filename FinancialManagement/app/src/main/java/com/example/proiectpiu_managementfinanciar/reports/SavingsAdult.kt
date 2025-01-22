package com.example.proiectpiu_managementfinanciar.reports

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.budget.MainBudgetActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.settings.SettingsStartActivity
import com.example.proiectpiu_managementfinanciar.util.ObjectiveManager

class SavingsAdult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_adult)

        ObjectiveManager.init(this)

        val pieChartView = findViewById<CustomPieChart>(R.id.pieChartView)
        val selectedSliceInfo = findViewById<TextView>(R.id.selectedSliceInfo)
        val totalAmountText = findViewById<TextView>(R.id.totalAmountText)

        val objectives = ObjectiveManager.getObjectives()
        Log.d("SavingsAdult", "Objectives: $objectives")

        if (objectives.isEmpty()) {
            totalAmountText.text = "Nu există economii"
            return
        }


        val colors = listOf(
            Color.parseColor("#FF9966"),
            Color.parseColor("#FE6F5E"),
            Color.parseColor("#e24c00"),
            Color.parseColor("#E6812F"),
            Color.parseColor("#FAB972"),
            Color.parseColor("#FF7F50"),
            Color.parseColor("#e9967a"),
            Color.parseColor("#FFC3C3"),
            Color.parseColor("#FFD8D8")
        )


        val dataForChart = objectives.mapIndexed { index, objective ->
            Triple(
                objective.sumaCurenta.toFloat(),
                colors[index % colors.size],
                objective.denumire
            )
        }
        Log.d("SavingsAdult", "Data for chart: $dataForChart")

        pieChartView.setData(dataForChart)

        val totalAmount = objectives.sumOf { it.sumaCurenta }
        totalAmountText.text = "Total: $totalAmount lei"

        pieChartView.setOnSliceSelectedListener { _, slice ->
            val value = slice.first
            val color = slice.second
            val name = slice.third

            val totalSum = dataForChart.sumOf { it.first.toDouble() }
            selectedSliceInfo.text = String.format(
                "Categorie: %s\nSuma: %.0f lei\nProcent: %.1f%%",
                name,
                value,
                (value / totalSum) * 100
            )
            selectedSliceInfo.setTextColor(color)
        }
    }

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.homeButton -> startActivity(Intent(this, ParentDashboardActivity::class.java))
            R.id.budgetButton -> startActivity(Intent(this, MainBudgetActivity::class.java))
            R.id.goalsButton -> startActivity(Intent(this, ObjectiveStartPageActivityAdult::class.java))
            R.id.reportsButton -> startActivity(Intent(this, ReportsActivity::class.java))
            R.id.settingsButton -> startActivity(Intent(this, SettingsStartActivity::class.java))
            R.id.profile -> startActivity(Intent(this, MyAccountActivity::class.java))
        }
    }
}
