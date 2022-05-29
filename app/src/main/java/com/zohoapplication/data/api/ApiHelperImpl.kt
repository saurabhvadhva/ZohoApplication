package com.zohoapplication.data.api

import com.zohoapplication.data.model.WeatherResponse
import com.zohoapplication.data.model.RandomUserResponse
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getRandomUsers(result : Int): Response<RandomUserResponse> = apiService.getRandomUsers(result)

    override suspend fun getWeather(key: String,
                                    state: String?): Response<WeatherResponse> = apiService.getWeather(key,state)

}