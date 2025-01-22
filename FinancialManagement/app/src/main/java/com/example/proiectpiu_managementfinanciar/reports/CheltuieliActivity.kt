package com.example.proiectpiu_managementfinanciar.reports

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.budget.MainBudgetActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.settings.SettingsStartActivity
import com.example.proiectpiu_managementfinanciar.util.BudgetManager

class CheltuieliActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheltuieli)

        val pieChartView = findViewById<CustomPieChart>(R.id.pieChartView)
        val budgets = BudgetManager.getAllBudgets()
        val colors = listOf(
            Color.parseColor("#F59227"), Color.parseColor("#F8BC79"),
            Color.parseColor("#FCDB80"), Color.parseColor("#D76A00"),
            Color.parseColor("#FFC107"), Color.parseColor("#FAB972"),
            Color.parseColor("#FF7F50"), Color.parseColor("#FE6F5E")
        )

        val dataForChart = budgets.mapIndexed { index, budget ->
            Triple(budget.amount.toFloat(), colors[index % colors.size], budget.name)
        }

        pieChartView.setData(dataForChart)

        val totalAmount = BudgetManager.getTotalAmount()
        findViewById<TextView>(R.id.totalAmountText).text = "Total: $totalAmount lei"


        pieChartView.setOnSliceSelectedListener { index, slice ->
            val percentage = slice.first
            val color = slice.second
            val name = slice.third

            val selectedSliceInfo = findViewById<TextView>(R.id.selectedSliceInfo)
            selectedSliceInfo.text = String.format(
                "Cheltuială: %s\nSuma: %.0f lei\nProcent: %.1f%%",
                name, percentage, (percentage / 100) * 100
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
