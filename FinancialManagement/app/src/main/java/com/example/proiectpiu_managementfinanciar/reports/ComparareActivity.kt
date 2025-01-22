package com.example.proiectpiu_managementfinanciar.reports

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.budget.MainBudgetActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.settings.SettingsStartActivity

class ComparareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compara)

        val pieChartView = findViewById<CustomPieChart>(R.id.comparisonPieChart)
        val selectedSliceInfo = findViewById<TextView>(R.id.selectedSliceInfo)
        val totalAmountText = findViewById<TextView>(R.id.totalAmountText)
        val monthSpinner = findViewById<Spinner>(R.id.monthSpinner)

        val dataForMonths = mapOf(
            "Noiembrie" to listOf(
                Triple(150f, Color.parseColor("#F59227"), "Alimente"),
                Triple(100f, Color.parseColor("#F8BC79"), "Facturi"),
                Triple(80f, Color.parseColor("#FCDB80"), "Rate"),
                Triple(70f, Color.parseColor("#D76A00"), "Haine"),
                Triple(60f, Color.parseColor("#FFC107"), "Combustibil"),
                Triple(50f, Color.parseColor("#F4AB6A"), "Consumabile")
            ),
            "Decembrie" to listOf(
                Triple(200f, Color.parseColor("#C35214"), "Alimente"),
                Triple(180f, Color.parseColor("#FBCEB1"), "Facturi"),
                Triple(150f, Color.parseColor("#FF9966"), "Rate"),
                Triple(120f, Color.parseColor("#FE6F5E"), "Haine"),
                Triple(100f, Color.parseColor("#E24C00"), "Combustibil"),
                Triple(80f, Color.parseColor("#FAB972"), "Consumabile")
            ),
            "Ianuarie" to listOf(
                Triple(300f, Color.parseColor("#FF7F50"), "Alimente"),
                Triple(250f, Color.parseColor("#E9967A"), "Facturi"),
                Triple(200f, Color.parseColor("#FFC3C3"), "Rate"),
                Triple(150f, Color.parseColor("#FFD8D8"), "Haine"),
                Triple(100f, Color.parseColor("#D76A00"), "Combustibil"),
                Triple(75f, Color.parseColor("#E24C00"), "Consumabile")
            )
        )

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedMonth = parent?.getItemAtPosition(position).toString()
                val dataForChart = dataForMonths[selectedMonth] ?: emptyList()

                pieChartView.setData(dataForChart)

                val totalAmount = dataForChart.sumOf { it.first.toDouble() }
                totalAmountText.text = "Total: %.0f lei".format(totalAmount)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        pieChartView.setOnSliceSelectedListener { _, slice ->
            val value = slice.first
            val color = slice.second
            val name = slice.third

            selectedSliceInfo.text = String.format(
                "Categorie: %s\nSuma: %.0f lei",
                name,
                value
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
