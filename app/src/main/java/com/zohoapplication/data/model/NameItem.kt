package com.zohoapplication.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Entity(tableName = "name")
@Parcelize
data class NameItem(
    @PrimaryKey()
    var uuid : String,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String?,

    @ColumnInfo(name = "first")
    @SerializedName("first")
    val first: String?,

    @ColumnInfo(name = "last")
    @SerializedName("last")
    val last: String?
) : Parcelable