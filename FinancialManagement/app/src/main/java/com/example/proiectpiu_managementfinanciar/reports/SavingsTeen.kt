package com.example.proiectpiu_managementfinanciar.reports

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.home_dashboard.AdolescentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdolescent
import com.example.proiectpiu_managementfinanciar.util.ObjectiveManagerAdolescent

class SavingsTeen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_teen)

        ObjectiveManagerAdolescent.initialize(this)

        val pieChartViewAdolescent = findViewById<CustomPieChart>(R.id.pieChartViewAdolescent)
        val selectedSliceInfoAdolescent = findViewById<TextView>(R.id.selectedSliceInfoAdolescent)
        val totalAmountText = findViewById<TextView>(R.id.totalAmountText)

        val objectives = ObjectiveManagerAdolescent.getObjectives()
        if (objectives.isEmpty()) {
            Log.e("SavingsTeen", "Lista de obiective este goală.")
            totalAmountText.text = "Total economii: 0 lei"
            return
        }

        objectives.map { objective ->
            Triple(
                objective.sumaCurenta.toFloat(),
                objective.denumire,
                objective.sumaTotala
            )
        }

        val colorsAdolescent = listOf(
            Color.parseColor("#C35214"),
            Color.parseColor("#FBCEB1"),
            Color.parseColor("#FF9966"),
            Color.parseColor("#FE6F5E"),
            Color.parseColor("#E24C00")
        )

        val dataForChartAdolescent = objectives.mapIndexed { index, objective ->
            Triple(
                objective.sumaCurenta.toFloat(),
                colorsAdolescent[index % colorsAdolescent.size],
                objective.denumire
            )
        }

        pieChartViewAdolescent.setData(dataForChartAdolescent)

        val totalSavings = objectives.sumOf { it.sumaCurenta }
        totalAmountText.text = "Total economii: %.0f lei".format(totalSavings)

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
