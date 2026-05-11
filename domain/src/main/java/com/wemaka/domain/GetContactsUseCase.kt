package com.wemaka.domain

import com.wemaka.data.model.Contact
import com.wemaka.data.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class GetContactsUseCase(
    private val contactRepository: ContactRepository
) {
    operator fun invoke(): Flow<List<Contact>> {
        return contactRepository.getContacts()
    }
}