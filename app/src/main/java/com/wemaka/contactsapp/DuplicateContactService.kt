package com.wemaka.contactsapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wemaka.aidl.DeleteResultAidl
import com.wemaka.aidl.DuplicateContactResultAidl
import com.wemaka.aidl.IDuplicateContact
import com.wemaka.data.repository.ContactRepository
import com.wemaka.domain.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DuplicateContactService : Service(), KoinComponent {
    private val contactRepository: ContactRepository by inject()
    private val notification: NotificationHelper by inject()

    override fun onBind(intent: Intent?): IBinder {
        return object : IDuplicateContact.Stub() {
            override fun onResult(): DuplicateContactResultAidl {
                val result = contactRepository.deleteDuplicateContacts()

                if (result.result == DeleteResultAidl.SUCCESS || result.result == DeleteResultAidl.NOT_FOUND) {
                    notification.showSuccessNotification(result.deleteCount)
                } else {
                    notification.showErrorNotification()
                }

                return result
            }
        }
    }
}
