package com.example.proiectpiu_managementfinanciar.budget

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.adapters.BudgetListAdapter
import com.example.proiectpiu_managementfinanciar.home_dashboard.ParentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.models.BudgetItem
import com.example.proiectpiu_managementfinanciar.objective.ObjectiveStartPageActivityAdult
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivity
import com.example.proiectpiu_managementfinanciar.settings.SettingsStartActivity
import com.example.proiectpiu_managementfinanciar.util.BudgetManager

class NewBudgetActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var profile: View
    private lateinit var homeButton: View
    private lateinit var budgetButton: View
    private lateinit var goalsButton: View
    private lateinit var reportsButton: View
    private lateinit var settingsButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.budget_add_section)

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

        val recyclerView: RecyclerView = findViewById(R.id.budgets_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val budgetItems = listOf(
            BudgetItem(getString(R.string.budget_groceries), 300),
            BudgetItem(getString(R.string.budget_rent), 1200),
            BudgetItem(getString(R.string.budget_utilities), 150)
        )

        val adapter = BudgetListAdapter(budgetItems.toMutableList())
        recyclerView.adapter = adapter


        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val sectionName = findViewById<EditText>(R.id.sectionName).text.toString()
            val sectionAmount = findViewById<EditText>(R.id.sectionAmount).text.toString()

            if (sectionName.isNotEmpty() && sectionAmount.isNotEmpty()) {
                val newBudget = BudgetItem(sectionName, sectionAmount.toInt())
                BudgetManager.addTemporaryBudget(newBudget)

                val intent = Intent(this, MainBudgetActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show()
            }
        }


        setKeyboardVisibilityListener()

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

    private fun setKeyboardVisibilityListener() {
        val rootView = findViewById<View>(android.R.id.content)
        val footerMenu = findViewById<View>(R.id.footer_menu)
        val navigationMenu = findViewById<View>(R.id.navigationMenu)

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                footerMenu.visibility = View.GONE
                navigationMenu.visibility = View.GONE
            } else {
                footerMenu.visibility = View.VISIBLE
                navigationMenu.visibility = View.VISIBLE
            }
        }
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
