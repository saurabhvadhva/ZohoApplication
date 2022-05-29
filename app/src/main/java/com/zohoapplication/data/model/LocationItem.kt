package com.zohoapplication.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "location")
@Parcelize
data class LocationItem(
    @PrimaryKey()
    var uuid : String,

    @ColumnInfo(name = "city")
    @SerializedName("city")
    val city : String?
) : Parcelable