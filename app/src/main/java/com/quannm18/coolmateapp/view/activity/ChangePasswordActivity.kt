package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.Status.*
import com.quannm18.coolmateapp.view.activity.UserActivity.Companion.TAG
import com.quannm18.coolmateapp.view.dialog.DialogAsk
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val sessionManager: SessionManager by lazy { SessionManager() }
    private lateinit var loadingDialog: LoadingDialog
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }

    override fun layoutID(): Int = R.layout.activity_change_password

    override fun initData() {
    }

    override fun initView() {
        loadingDialog = LoadingDialog(this)
    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        btnConfirm.setOnClickListener {
            val currentPassword = tilPassword.editText?.text.toString()
            val newPassword = tilNewPassword.editText?.text.toString()
            val confirmPassword = tilConfirmPassword.editText?.text.toString()
            validate(currentPassword, newPassword, confirmPassword)
        }
    }
    private fun validate(currentPassword: String, newPassword: String, confirmPassword: String) {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            DialogAsk(this, "Thiếu dữ liệu", "Vui lòng nhập đầy đủ dữ liệu").show()
            return
        }
        if (newPassword.length < 6) {
            DialogAsk(this, "Sai định dạng", "Mật khẩu phải từ 6 ký tự trở nên").show()
            return
        }
        if (confirmPassword != newPassword) {
            DialogAsk(this, "Sai định dạng", "Nhập lại phải khớp với mật khẩu mới").show()
            return
        }
        if (managerSaveLocal.getPassword() != currentPassword) {
            DialogAsk(this, "Sai định dạng", "Mật khẩu hiện tại sai vu lòng nhập lại").show()
            return
        }
        onClickChangePassword(currentPassword, newPassword, confirmPassword)

    }

    private fun onClickChangePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        userViewModel.postChangePassword(
            "Bearer ${sessionManager.fetchAuthToken()}",
            currentPassword,
            newPassword,
            confirmPassword)
            .observe(this) {
                it?.let {
                    when (it.status) {
                        ERROR -> {
                            loadingDialog.dismissDialog()
                            DialogAsk(
                                this,
                                "Đổi mật khẩu thất bại",
                                "Mật khẩu không trùng khớp vui lòng kiểm tra lại"
                            )
                        }
                        SUCCESS -> {
                            if (it.data?.code() == 200) {
                                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,LoginActivity::class.java))
                                finishAffinity()
                                loadingDialog.dismissDialog()
                            } else if (it.data?.code() == 400) {
                                DialogAsk(
                                    this,
                                    "Đổi mật khẩu thất bại",
                                    "Mật khẩu không trùng khớp vui lòng kiểm tra lại"
                                )
                                loadingDialog.dismissDialog()
                            }

                        }
                        LOADING -> {
                            loadingDialog.startLoadingDialog()
                        }
                    }
                }
            }
    }
}