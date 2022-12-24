package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.ValidateData
import com.quannm18.coolmateapp.utils.ValidateData.Companion.validateEmail
import com.quannm18.coolmateapp.view.dialog.DialogAsk
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_forget_password.view.*

class ForgetPasswordActivity : BaseActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private var otp = ""
    private lateinit var loadingDialog: LoadingDialog
    override fun layoutID(): Int = R.layout.activity_forget_password

    override fun initData() {
    }

    override fun initView() {
        marginStatusBar(listOf(imgBackForgetPassword))
        marginNavigationBar(listOf(btnConfirm))
        configOtpEdittext(edtVerify1, edtVerify2, edtVerify3, edtVerify4, edtVerify5, edtVerify6)
        loadingDialog = LoadingDialog(this)


    }

    override fun listenLiveData() {
    }

    override fun listeners() {
        onClickDone()
        btnConfirm.setOnClickListener {
//            onClickResetPassword()
            validate()
        }
        imgBackForgetPassword.setOnClickListener {
            finish()
        }
    }

    private fun onClickDone() {
        tilEmail.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //su ly su kien get otp o day
                if (Patterns.EMAIL_ADDRESS.matcher(tilEmail.editText?.text!!.trim().toString())
                        .matches()
                ) {
                    getOtpFromEmail(tilEmail.editText?.text?.trim().toString())
                    tilEmail.isErrorEnabled = false
                    edtVerify1.requestFocus()
                } else {
                    tilEmail.error = "Sai định dạng email"
                    tilEmail.isErrorEnabled = true
                }

                Toast.makeText(
                    this@ForgetPasswordActivity,
                    tilEmail.editText?.text,
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        }
    }

    private fun getOtpFromEmail(email: String) {
        userViewModel.getOtpFromEmail(email).observe(this) {
            it.let { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        if (res.data?.code() == 200) {
                            Snackbar.make(
                                layoutContainer,
                                "Vui lòng kiểm tra hòm thư",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else if (res.data?.code() == 404) {
                            Snackbar.make(
                                layoutContainer,
                                "Tài khoản không tồn tại",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun configOtpEdittext(vararg editTexts: TextInputEditText) {
        val afterTextChanged = { index: Int, e: Editable ->
            val view = editTexts[index]
            val text = e.toString()

            when (view.id) {
                // first text changed
                editTexts[0].id -> {
                    if (text.isNotEmpty()) editTexts[index + 1].requestFocus()
                }
                // las text changed
                editTexts[editTexts.size - 1].id -> {
                    if (text.isEmpty()) editTexts[index - 1].requestFocus()
                }
                // middle text changes
                else -> {
                    if (text.isNotEmpty()) editTexts[index + 1].requestFocus()
                    else editTexts[index - 1].requestFocus()
                }
            }
            false
        }
        editTexts.forEachIndexed { index, editText ->
            editText.doAfterTextChanged { it?.let { it1 -> afterTextChanged(index, it1) } }
        }

    }

    private fun validate() {
        otp =
            edtVerify1.text.toString() + edtVerify2.text.toString() + edtVerify3.text.toString() + edtVerify4.text.toString() + edtVerify5.text.toString() + edtVerify6.text.toString()
        val email = tilEmail.editText?.text?.trim().toString()
        val newPassword = tilPassword.editText?.text?.trim().toString()
        val confirmPassword = tilConfirmPassword.editText?.text?.trim().toString()


        if (otp.isEmpty() || email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            DialogAsk(
                this,
                "Thiếu trường dữ liệu",
                "Vui lòng nhập đủ trường dữ liệu"
            ).show()
            return
        }
        if (!email.validateEmail() || email.substring(0, email.indexOf("@")).length < 6) {
            DialogAsk(
                this,
                "Sai định dạng E-mail",
                "Vui lòng nhập đúng định dạng E-mail"
            ).show()
            return
        }
        if (tilVerifyCode1.edtVerify1.text.toString().isEmpty() ||
            tilVerifyCode2.edtVerify2.text.toString().isEmpty() ||
            tilVerifyCode3.edtVerify3.text.toString().isEmpty() ||
            tilVerifyCode4.edtVerify4.text.toString().isEmpty() ||
            tilVerifyCode5.edtVerify5.text.toString().isEmpty() ||
            tilVerifyCode6.edtVerify6.text.toString().isEmpty()
        ) {
            DialogAsk(this, "Nhập sai mã OTP", "Vui lòng nhập đủ mã otp").show()
            return
        }
        if (otp.isEmpty() || email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            DialogAsk(this, "Sai định dạng", "Vui lòng nhập đủ dữ liệu").show()
            return
        }
        if (newPassword.length < 6) {
            DialogAsk(this, "Sai định dạng", "Mật khẩu phải từ 6 ký tự").show()
            return
        }
        if (confirmPassword != newPassword) {
            DialogAsk(this, "Sai định dạng", "Phải trùng khớp với mật khẩu").show()
            return
        }
        onClickResetPassword(email, otp, newPassword, confirmPassword)
    }

    private fun onClickResetPassword(
        email: String,
        otp: String,
        newPassword: String,
        confirmPassword: String
    ) {
        userViewModel.postResetPassword(email, otp, newPassword, confirmPassword).observe(this) {
            it?.let { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        res.data.let { data ->
                            if (data?.code() == 200) {
                                startActivity(Intent(this,LoginActivity::class.java))
                                loadingDialog.dismissDialog()
                            } else {
                                loadingDialog.dismissDialog()
                                DialogAsk(this,"Đặt lại mật khẩu thất bại","Vui lòng kiểm tra lại dữ liệu")
                            }
                        }
                    }
                    Status.ERROR -> {
                        loadingDialog.dismissDialog()
                        DialogAsk(this,"Đặ lại mật khẩu thất bại","Vui lòng kiểm tra lại dữ liệu")
                    }
                    Status.LOADING -> {
                        loadingDialog.startLoadingDialog()
                    }
                }
            }
        }
    }
}