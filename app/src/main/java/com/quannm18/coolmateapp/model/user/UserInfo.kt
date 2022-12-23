package com.quannm18.coolmateapp.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class UserInfo(
    @ColumnInfo(name = "address")
    @SerializedName("address")
    var address: String = "",
    @ColumnInfo(name = "avatar")
    @SerializedName("avatar")
    val avatar: String = "",
    @ColumnInfo(name = "birthday")
    @SerializedName("birthday")
    val birthday: String = "",
    @ColumnInfo(name = "createdAt")
    @SerializedName("createdAt")
    val createdAt: String = "",
    @ColumnInfo(name = "deletedAt")
    @SerializedName("deletedAt")
    val deletedAt: String = "",
    @ColumnInfo(name = "email")
    @SerializedName("email")
    val email: String = "",
    @ColumnInfo(name = "fullName")
    @SerializedName("fullName")
    val fullName: String = "",
    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    val gender: String = "",
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String = "",
    @ColumnInfo(name = "isCreate")
    @SerializedName("isCreate")
    val isCreate: Boolean = false,
    @ColumnInfo(name = "otp")
    @SerializedName("otp")
    val otp: String = "",
    @ColumnInfo(name = "password")
    @SerializedName("password")
    var password: String = "",
    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    val phone: String = "",
    @ColumnInfo(name = "role")
    @SerializedName("role")
    val role: String = "",
    @ColumnInfo(name = "status")
    @SerializedName("status")
    val status: String = "",
    @ColumnInfo(name = "updatedAt")
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @ColumnInfo(name = "chatLink")
    @SerializedName("chatLink")
    val chatLink: String = "",
    @ColumnInfo(name = "phoneActive")
    @SerializedName("phoneActive")
    val phoneActive: String = ""
)