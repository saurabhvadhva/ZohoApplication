package com.zohoapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zohoapplication.data.model.UserItem

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUserItem() : List<UserItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(list: List<UserItem>)
}