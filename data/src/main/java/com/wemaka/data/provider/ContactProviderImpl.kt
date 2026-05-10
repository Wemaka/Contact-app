package com.wemaka.data.provider

import android.content.ContentResolver
import android.provider.ContactsContract
import com.wemaka.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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

    private fun getAllContact(): List<Contact> {
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

    override fun contactsFlow(): Flow<List<Contact>> {
        return flow {
            emit(getAllContact())
        }.flowOn(Dispatchers.IO)
    }
}