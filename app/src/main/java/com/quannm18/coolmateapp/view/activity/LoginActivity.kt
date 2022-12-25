package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.bus.ListenerValidate
import com.quannm18.coolmateapp.model.user.ResponseLogin
import com.quannm18.coolmateapp.model.user.UserLogin
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.dialog.DialogAsk
import com.quannm18.coolmateapp.view.dialog.DialogShowValidate
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.LoginViewModel
import com.sendbird.android.GroupChannel
import com.sendbird.android.GroupChannelParams
import com.sendbird.android.SendBird
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.tilPassword

class LoginActivity : BaseActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val userLogin: UserLogin = UserLogin()
    private var selectedUsers = ArrayList<String>()
    private lateinit var loadingDialog: LoadingDialog
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }

    override fun layoutID(): Int = R.layout.activity_login

    override fun initData() {
        loadingDialog = LoadingDialog(this)
        lightStatusBar()
    }

    override fun initView() {
        managerSaveLocal.getLogin()?.let {
            tilEmail.editText?.setText(it.email)
            tilPassword.editText?.setText(it.password)
            chkRemember.isChecked = it.email != null
        }
    }

    override fun listenLiveData() {
        loginViewModel.event.observe(this) {
            when (it) {
                is ListenerValidate -> {
                    if (it.status != Status.SUCCESS) {
                        DialogShowValidate(this, it).apply {
                            show()
                        }
                    } else {
                        loginViewModel.login(userLogin).observe(this) {
                            when (it.status) {
                                Status.LOADING -> {
                                    loadingDialog.startLoadingDialog()
                                }
                                Status.SUCCESS -> {
                                    try {
                                        if (it.data?.code() == 200) {
                                            it.data?.body()?.let { res ->
                                                loginViewModel.saveTokenAndRememberUserData(
                                                    res,
                                                    chkRemember.isChecked,
                                                    userLogin
                                                )
                                                managerSaveLocal.savePassword(
                                                    tilPassword.editText?.text.toString().trim()
                                                )
                                                connectToSendbird(res)
                                            }
                                        } else {
                                            loadingDialog.dismissDialog()
                                            DialogAsk(
                                                this,
                                                "Đăng nhập thất bại",
                                                "Sai tên tài khoản hoặc mật khẩu"
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                Status.ERROR -> {
                                    loadingDialog.dismissDialog()
                                    Log.e(TAG, "listeners: ${it.message}")
                                    if (it.message?.contains("400") == true) {
                                        DialogShowValidate(
                                            this,
                                            ListenerValidate(message = "Tài khoản hoặc mật khẩu không chính xác!")
                                        ).apply {
                                            show()
                                        }
                                        return@observe
                                    }
                                    if (it.message?.contains("500") == true) {
                                        DialogShowValidate(
                                            this,
                                            ListenerValidate("Tài khoản không tồn tại hoặc bị khóa!")
                                        ).apply {
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
        btnLogin.setOnClickListener {
            userLogin.timeLogin = System.currentTimeMillis()
            userLogin.email = tilEmail.editText?.text.toString()
            userLogin.password = tilPassword.editText?.text.toString()
            loginViewModel.loginAndValidate(userLogin)
        }
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        imgBack.setOnClickListener {
            finish()
        }
        tvForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
        btnLoginGG.setOnClickListener {

        }

        tvRemember.setOnClickListener {
            chkRemember.isChecked = !chkRemember.isChecked
        }

    }

    private fun connectToSendbird(
        responseLogin: ResponseLogin
    ) {
        SendBird.connect(responseLogin.user?.id) { user, e ->
            if (e != null) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            } else {
                SendBird.updateCurrentUserInfo(
                    responseLogin.user?.fullName,
                    "https://coolmate.pimob.me/${responseLogin.user?.avatar}"
                ) { e ->
                    if (e != null) {
                        Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                    } else {
                        if ((responseLogin.user?.chatLink?.length ?: 0) < 7) {
                            createChannel(selectedUsers, tilPassword.editText?.text.toString())
                        } else {
                            Toast.makeText(
                                this,
                                "Connect to server successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            loadingDialog.dismissDialog()
                            //luu y cai nay se luu gui link chanel ddeen man home khi bam nut chat se su dung no
                            val i = Intent(this, HomeActivity::class.java)
                            responseLogin.user?.chatLink?.let {
                                managerSaveLocal.saveChatLink(it)
                                managerSaveLocal
                                Log.e(TAG, "connectToSendbird: $it")
                            }
                            responseLogin.user?.avatar?.let {
                                managerSaveLocal.saveAvatar(it)
                                Log.e(TAG, "connectToSendbird: $it")
                            }
                            startActivity(i)
                        }
                    }
                }
            }
        }
    }


    private fun createChannel(
        users: MutableList<String>,
        password: String
    ) {
        val params = GroupChannelParams()
        val operatorId = ArrayList<String>()
        operatorId.add(SendBird.getCurrentUser().userId)
        selectedUsers.add("123456")
        params.addUserIds(users)
        params.setOperatorUserIds(operatorId)
        GroupChannel.createChannel(params) { groupChannel, e ->
            if (e != null) {
                Log.e("ERROR", "createChannel: ${e.message}")
            } else {
                addLinkChat(groupChannel.url, password)
                Log.e(TAG, "createChannel: ${groupChannel.url}")
                Toast.makeText(this, "Thanh cong ${groupChannel.url}", Toast.LENGTH_SHORT).show()
            }

        }

    }

    // them link chat vao user
    private fun addLinkChat(chatLink: String, password: String) {
        loginViewModel.putChatLink(
            "Bearer ${sessionManager.fetchAuthToken()}",
            chatLink = chatLink
        ).observe(this) { response ->
            when (response.status) {
                Status.ERROR -> {
                    Log.e(TAG, "addLinkChat: ${response.message}")
                }
                Status.SUCCESS -> {
                    response.data?.let {
                        managerSaveLocal.saveChatLink(chatLink)
                        val i = Intent(this, HomeActivity::class.java)
                        Log.e(TAG, "createChannel: ${chatLink}")
                        startActivity(i)
                    }
                }
                Status.LOADING -> {
                    Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        val TAG = javaClass.simpleName
    }
}