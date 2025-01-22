package com.example.proiectpiu_managementfinanciar.util

import android.content.Context
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.models.Objective

object ObjectiveManagerAdolescent {
    private val objectiveList: MutableList<Objective> = mutableListOf()

    fun initialize(context: Context) {
        if (objectiveList.isNotEmpty()) {
            println("ObjectiveManagerAdolescent: Already initialized. Skipping reinitialization.")
            return
        }
        objectiveList.clear()
        objectiveList.addAll(
            listOf(
                Objective(context.getString(R.string.objective_bicycle), 500.0, 1000.0, R.drawable.bicycle_icon6),
                Objective(context.getString(R.string.objective_skateboard), 150.0, 400.0, R.drawable.skateboard_icon14),
                Objective(context.getString(R.string.objective_smartphone), 700.0, 1200.0, R.drawable.smartphone_icon4)
            )
        )
        println("ObjectiveManagerAdolescent: Initialized successfully")
    }

    fun addObjective(objective: Objective) {
        require(objective.sumaCurenta <= objective.sumaTotala) { "sumaCurenta nu poate fi mai mare decât sumaTotala" }
        objectiveList.add(objective)
    }

    fun getObjectives(): List<Objective> {
        if (objectiveList.isEmpty()) {
            println("ObjectiveManagerAdolescent: List is empty. Did you call initialize(context)?")
        }
        return objectiveList
    }
}
