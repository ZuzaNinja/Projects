package com.example.proiectpiu_managementfinanciar.models;

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Notification(
    val iconResId: Int,
    val title: String,
    val description: String,
    val timestamp: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date()),
    var isRead: Boolean = false,
)
