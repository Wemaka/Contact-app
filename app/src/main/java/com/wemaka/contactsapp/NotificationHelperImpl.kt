package com.wemaka.contactsapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.wemaka.contactsapp.ui.MainActivity
import com.wemaka.domain.NotificationHelper
import com.wemaka.contactsapp.uikit.R

class NotificationHelperImpl(
    private val context: Context
) : NotificationHelper {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showSuccessNotification(deletedCount: Int) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_delete)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(
                if (deletedCount == 0) {
                    context.getString(R.string.duplicate_not_found)
                } else {
                    context.resources.getQuantityString(
                        R.plurals.deleted_count_contacts, deletedCount, deletedCount
                    )
                }
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            1, notification
        )
    }

    override fun showErrorNotification() {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.duplicate_delete_error))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            1, notification
        )
    }

    companion object {
        const val CHANNEL_ID = "notification_channel"
    }
}