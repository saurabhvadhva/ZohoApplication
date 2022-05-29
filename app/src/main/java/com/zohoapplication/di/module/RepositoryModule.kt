package com.zohoapplication.di.module

import com.zohoapplication.data.repository.MainRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepository(get(), userDao = get())
    }
}