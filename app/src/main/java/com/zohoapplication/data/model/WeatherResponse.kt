package com.zohoapplication.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val currentItem : CurrentItem? = null
)