package com.wemaka.contactsapp.di

import com.wemaka.data.di.dataModule
import com.wemaka.domain.di.domainModule
import features.contacts.impl.contactsImplModule
import org.koin.dsl.module

val rootModule = module {
    includes(
        dataModule
    )

    includes(
        domainModule
    )

    includes(
        contactsImplModule
    )
}