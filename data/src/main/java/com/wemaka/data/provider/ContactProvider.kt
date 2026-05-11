package com.wemaka.data.provider

import com.wemaka.aidl.DuplicateContactResultAidl
import com.wemaka.data.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactProvider {
    fun getAllContact(): List<Contact>
    fun contactsFlow(): Flow<List<Contact>>
    fun deleteDuplicateContacts(): DuplicateContactResultAidl
}