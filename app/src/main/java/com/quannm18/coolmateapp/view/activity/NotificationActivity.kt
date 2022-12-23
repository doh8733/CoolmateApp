package com.quannm18.coolmateapp.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : BaseActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun layoutID(): Int = R.layout.activity_notification

    override fun initData() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    javaClass.simpleName,
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(javaClass.simpleName, "token\n$token")
            Toast.makeText(baseContext, "$token", Toast.LENGTH_SHORT).show()
        })
    }

    override fun initView() {
        btnBackBuying.setOnClickListener {
            finish()
        }
    }

    override fun listenLiveData() {

    }

    override fun listeners() {

    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}