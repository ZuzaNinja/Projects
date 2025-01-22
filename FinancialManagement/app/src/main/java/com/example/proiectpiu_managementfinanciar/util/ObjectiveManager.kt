package com.example.proiectpiu_managementfinanciar.util

import android.content.Context
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.models.Objective

object ObjectiveManager {
    private val objectiveList: MutableList<Objective> = mutableListOf()

    fun init(context: Context) {
        if (objectiveList.isEmpty()) {
            objectiveList.addAll(
                listOf(
                    Objective(context.getString(R.string.objective_car), 10000.0, 25000.0, R.drawable.car_icon1),
                    Objective(context.getString(R.string.objective_headphones), 150.0, 490.0, R.drawable.headphones_icon2),
                    Objective(context.getString(R.string.objective_sneakers), 300.0, 560.0, R.drawable.sneakers_icon3)
                )
            )
        }
    }

    fun addObjective(objective: Objective) {
        objectiveList.add(objective)
    }

    fun getObjectives(): List<Objective> {
        return objectiveList.toList()
    }
}
