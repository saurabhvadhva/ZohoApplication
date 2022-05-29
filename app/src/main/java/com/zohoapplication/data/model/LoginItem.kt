package com.zohoapplication.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull


@Entity(tableName = "login")
@Parcelize
data class LoginItem(
    @PrimaryKey()
    @SerializedName("uuid")
    var uuid : String,
) : Parcelable