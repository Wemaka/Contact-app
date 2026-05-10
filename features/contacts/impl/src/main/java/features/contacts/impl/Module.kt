package features.contacts.impl

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val contactsImplModule = module {
    viewModelOf(::ContactsViewModel)
}