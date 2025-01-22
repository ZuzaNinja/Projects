package com.example.proiectpiu_managementfinanciar.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.adapters.NotificationAdapterAdolescent
import com.example.proiectpiu_managementfinanciar.home_dashboard.AdolescentDashboardActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdolescent
import com.example.proiectpiu_managementfinanciar.util.NotificationManagerAdolescent
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivityTeen

class NotificationActivityAdolescent : AppCompatActivity(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapterAdolescent

    private lateinit var homeButton: View
    private lateinit var pusculitaButton: View
    private lateinit var goalsButton: View
    private lateinit var learnButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notificari_activity_adolescent)

        recyclerView = findViewById(R.id.notificari_recyclerView_1)
        adapter = NotificationAdapterAdolescent(this, NotificationManagerAdolescent.getNotifications().toMutableList()) { notification ->
            Toast.makeText(this, getString(R.string.read_notification, notification.title), Toast.LENGTH_SHORT).show()
            val index = NotificationManagerAdolescent.getNotifications().indexOf(notification)
            NotificationManagerAdolescent.markNotificationAsRead(index)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        NotificationManagerAdolescent.onNotificationUpdated = {
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }

        initializeViews()

        setListeners()
    }

    private fun initializeViews() {
        homeButton = findViewById(R.id.homeButton)
        pusculitaButton = findViewById(R.id.pusculitaButton)
        goalsButton = findViewById(R.id.goalsButton)
        learnButton = findViewById(R.id.learnButton)
    }


    override fun onClick(view: View?) {
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
                Toast.makeText(this, getString(R.string.learn_in_progress), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, getString(R.string.unknown_action), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setListeners() {
        homeButton.setOnClickListener(this)
        pusculitaButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        learnButton.setOnClickListener(this)
    }

}
