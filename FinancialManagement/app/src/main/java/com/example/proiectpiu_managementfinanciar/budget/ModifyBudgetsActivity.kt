package com.example.proiectpiu_managementfinanciar.budget

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivity
import com.example.proiectpiu_managementfinanciar.settings.SettingsStartActivity

class ModifyBudgetsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var profile: View
    private lateinit var homeButton: View
    private lateinit var budgetButton: View
    private lateinit var goalsButton: View
    private lateinit var reportsButton: View
    private lateinit var saveButton: Button
    private lateinit var settingsButton: View
    private lateinit var amountTextEdit: EditText
    private lateinit var messageSection: View
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.budget_modify_section)

        val addBudgetsButton : ImageButton = findViewById(R.id.add_section_button)
        addBudgetsButton.setOnClickListener {
            val intent = Intent(this, NewBudgetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val editBudgetsButton : ImageButton = findViewById(R.id.modify_section_button)
        editBudgetsButton.setOnClickListener {
            val intent = Intent(this, ModifyBudgetsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val emergencyBudgetButton : ImageButton = findViewById(R.id.emergency_fund_button)
        emergencyBudgetButton.setOnClickListener {
            val intent = Intent(this, EmergencyBudgetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        amountTextEdit = findViewById(R.id.sectionAmount)
        saveButton = findViewById(R.id.save_button)
        messageSection = findViewById(R.id.succes_section)
        cancelButton = findViewById(R.id.cancel_button)

        cancelButton.setOnClickListener{
            amountTextEdit.text.clear()
        }

        saveButton.setOnClickListener {
            if (amountTextEdit.getText().toString().isEmpty()){
                messageSection.visibility = View.VISIBLE

                val importantIcon : ImageView = findViewById(R.id.success_message_icon)
                importantIcon.visibility = View.VISIBLE

                val whatEverText : TextView = findViewById(R.id.success_message)
                whatEverText.visibility = View.VISIBLE

                Handler().postDelayed({
                    messageSection.visibility = View.GONE
                    importantIcon.visibility = View.GONE
                    whatEverText.visibility = View.GONE
                }, 2000)
            }
            else{
                messageSection.visibility = View.VISIBLE

                val bell : ImageView = findViewById(R.id.success_message_icon2)
                bell.visibility = View.VISIBLE

                val whatEverText2 : TextView = findViewById(R.id.success_message2)
                whatEverText2.visibility = View.VISIBLE
                amountTextEdit.text.clear()

                Handler().postDelayed({
                    messageSection.visibility = View.GONE
                    bell.visibility = View.GONE
                    whatEverText2.visibility = View.GONE
                }, 2000)
            }
        }

        profile = findViewById(R.id.profile)
        homeButton = findViewById(R.id.homeButton)
        budgetButton = findViewById(R.id.budgetButton)
        goalsButton = findViewById(R.id.goalsButton)
        reportsButton = findViewById(R.id.reportsButton)
        settingsButton = findViewById(R.id.settingsButton)

        profile.setOnClickListener(this)
        homeButton.setOnClickListener(this)
        budgetButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        reportsButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.adolescentButton -> {
                //todo
                startActivity(intent)
            }
            R.id.homeButton -> {
                val intent = Intent(this, ParentDashboardActivity::class.java)
                startActivity(intent)
            }
            R.id.budgetButton -> {
                val intent = Intent(this, MainBudgetActivity::class.java)
                startActivity(intent)
            }
            R.id.goalsButton -> {
                val intent = Intent(this, ObjectiveStartPageActivityAdult::class.java)
                startActivity(intent)
            }
            R.id.reportsButton -> {
                val intent = Intent(this, ReportsActivity::class.java)
                startActivity(intent)
            }
            R.id.settingsButton -> {
                startActivity(Intent(this, SettingsStartActivity::class.java))
            }

            R.id.profile -> {
                startActivity(Intent(this, MyAccountActivity::class.java))
            }
        }
    }
}