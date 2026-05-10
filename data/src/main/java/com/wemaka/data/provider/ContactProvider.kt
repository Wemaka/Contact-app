package com.wemaka.data.provider

import com.wemaka.data.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactProvider {
    fun contactsFlow(): Flow<List<Contact>>
}