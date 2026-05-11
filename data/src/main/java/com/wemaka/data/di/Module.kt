package com.wemaka.data.di

import com.wemaka.data.provider.ContactProvider
import com.wemaka.data.provider.ContactProviderImpl
import com.wemaka.data.repository.ContactRepository
import com.wemaka.data.repository.ContactRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<ContactProvider> {
        ContactProviderImpl(androidContext().contentResolver)
    }

    single<ContactRepository> {
        ContactRepositoryImpl(get(), get())
    }
}