package com.wemaka.data.repository

import com.wemaka.aidl.DuplicateContactResultAidl
import com.wemaka.data.model.Contact
import com.wemaka.data.model.DuplicateContactResult
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getContacts(): Flow<List<Contact>>
    fun getAllContacts(): List<Contact>
    fun deleteDuplicateContacts(): DuplicateContactResultAidl
    suspend fun deleteDuplicatesService(): DuplicateContactResult?
}