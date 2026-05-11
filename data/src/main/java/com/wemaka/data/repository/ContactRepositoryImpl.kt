package com.wemaka.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.wemaka.aidl.DuplicateContactResultAidl
import com.wemaka.aidl.IDuplicateContact
import com.wemaka.data.model.Contact
import com.wemaka.data.model.DuplicateContactResult
import com.wemaka.data.model.toExternal
import com.wemaka.data.provider.ContactProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ContactRepositoryImpl(
    private val context: Context,
    private val contactsProvider: ContactProvider
) : ContactRepository {
    override fun getContacts(): Flow<List<Contact>> {
        return contactsProvider.contactsFlow()
    }

    override fun getAllContacts(): List<Contact> {
        return contactsProvider.getAllContact()
    }

    override fun deleteDuplicateContacts(): DuplicateContactResultAidl {
        return contactsProvider.deleteDuplicateContacts()
    }

    override suspend fun deleteDuplicatesService(): DuplicateContactResult? = suspendCancellableCoroutine { continuation ->
        var proxy: IDuplicateContact? = null

        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                proxy = IDuplicateContact.Stub.asInterface(service)

                try {
                    val resultAidl = proxy?.onResult()
                    val domainResult = resultAidl?.toExternal()

                    if (continuation.isActive) {
                        continuation.resume(domainResult)
                    }
                } catch (e: RemoteException) {
                    if (continuation.isActive) continuation.resumeWithException(e)
                } finally {
                    context.unbindService(this)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                proxy = null
            }
        }

        context.bindService(createExplicitIntent(), connection, Context.BIND_AUTO_CREATE)

        continuation.invokeOnCancellation {
            context.unbindService(connection)
        }
    }

    private fun createExplicitIntent(): Intent {
        val intent = Intent("com.wemaka.aidl.DELETE_DUPLICATION_CONTACTS")
        val services = context.packageManager.queryIntentServices(intent, 0)
        if (services.isEmpty()) {
            throw IllegalStateException("The server application is not installed")
        }
        return Intent(intent).apply {
            val resolveInfo = services[0]
            val packageName = resolveInfo.serviceInfo.packageName
            val className = resolveInfo.serviceInfo.name
            component = ComponentName(packageName, className)
        }
    }
}