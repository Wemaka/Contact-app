package com.wemaka.domain

import com.wemaka.data.model.DuplicateContactResult
import com.wemaka.data.repository.ContactRepository

class DeleteDuplicateUseCase(
    private val contactRepository: ContactRepository
) {
    suspend operator fun invoke(): DuplicateContactResult? {
        return contactRepository.deleteDuplicatesService()
    }
}