package com.wemaka.data.di

import com.wemaka.data.provider.ContactProvider
import com.wemaka.data.provider.ContactProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<ContactProvider> {
        ContactProviderImpl(androidContext().contentResolver)
    }
}