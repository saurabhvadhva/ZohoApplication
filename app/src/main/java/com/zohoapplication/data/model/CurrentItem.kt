package com.zohoapplication.data.model

import com.google.gson.annotations.SerializedName

data class CurrentItem(
    @SerializedName("temperature")
    val temperature: String? = null,

    @SerializedName("weather_icons")
    val weatherIcons: List<String> = emptyList(),

    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String> = emptyList(),

    @SerializedName("wind_speed")
    val windSpeed: String? = null,

    @SerializedName("humidity")
    val humidity: String? = null,

    @SerializedName("pressure")
    val pressure: String? = null,

    @SerializedName("visibility")
    val visibility: String? = null,
)