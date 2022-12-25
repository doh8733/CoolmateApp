package com.quannm18.coolmateapp.view.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.user.ResponseLogin
import com.quannm18.coolmateapp.model.user.UserInfo
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.Status.*
import com.quannm18.coolmateapp.view.dialog.DialogLogOut
import com.quannm18.coolmateapp.viewmodel.LoginViewModel
import com.sendbird.android.constant.StringSet.key
import com.sendbird.android.shadow.okhttp3.Cache.key
import com.sendbird.android.shadow.okhttp3.MultipartBody
import com.sendbird.android.shadow.okhttp3.RequestBody
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.dialog_upload_image.*
import kotlinx.android.synthetic.main.dialog_upload_image.view.*
import kotlinx.coroutines.launch
import okhttp3.Cache.Companion.key
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.math.log

class UserActivity : BaseActivity() {
    private val mUserViewModel: LoginViewModel by viewModels()
    private var responseLogin: ResponseLogin = ResponseLogin()
    private lateinit var sessionManager: SessionManager
   private lateinit var  view :View
    private lateinit var imgUri: Uri
    private val contact = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imgUri = it!!
        circleImageView.setImageURI(it)
        view.imageAvatar.setImageURI(it)
    }

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
        circleImageView.setOnClickListener {
            uploadImageDialog()
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
                SUCCESS -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                    setDataForUI(it.data?.user)
                    Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                }
                ERROR -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "refreshData: ${it.message}")

                }
                LOADING -> {
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
                SUCCESS -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                    setDataForUI(it.data)
                    Log.e(TAG, "${it.data?.email ?: "dvsv"}")
                }
                ERROR -> {
                    layoutUser.setVisibility(
                        R.id.progressBarLoading,
                        View.INVISIBLE,
                        CommonUtils.Companion.IS_END.BOTH
                    )
                }
                LOADING -> {
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
    private fun uploadImageDialog(){
        val dialog = Dialog(this)
        view = LayoutInflater.from(this).inflate(R.layout.dialog_upload_image,null)
        dialog.setContentView(view)
        dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        dialog.create()
        view.btnRetryLogin.setOnClickListener {
            uploadAvatar()
        }
        view.imageAvatar.setOnClickListener {
            contact.launch("image/*")
        }
        dialog.show()
    }

    @SuppressLint("Recycle")
    fun uploadAvatar() {
        val filesDir = applicationContext.filesDir
        val file = File(filesDir, "image.png")
        Log.e(TAG, "uploadAvatar: $file", )
        Log.e(TAG, "uploadAvatar: ${file.name}", )
        val inputStream = contentResolver.openInputStream(imgUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = okhttp3.MultipartBody.Part.createFormData("upload", file.name, requestBody)
        Log.e(TAG, "uploadAvatar: ${part.toString()}", )
//        val key: okhttp3.RequestBody =
//            getString(R.string.key).toRequestBody("text/plain".toMediaTypeOrNull())
        lifecycleScope.launch {
            mUserViewModel.changeAvatar("Bearer ${sessionManager.fetchAuthToken()}", part)
                .observe(this@UserActivity) {
                it.let {
                    when(it.status){
                        ERROR ->   {
                            Toast.makeText(this@UserActivity, "That bai", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "uploadAvatar: ${it.message}", )
                        }
                        SUCCESS -> {
                            glide.load(it.data?.avatar).into(circleImageView)
                            Toast.makeText(this@UserActivity, "Thành công", Toast.LENGTH_SHORT).show()
                        }
                        LOADING -> {
                            Toast.makeText(this@UserActivity, "Doi chut", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                }
        }
    }

    companion object {
        val TAG = javaClass.simpleName
    }
}
