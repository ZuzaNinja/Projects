package com.example.proiectpiu_managementfinanciar.objective

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.adapters.ObjectiveAdapterAdolescent
import com.example.proiectpiu_managementfinanciar.util.ObjectiveManagerAdolescent
import com.example.proiectpiu_managementfinanciar.home_dashboard.AdolescentDashboardActivity
import com.example.proiectpiu_managementfinanciar.login.MyAccountActivity
import com.example.proiectpiu_managementfinanciar.settings.NotificationActivityAdolescent
import com.example.proiectpiu_managementfinanciar.reports.ReportsActivityTeen
class ObjectiveStartPageActivityAdolescent : AppCompatActivity(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ObjectiveAdapterAdolescent

    private lateinit var sumaText: TextView
    private lateinit var catPhoto: ImageView
    private lateinit var titleText: TextView
    private lateinit var addObjectiveButton: ImageButton

    private lateinit var notificationIcon: ImageView

    private lateinit var homeButton: View
    private lateinit var pusculitaButton: View
    private lateinit var goalsButton: View
    private lateinit var learnButton: View

    private lateinit var profileSection: View

    private lateinit var sumaInput: EditText
    private lateinit var adaugaSumaButton: Button
    private lateinit var anuleazaSumaButton: Button

    private lateinit var errorMessage1: LinearLayout
    private lateinit var errorMessageText1: TextView
    private lateinit var errorMessage2: LinearLayout
    private lateinit var errorMessageText2: TextView
    private lateinit var successMessage: LinearLayout
    private lateinit var successMessageText: TextView

    private lateinit var manageObjectiveSection: LinearLayout
    private lateinit var manageObjectiveElements: LinearLayout
    private lateinit var manageObjectiveTitle: TextView
    private lateinit var manageObjectiveButton: ImageButton


    private val handler = android.os.Handler()

    private fun afisareTemporaraMesaje(view: View, durata: Long) {
        view.visibility = View.VISIBLE
        handler.postDelayed({
            view.visibility = View.GONE
        }, durata)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_activity_adolescent_start_page)

        recyclerView = findViewById(R.id.objectivesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ObjectiveAdapterAdolescent(ObjectiveManagerAdolescent.getObjectives()) {
            handleObjectiveSelected()
        }
        recyclerView.adapter = adapter

        initializeViews()

        setListeners()
    }

    private fun initializeViews() {
        sumaText = findViewById(R.id.sumaText)
        catPhoto = findViewById(R.id.cat_photo)
        titleText = findViewById(R.id.title_text)
        addObjectiveButton = findViewById(R.id.addObjectiveButton)

        notificationIcon = findViewById(R.id.notification_icon_ring)

        homeButton = findViewById(R.id.homeButton)
        pusculitaButton = findViewById(R.id.pusculitaButton)
        goalsButton = findViewById(R.id.goalsButton)
        learnButton = findViewById(R.id.learnButton)

        profileSection = findViewById(R.id.profile_section)

        sumaInput = findViewById(R.id.sumaInput)
        adaugaSumaButton = findViewById(R.id.adaugaSumaButton)
        anuleazaSumaButton = findViewById(R.id.anuleazaSumaButton)

        errorMessage1 = findViewById(R.id.errorMessage1)
        errorMessageText1 = findViewById(R.id.errorMessageText1)
        errorMessage2 = findViewById(R.id.errorMessage2)
        errorMessageText2 = findViewById(R.id.errorMessageText2)
        successMessage = findViewById(R.id.successMessage)
        successMessageText = findViewById(R.id.successMessageText)

        manageObjectiveSection = findViewById(R.id.manageObjectiveSection)
        manageObjectiveElements = findViewById(R.id.manageObjectiveElements)
        manageObjectiveTitle = findViewById(R.id.manageObjectiveTitle)
        manageObjectiveButton = findViewById(R.id.manageObjectiveButton)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.addObjectiveButton -> {
                startActivity(Intent(this, AddObjectiveActivityAdolescent::class.java))
            }
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


    override fun onResume() {
        super.onResume()
        adapter.updateObjectives(ObjectiveManagerAdolescent.getObjectives())
        adapter.notifyDataSetChanged()
    }

    fun handleManageObjectiveButtonClick() {
        manageObjectiveTitle.visibility = View.GONE
        manageObjectiveButton.visibility = View.GONE

        manageObjectiveSection.visibility = View.VISIBLE
        sumaText.visibility = View.VISIBLE
        sumaInput.visibility = View.VISIBLE
        adaugaSumaButton.visibility = View.VISIBLE
        anuleazaSumaButton.visibility = View.VISIBLE
    }

    fun handleObjectiveSelected() {
        catPhoto.visibility = View.GONE
        titleText.visibility = View.GONE
        addObjectiveButton.visibility = View.GONE

        manageObjectiveElements.visibility = View.VISIBLE
        manageObjectiveTitle.visibility = View.VISIBLE
        manageObjectiveButton.visibility = View.VISIBLE

        manageObjectiveSection.visibility = View.GONE
        sumaInput.visibility = View.GONE
        adaugaSumaButton.visibility = View.GONE
        anuleazaSumaButton.visibility = View.GONE
        errorMessage1.visibility = View.GONE
        errorMessage2.visibility = View.GONE
        successMessage.visibility = View.GONE
    }

    fun resetToHomeState() {
        catPhoto.visibility = View.VISIBLE
        titleText.visibility = View.VISIBLE
        addObjectiveButton.visibility = View.VISIBLE

        manageObjectiveElements.visibility = View.GONE
        manageObjectiveSection.visibility = View.GONE
        sumaInput.text.clear()
        errorMessage1.visibility = View.GONE
        errorMessage2.visibility = View.GONE
        successMessage.visibility = View.GONE
    }

    private fun setListeners() {
        addObjectiveButton.setOnClickListener(this)

        homeButton.setOnClickListener(this)
        pusculitaButton.setOnClickListener(this)
        goalsButton.setOnClickListener(this)
        learnButton.setOnClickListener(this)

        profileSection.setOnClickListener(this)

        notificationIcon.setOnClickListener {
            startActivity(Intent(this, NotificationActivityAdolescent::class.java))
        }

        manageObjectiveButton.setOnClickListener {
            handleManageObjectiveButtonClick()
        }

        adaugaSumaButton.setOnClickListener {
            val selectedPosition = adapter.getSelectedPosition()
            if (selectedPosition == RecyclerView.NO_POSITION) {
                Toast.makeText(this, getString(R.string.toast_select_objective), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sumaAdaugata = sumaInput.text.toString().toDoubleOrNull()
            errorMessage1.visibility = View.GONE
            errorMessage2.visibility = View.GONE
            successMessage.visibility = View.GONE

            if (sumaAdaugata == null || sumaAdaugata <= 0) {
                afisareTemporaraMesaje(errorMessage1, 3000)
                errorMessageText1.text = getString(R.string.error_specify_amount)
                return@setOnClickListener
            }

            val selectedObjective = ObjectiveManagerAdolescent.getObjectives()[selectedPosition]

            if (selectedObjective.sumaCurenta + sumaAdaugata > selectedObjective.sumaTotala) {
                afisareTemporaraMesaje(errorMessage2, 3000)
                errorMessageText2.text = getString(R.string.error_exceeds_objective)
                sumaInput.text.clear()
                return@setOnClickListener
            }

            selectedObjective.sumaCurenta += sumaAdaugata
            afisareTemporaraMesaje(successMessage, 3000)
            successMessageText.text = getString(R.string.success_amount_added)
            adapter.updateObjectives(ObjectiveManagerAdolescent.getObjectives())
            adapter.notifyDataSetChanged()

            sumaInput.text.clear()

            if (selectedObjective.sumaCurenta == selectedObjective.sumaTotala) {
                Toast.makeText(this, getString(R.string.toast_objective_completed), Toast.LENGTH_LONG).show()
            }

            adapter.unlockSelection()
            resetToHomeState()
        }


        anuleazaSumaButton.setOnClickListener {
            sumaInput.text.clear()
            errorMessage1.visibility = View.GONE
            errorMessage2.visibility = View.GONE
            successMessage.visibility = View.GONE
            adapter.unlockSelection()
            resetToHomeState()
        }
    }
}
