package com.wemaka.domain

interface NotificationHelper {
    fun showSuccessNotification(deletedCount: Int)
    fun showErrorNotification()
}