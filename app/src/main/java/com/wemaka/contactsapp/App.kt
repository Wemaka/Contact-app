package com.wemaka.contactsapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.wemaka.contactsapp.di.rootModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(rootModule)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationHelperImpl.CHANNEL_ID,
            "Delete duplicate",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}