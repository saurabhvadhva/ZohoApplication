package com.zohoapplication.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.lang.reflect.Type
import java.sql.Timestamp


@Entity(tableName = "users")
@Parcelize
data class UserItem (
    @PrimaryKey()
    var uuid : String,

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    val gender : String?,

    @SerializedName("name")
    val nameItem: NameItem?,

    @ColumnInfo(name = "email")
    @SerializedName("email")
    val email : String?,

    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    val phone : String?,

    @ColumnInfo(name = "cell")
    @SerializedName("cell")
    val cell : String?,

    @SerializedName("picture")
    val pictureItem : PictureItem?,

    @SerializedName("location")
    val locationItem : LocationItem?,

    @SerializedName("login")
    val loginItem : LoginItem?

    ) : Parcelable {

}

class NameItemConverter {
    @TypeConverter
    fun fromString(value: String?): NameItem {
        val type: Type = object : TypeToken<NameItem>() {}.type
        return Gson().fromJson<NameItem>(value, type)
    }

    @TypeConverter
    fun fromArrayList(nameItem: NameItem?): String {
        val gson = Gson()
        return gson.toJson(nameItem)
    }
}

class PictureItemConverter {
    @TypeConverter
    fun fromString(value: String?): PictureItem {
        val type: Type = object : TypeToken<PictureItem>() {}.type
        return Gson().fromJson<PictureItem>(value, type)
    }

    @TypeConverter
    fun fromArrayList(pictureItem: PictureItem?): String {
        val gson = Gson()
        return gson.toJson(pictureItem)
    }
}

class LocationItemConverter {
    @TypeConverter
    fun fromString(value: String?): LocationItem {
        val type: Type = object : TypeToken<LocationItem>() {}.type
        return Gson().fromJson<LocationItem>(value, type)
    }

    @TypeConverter
    fun fromArrayList(locationItem: LocationItem?): String {
        val gson = Gson()
        return gson.toJson(locationItem)
    }
}

class LoginItemConverter {
    @TypeConverter
    fun fromString(value: String?): LoginItem {
        val type: Type = object : TypeToken<LoginItem>() {}.type
        return Gson().fromJson<LoginItem>(value, type)
    }

    @TypeConverter
    fun fromArrayList(loginItem: LoginItem?): String {
        val gson = Gson()
        return gson.toJson(loginItem)
    }
}