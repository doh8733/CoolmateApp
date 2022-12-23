package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.bus.ListenerValidate
import com.quannm18.coolmateapp.model.user.UserRegister
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.dialog.DialogShowValidate
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private var userRegister: UserRegister = UserRegister()
    private lateinit var loadingDialog: LoadingDialog
    override fun layoutID(): Int = R.layout.activity_register

    override fun initData() {
        loadingDialog = LoadingDialog(this)
    }

    override fun initView() {

    }

    override fun listenLiveData() {
        registerViewModel.event.observe(this) {
            when (it) {
                is ListenerValidate -> {
                    if (it.status != Status.SUCCESS) {
                        DialogShowValidate(this, it).apply {
                            show()
                        }
                    } else {
                        registerViewModel.register(userRegister).observe(this) {
                            when (it.status) {
                                Status.LOADING -> {
                                    loadingDialog.startLoadingDialog()
                                }
                                Status.SUCCESS -> {
                                    try {
                                        loadingDialog.dismissDialog()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                Status.ERROR -> {
                                    Log.e(LoginActivity.TAG, "listeners: ${it.message}")
                                    loadingDialog.dismissDialog()
                                    if(it.message?.contains("409") == true){
                                        DialogShowValidate(this, ListenerValidate("Tài khoản đã tồn tại!")).apply {
                                            show()
                                        }
                                        return@observe
                                    }
                                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun listeners() {
        tvLogin.setOnClickListener {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnRegister.setOnClickListener {
            userRegister = UserRegister()
            userRegister.email = tilEmail.editText!!.text.toString()
            userRegister.fullName = tilUserName.editText!!.text.toString()
            userRegister.password = tilPassword.editText!!.text.toString()

            Log.e(javaClass.simpleName, "listeners: $userRegister")
            registerViewModel.registerAndValidate(
                userRegister,
                tilReInputPassword.editText!!.text.toString()
            )
        }

        imgBack.setOnClickListener {
            finish()
        }
    }
}