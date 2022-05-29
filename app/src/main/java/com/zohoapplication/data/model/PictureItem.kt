package com.zohoapplication.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull


@Entity(tableName = "picture")
@Parcelize
data class PictureItem(
    @PrimaryKey()
    var uuid : String,

    @ColumnInfo(name = "large")
    @SerializedName("large")
    val large: String?,

    @ColumnInfo(name = "medium")
    @SerializedName("medium")
    val medium: String?,

    @ColumnInfo(name = "thumbnail")
    @SerializedName("thumbnail")
    val thumbnail: String?,

    ) : Parcelable