package com.zohoapplication.di.module

import com.zohoapplication.di.main.viewmodel.MainViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        MainViewModel(get(),get())
    }
}