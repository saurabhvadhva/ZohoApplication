package com.zohoapplication.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zohoapplication.data.model.*

@Database(entities = [UserItem::class, NameItem::class, PictureItem::class, LocationItem::class,LoginItem::class], exportSchema = false, version = 1)
@TypeConverters(NameItemConverter::class, PictureItemConverter::class, LocationItemConverter::class, LoginItemConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao : UserDao
}