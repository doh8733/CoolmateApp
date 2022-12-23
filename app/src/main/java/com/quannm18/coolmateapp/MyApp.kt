package com.quannm18.coolmateapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.service.MyFirebaseMessagingService
import com.quannm18.coolmateapp.utils.Constants.Companion.CHANNEL_ID
import com.quannm18.coolmateapp.utils.Constants.Companion.DESCRIPTION
import com.quannm18.coolmateapp.utils.Constants.Companion.NAME_CHANNEL
import com.quannm18.coolmateapp.utils.zalopay.AppInfo
import com.sendbird.android.SendBird
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK
import java.text.DecimalFormat

class MyApp : Application() {
    companion object {
        lateinit var glide: RequestManager
        val dec = DecimalFormat("#,###.##")
        val environment: Environment = Environment.SANDBOX
    }

    override fun onCreate() {
        super.onCreate()
        SendBird.init("6096D221-F13B-41B3-BB53-CACFCD6D6589", this)
        glide = Glide.with(this@MyApp)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        createNotificationChannel()
        Hawk.init(this).build()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        ZaloPaySDK.init(AppInfo.APP_ID, environment)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NAME_CHANNEL
            val desc = DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = desc

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}