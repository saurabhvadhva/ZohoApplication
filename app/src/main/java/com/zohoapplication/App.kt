package com.zohoapplication

import android.app.Application
import com.zohoapplication.di.module.appModule
import com.zohoapplication.di.module.databaseModule
import com.zohoapplication.di.module.repoModule
import com.zohoapplication.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule,databaseModule, viewModelModule))
        }
    }

    fun refreshScope() {
        unloadKoinModules(listOf(appModule, repoModule, viewModelModule))
        loadKoinModules(listOf(appModule, repoModule, viewModelModule))
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}