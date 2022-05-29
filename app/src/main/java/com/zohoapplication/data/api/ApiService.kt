package com.zohoapplication.data.api

import com.zohoapplication.data.model.WeatherResponse
import com.zohoapplication.data.model.RandomUserResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("api")
    suspend fun getRandomUsers(
        @Query("results") result : Int
    ): Response<RandomUserResponse>

    @GET("forecast")
    suspend fun getWeather(
        @Query("access_key") key: String,
        @Query("query") state: String?
    ): Response<WeatherResponse>

}