package com.quannm18.coolmateapp.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.user.PostNotification
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.PermissionUtil
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.dialog.DialogNhanThongBao
import com.quannm18.coolmateapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_splash.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }
    override fun layoutID(): Int = R.layout.activity_splash


    override fun initData() {

    }

    override fun initView() {
        addBounceView(listOf(btnSignUpSplash, btnLoginSplash))
    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        btnLoginSplash.setOnClickListener {
            startLogin()
        }

        btnSignUpSplash.setOnClickListener {
            FirebaseMessaging.getInstance().isAutoInitEnabled = true
            finish()
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun startLogin() {
        finish()
        managerSaveLocal.getLogin()?.let {
            if (it.timeLogin != null) {
                if (System.currentTimeMillis() - it.timeLogin!! >= 60400000) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    return
                }
            }
        }
        startActivity(Intent(this, LoginActivity::class.java))
    }

}