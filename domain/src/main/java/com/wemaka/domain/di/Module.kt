package com.wemaka.domain.di

import com.wemaka.domain.DeleteDuplicateUseCase
import com.wemaka.domain.GetContactsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetContactsUseCase)
    factoryOf(::DeleteDuplicateUseCase)
}