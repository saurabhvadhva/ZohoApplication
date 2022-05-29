package com.zohoapplication.data.repository

import com.zohoapplication.data.api.ApiHelper
import com.zohoapplication.data.dao.UserDao
import com.zohoapplication.data.model.UserItem
import com.zohoapplication.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainRepository (private val apiHelper: ApiHelper,private val userDao: UserDao) {

    suspend fun getRandomUsers(result : Int) =  apiHelper.getRandomUsers(result)

    suspend fun getWeather(key: String,
                           state: String?): Response<WeatherResponse> = apiHelper.getWeather(key,state)

    suspend fun getRandomUsersFromLocalDB() =
        withContext(Dispatchers.IO) {
            userDao.getUserItem()
        }

    suspend fun insertUsers(list : List<UserItem>) =
        withContext(Dispatchers.IO) {
            userDao.insertUsers(list)
        }
}