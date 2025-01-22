package com.example.proiectpiu_managementfinanciar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proiectpiu_managementfinanciar.models.BudgetItem
import com.example.proiectpiu_managementfinanciar.R

class BudgetListAdapter(private var budgets: MutableList<BudgetItem>) :
    RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {
    class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.budget_item_name)
        val amountTextView: TextView = itemView.findViewById(R.id.budget_item_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.budget_list_item, parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budgetItem = budgets[position]
        holder.nameTextView.text = budgetItem.name
        holder.amountTextView.text = "${budgetItem.amount} lei"
    }

    override fun getItemCount(): Int = budgets.size
    }
