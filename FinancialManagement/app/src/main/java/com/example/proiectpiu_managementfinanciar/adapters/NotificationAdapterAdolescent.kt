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

class NotificationAdapterAdolescent(
    private val context: Context,
    private val notifications: MutableList<Notification>,
    private val onNotificationClicked: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapterAdolescent.NotificationViewHolderAdolescent>() {

    inner class NotificationViewHolderAdolescent(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.notification_icon)
        val title: TextView = itemView.findViewById(R.id.notification_title)
        val description: TextView = itemView.findViewById(R.id.notification_description)
        val timestamp: TextView = itemView.findViewById(R.id.notification_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolderAdolescent {
        val view = LayoutInflater.from(context).inflate(R.layout.notificare_item_adolescent, parent, false)
        return NotificationViewHolderAdolescent(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolderAdolescent, position: Int) {
        val notification = notifications[position]

        holder.icon.setImageResource(R.drawable.paw1)

        holder.title.text = notification.title
        holder.description.text = notification.description
        holder.timestamp.text = notification.timestamp

        if (notification.isRead) {
            holder.icon.setImageResource(R.drawable.paw2)
        }

        holder.itemView.setBackgroundColor(
            if (notification.isRead) ContextCompat.getColor(context, R.color.gray_light)
            else ContextCompat.getColor(context, R.color.white)
        )

        holder.itemView.setOnClickListener {
            if (!notification.isRead) {
                notification.isRead = true

                holder.icon.setImageResource(R.drawable.paw2)

                notifyItemChanged(position)
                onNotificationClicked(notification)
            }
        }
    }

    override fun getItemCount(): Int = notifications.size
}
