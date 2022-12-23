package com.quannm18.coolmateapp.model

import androidx.room.Entity

@Entity(tableName = "notification_item")
data class NotificationModel(
    var id: String?,
    var title: String?,
    var content: String?,
    var path: String?,
    var time: String?,
)