package com.example.proiectpiu_managementfinanciar.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.adapters.NotificationAdapter
import com.example.proiectpiu_managementfinanciar.budget.MainBudgetActivity
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivity
import com.example.proiectpiu_managementfinanciar.util.NotificationManager

class NotificationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter

    private lateinit var homeButton: View
    private lateinit var budgetButton: View
    private lateinit var goalsButton: View
    private lateinit var reportsButton: View
    private lateinit var settingsButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notificari_activity)

        initializeViews()
        setListeners()

        recyclerView = findViewById(R.id.notificari_recyclerView)
        adapter = NotificationAdapter(this, NotificationManager.getNotifications().toMutableList()) { notification ->
            Toast.makeText(this, getString(R.string.read_notification, notification.title), Toast.LENGTH_SHORT).show()
            val index = NotificationManager.getNotifications().indexOf(notification)
            NotificationManager.markNotificationAsRead(index)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        NotificationManager.onNotificationUpdated = {
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initializeViews() {
        homeButton = findViewById(R.id.homeButton)
        budgetButton = findViewById(R.id.budgetButton)
        goalsButton = findViewById(R.id.goalsButton)
        reportsButton = findViewById(R.id.reportsButton)
        settingsButton = findViewById(R.id.settingsButton)
    }

    private fun setListeners() {
        homeButton.setOnClickListener(this)
        budgetButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        reportsButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.homeButton -> startActivity(Intent(this, ParentDashboardActivity::class.java))
            R.id.budgetButton -> startActivity(Intent(this, MainBudgetActivity::class.java))
            R.id.goalsButton -> startActivity(Intent(this, ObjectiveStartPageActivityAdult::class.java))
            R.id.reportsButton -> startActivity(Intent(this, ReportsActivity::class.java))
            R.id.settingsButton -> startActivity(Intent(this, SettingsStartActivity::class.java))
            else -> Toast.makeText(this, getString(R.string.unknown_action), Toast.LENGTH_SHORT).show()
        }
    }
}
