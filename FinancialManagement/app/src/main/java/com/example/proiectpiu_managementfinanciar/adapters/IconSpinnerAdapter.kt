package com.example.proiectpiu_managementfinanciar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.proiectpiu_managementfinanciar.R

class IconSpinnerAdapter(
    private val context: Context,
    private val icons: List<Int>,
    private val labels: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = icons.size

    override fun getItem(position: Int): Any = icons[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.objective_spinner_item_icon, parent, false)
        val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
        val iconTextView: TextView = view.findViewById(R.id.iconTextView)

        iconImageView.setImageResource(icons[position])
        iconTextView.text = labels[position]

        iconTextView.textSize = 14f

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.objective_spinner_dropdown_item, parent, false)
        val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
        val iconTextView: TextView = view.findViewById(R.id.iconTextView)

        iconImageView.setImageResource(icons[position])
        iconTextView.text = labels[position]

        iconTextView.textSize = 14f

        return view
    }
}
