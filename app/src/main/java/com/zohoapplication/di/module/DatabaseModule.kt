package com.zohoapplication.di.module

import androidx.room.Room
import com.zohoapplication.data.dao.UserDao
import com.zohoapplication.data.dao.UserDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            UserDatabase::class.java,
            "users.database"
        ).build()
    }
    single<UserDao> {
        val database = get<UserDatabase>()
        database.userDao
    }
}