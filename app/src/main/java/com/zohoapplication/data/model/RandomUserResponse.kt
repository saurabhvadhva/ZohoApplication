package com.zohoapplication.data.model

import com.google.gson.annotations.SerializedName

data class RandomUserResponse(
    @SerializedName("results")
    val list : List<UserItem> = emptyList()
)