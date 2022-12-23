package com.quannm18.coolmateapp.utils

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.utils.Constants.Companion.CHANNEL_ID

class PermissionUtil {
    companion object {
        fun areNotificationsEnabled(context: Context): Boolean {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled())
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val manager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val channel =
                        manager.getNotificationChannel(CHANNEL_ID)
                    return channel?.importance != NotificationManager.IMPORTANCE_NONE
                } else {
                    true
                }
            return false
        }

        fun checkPermissionPostNotify(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        }
        fun settingNotification(context: Context) {
            if (Build.VERSION_CODES.R <= Build.VERSION.SDK_INT) {
                val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
                context.startActivity(settingsIntent)
            }
        }
    }
}