package com.wemaka.data.provider

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.wemaka.aidl.DeleteResultAidl
import com.wemaka.aidl.DuplicateContactResultAidl
import com.wemaka.data.model.Contact
import core.common.normalizePhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.collections.component1
import kotlin.collections.component2

class ContactProviderImpl(
    private val contentResolver: ContentResolver
) : ContactProvider {
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.TYPE,
        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
    )
    val selection = "${ContactsContract.CommonDataKinds.Phone.TYPE} = ?"
    val selectionArgs = arrayOf(
        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE.toString()
    )

    override fun getAllContact(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use { c ->
            with(c) {
                val idIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
                val contactIdIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nameIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val photoIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

                while (moveToNext()) {
                    val id = getString(idIndex)
                    val contactId = getString(contactIdIndex)
                    val name = getString(nameIndex)
                    val number = getString(numberIndex)
                    val photoUri = getString(photoIndex)

                    contacts.add(
                        Contact(
                            id = id,
                            contactId = contactId,
                            name = name,
                            phoneNumber = number,
                            photoUri = photoUri
                        )
                    )
                }
            }
        }

        return contacts.distinctBy { it.contactId }
    }

    override fun contactsFlow(): Flow<List<Contact>> = callbackFlow {
        val observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                trySend(getAllContact())
            }
        }

        contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            observer
        )

        trySend(getAllContact())

        awaitClose {
            contentResolver.unregisterContentObserver(observer)
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteDuplicateContacts(): DuplicateContactResultAidl {
        val findDuplicate = findDuplicates()

        if (findDuplicate.isEmpty()) return DuplicateContactResultAidl(
            result = DeleteResultAidl.NOT_FOUND,
            deleteCount = 0
        )

        val operations = ArrayList<ContentProviderOperation>()

        findDuplicate.forEach { contactId ->
            val uri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_URI,
                contactId
            )
            operations.add(
                ContentProviderOperation.newDelete(uri).build()
            )
        }

        return try {
            val results = contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            val deletedCount = results.count { it.count != null && it.count!! > 0 }
            DuplicateContactResultAidl(
                result = DeleteResultAidl.SUCCESS,
                deleteCount = deletedCount
            )
        } catch (e: Exception) {
            Log.e("ContactService", "Failed to delete contacts", e)
            DuplicateContactResultAidl(
                result = DeleteResultAidl.ERROR,
                deleteCount = 0
            )
        }
    }

    private fun findDuplicates(): List<String> {
        val contacts = getAllContact()

        val grouped = contacts.groupBy {
            it.name.trim().lowercase() + "|" +
                    it.phoneNumber.normalizePhoneNumber()
        }

        val idsToDelete = mutableListOf<String>()
        grouped.forEach { (_, contacts) ->
            if (contacts.size > 1) {
                idsToDelete.addAll(contacts.drop(1).map { it.contactId })
            }
        }

        return idsToDelete
    }
}