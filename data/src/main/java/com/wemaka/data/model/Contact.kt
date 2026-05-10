package com.wemaka.data.model

data class Contact(
    val id: String,
    var contactId: String,
    var name: String,
    var phoneNumber: String,
    val photoUri: String? = null,
)