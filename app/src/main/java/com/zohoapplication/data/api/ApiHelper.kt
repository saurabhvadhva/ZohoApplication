package com.zohoapplication.data.api

import com.zohoapplication.data.model.WeatherResponse
import com.zohoapplication.data.model.RandomUserResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getRandomUsers(result : Int): Response<RandomUserResponse>

    suspend fun getWeather(key: String,
                           state: String?): Response<WeatherResponse>
}