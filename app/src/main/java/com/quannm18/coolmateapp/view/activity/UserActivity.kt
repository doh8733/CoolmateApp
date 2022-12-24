package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.activity.viewModels
import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.user.ResponseLogin
import com.quannm18.coolmateapp.model.user.UserInfo
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.dialog.DialogLogOut
import com.quannm18.coolmateapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : BaseActivity() {
    private val mUserViewModel: LoginViewModel by viewModels()
    private var responseLogin: ResponseLogin = ResponseLogin()
    private lateinit var sessionManager: SessionManager

    override fun layoutID(): Int = R.layout.activity_user

    override fun initData() {
        sessionManager = SessionManager()
        val responseLoginTemp = Hawk.get<ResponseLogin>("ResponseLogin")
        responseLoginTemp?.let {
            responseLogin = it
        }
    }

    override fun initView() {
        glide.load(R.drawable.avatar).centerInside().override(WRAP_CONTENT).into(circleImageView)
        refreshData()
    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        btnRefreshUser.setOnClickListener {
            refreshData()
        }

        btnBackUser.setOnClickListener {
            finish()
        }

        btnEditUser.setOnClickListener {
            startActivity(Intent(this, InfoEditActivity::class.java))
        }
        btnTheoDoiDonUser.setOnClickListener {

        }
        btnHistoryUser.setOnClickListener {

        }
        btnChangePassUser.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        btnLogOut.setOnClickListener {
            DialogLogOut.newInstance(this@UserActivity).apply {
                show()
                event.observe(this@UserActivity) {
                    if (it) {
                        startActivity(Intent(this@UserActivity, LoginActivity::class.java))
                    }
                    dismiss()
                }
            }
        }
    }

    private fun refreshData() {
        mUserViewModel.me(authToken = "Bearer ${sessionManager.fetchAuthToken()}").observe(this) {
            Log.e(TAG, "token: ${sessionManager.fetchAuthToken()}")
            when (it.status) {
                Status.SUCCESS -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                    setDataForUI(it.data?.user)
                    Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "refreshData: ${it.message}")

                }
                Status.LOADING -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.VISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                    Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    fun fetchDataByID() {
        mUserViewModel.getUserByIDFromAPI("6358a5ec8a099134ecd9f61f").observe(this) {
            Log.e(TAG, "refreshData: ${it.status}")
            when (it.status) {
                Status.SUCCESS -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                    setDataForUI(it.data)
                    Log.e(TAG, "${it.data?.email ?: "dvsv"}")
                }
                Status.ERROR -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                }
                Status.LOADING -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.VISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                }
            }
        }
    }

    private fun setDataForUI(userInfo: UserInfo?) {
        userInfo?.apply {
            tvEmailUser.text = email
            tvNameUser.text = fullName
            tvPhoneUser.text = phone

            if (status == "INACTIVE") {
                layoutUser.setVisibility(
                    R.id.imgTickUser,
                    View.INVISIBLE,
                    CommonUtils.Companion.IS_END.BOTH
                )
                tvVerifyUser.text = "Chưa xác minh"
            } else {
                layoutUser.setVisibility(
                    R.id.imgTickUser,
                    View.VISIBLE,
                    CommonUtils.Companion.IS_END.BOTH
                )
                tvVerifyUser.text = "Đã xác minh"
            }
        }
    }

    companion object {
        val TAG = javaClass.simpleName
    }
}
