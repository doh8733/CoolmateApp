package com.quannm18.coolmateapp.model.banner

import com.google.gson.annotations.SerializedName

data class ResponseBanner(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("fieldname")
    val fieldname: String = "",
    @SerializedName("originalname")
    val originalname: String = "",
    @SerializedName("encoding")
    val encoding: String = "",
    @SerializedName("mimetype")
    val mimetype: String = "",
    @SerializedName("destination")
    val destination: String = "",
    @SerializedName("filename")
    val filename: String = "",
    @SerializedName("path")
    val path: String = "",
    @SerializedName("size")
    val size: Long = 0L,
    @SerializedName("delete")
    val delete: Boolean = false,
    @SerializedName("__v")
    val v: Long
)