package com.example.proiectpiu_managementfinanciar.util

import com.example.proiectpiu_managementfinanciar.models.BudgetItem

object BudgetManager {
    private val temporaryBudgets = mutableListOf<BudgetItem>()

    private val predefinedBudgets = listOf(
        BudgetItem("Groceries", 300),
        BudgetItem("Rent", 1200),
        BudgetItem("Utilities", 150)
    )


    fun addTemporaryBudget(budget: BudgetItem) {
        if (budget.name.isNotBlank() && budget.amount > 0) {
            if (!temporaryBudgets.any { it.name == budget.name }) {
                temporaryBudgets.add(budget)
            }
        }
    }

    fun getAllBudgets(): MutableList<BudgetItem> {
        return (predefinedBudgets + temporaryBudgets).toMutableList()
    }

    fun getTotalAmount(): Int {
        return getAllBudgets().sumOf { it.amount }
    }


}
