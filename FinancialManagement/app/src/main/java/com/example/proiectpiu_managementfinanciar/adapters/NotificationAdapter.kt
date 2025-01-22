package com.example.proiectpiu_managementfinanciar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proiectpiu_managementfinanciar.R
import com.example.proiectpiu_managementfinanciar.models.Notification

class NotificationAdapter(
    private val context: Context,
    private val notifications: MutableList<Notification>,
    private val onNotificationClicked: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.notification_icon)
        val title: TextView = itemView.findViewById(R.id.notification_title)
        val description: TextView = itemView.findViewById(R.id.notification_description)
        val timestamp: TextView = itemView.findViewById(R.id.notification_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notificare_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.icon.setImageResource(notification.iconResId)
        holder.title.text = notification.title
        holder.description.text = notification.description
        holder.timestamp.text = notification.timestamp

        if (notification.isRead) {
            holder.icon.setImageResource(R.drawable.bell)
        } else {
            holder.icon.setImageResource(notification.iconResId)
        }

        holder.title.text = notification.title
        holder.description.text = notification.description
        holder.timestamp.text = notification.timestamp

        holder.itemView.setBackgroundColor(
            if (notification.isRead) ContextCompat.getColor(context, R.color.gray_light)
            else ContextCompat.getColor(context, R.color.white)
        )

        holder.itemView.setOnClickListener {
            if (!notification.isRead) {
                notification.isRead = true
                notifyItemChanged(position)
                onNotificationClicked(notification)
            }
        }
    }

    override fun getItemCount(): Int = notifications.size

}
