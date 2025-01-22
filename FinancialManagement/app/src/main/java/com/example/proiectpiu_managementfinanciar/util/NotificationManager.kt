package com.example.proiectpiu_managementfinanciar.util

import com.example.proiectpiu_managementfinanciar.models.Notification

object NotificationManager {
    private val notifications: MutableList<Notification> = mutableListOf()
    var onNotificationUpdated: (() -> Unit)? = null

    fun getNotifications(): List<Notification> {
        return notifications.toList()
    }

    fun addNotification(notification: Notification) {
        notifications.add(0, notification)
        onNotificationUpdated?.invoke()
    }

    fun markNotificationAsRead(index: Int) {
        if (index in notifications.indices) {
            notifications[index].isRead = true
            onNotificationUpdated?.invoke()
        }
    }

}
