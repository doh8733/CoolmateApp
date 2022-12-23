package com.quannm18.coolmateapp.service

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.utils.Constants

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        var title = ""
        var text = ""


        val notifications = NotificationCompat.Builder(
            this,
            Constants.CHANNEL_ID
        ).setAutoCancel(true)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.logo_black_white)
            .setContentTitle(text)
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        notificationManager.notify(1, notifications.build())
        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String) {
        Log.e(javaClass.simpleName, "onNewToken: $token", )
        sendRegistrationToServer(token)

        super.onNewToken(token)
    }
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(javaClass.simpleName, "sendRegistrationTokenToServer($token)")
    }
}