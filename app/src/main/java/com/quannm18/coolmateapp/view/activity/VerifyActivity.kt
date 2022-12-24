package com.quannm18.coolmateapp.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.Status.*
import com.quannm18.coolmateapp.view.dialog.DialogAsk
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit

class VerifyActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()

    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private var mVerifyID = ""
    private var phoneNumber = ""
    private var chatLink = ""
    private var avatar = ""
    private lateinit var timer: CountDownTimer
    private lateinit var otp: String
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var mForceResendingToken: PhoneAuthProvider.ForceResendingToken

    override fun layoutID(): Int {
        return R.layout.activity_verify
    }

    override fun initData() {
        auth = FirebaseAuth.getInstance()
        mVerifyID = intent.getStringExtra("verification_id").toString()
        phoneNumber = intent.getStringExtra("PHONE").toString()
        mForceResendingToken = PhoneAuthProvider.ForceResendingToken.zza()

    }

    override fun initView() {
        marginStatusBar(listOf(imgbackVerify))
        countDownTimer()
        tvTextPhone.text = "Mã xác thực được gửi đến số $phoneNumber"
        configOtpEdittext(edtVerify, edtVerify2, edtVerify3, edtVerify4, edtVerify5, edtVerify6)
        loadingDialog = LoadingDialog(this)
        visibleResendText()
        tvResend.visibility = View.GONE

    }

    override fun listenLiveData() {
    }


    private fun visibleResendText() {
        Handler().postDelayed({
            tvResend.visibility = View.VISIBLE
        }, 60000)
    }

    override fun listeners() {
        btnConfirm.setOnClickListener {
            otp = edtVerify.text.toString() +
                    edtVerify2.text.toString() +
                    edtVerify3.text.toString() +
                    edtVerify4.text.toString() +
                    edtVerify5.text.toString() +
                    edtVerify6.text.toString()

            onClickSendOTPCode(otp)
        }
        imgbackVerify.setOnClickListener {
            finish()
        }
    }

    private fun configOtpEdittext(vararg editTexts: EditText) {
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

    private fun onClickSendOTPCode(otp: String) {
        val credential = PhoneAuthProvider.getCredential(mVerifyID!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    Toast.makeText(this, "Thanh cong", Toast.LENGTH_SHORT).show()
                    getDataAndEdit()
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this@VerifyActivity, "loi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun countDownTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvCountDownTime.text =
                    "Mã xác thực sẽ được gửi trong vòng: ${millisUntilFinished / 1000} giây"
                tvCountDownTime.setOnClickListener {

                }
            }

            override fun onFinish() {
                tvCountDownTime.text = "Gửi lại mã xác thực"
            }
        }
        timer.start()


    }

    override fun onResume() {
        super.onResume()
        tvResend.setOnClickListener {
            onResume()
            sendOTPAgain()
            timer.start()
        }
    }

    private fun sendOTPAgain() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)
            .setForceResendingToken(mForceResendingToken)// Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@VerifyActivity, "failed", Toast.LENGTH_SHORT).show()
                    loadingDialog.dismissDialog()
                }
                override fun onCodeSent(
                    verifyID: String,
                    p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verifyID, p1)
                    mVerifyID = verifyID
                    onResume()
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    //chua hoan thien
    private fun getDataAndEdit() {
        var bundle: Bundle = Bundle()
        bundle = intent.extras!!
        val fullName = bundle.getString("FULLNAME")
        val password = bundle.getString("PASSWORD")
        val address = bundle.getString("ADDRESS")
        val token = "Bearer " + sessionManager.fetchAuthToken()
        val gender = "" + Hawk.get("GENDER")
        val birthDay = "" + Hawk.get("BIRTHDAY")
        avatar = Hawk.get("AVATAR")
        chatLink = Hawk.get("CHATLINK")
        Log.e("fullName", "getDataAndEdit: $fullName")
        Log.e("password", "getDataAndEdit: $password")
        Log.e("address", "getDataAndEdit: $address")
        Log.e("token", "getDataAndEdit: $token")
        Log.e("gender", "getDataAndEdit: $gender")
        Log.e("birthDay", "getDataAndEdit: $birthDay")
        Log.e("avatar", "getDataAndEdit: $avatar")
        Log.e("chatLink", "getDataAndEdit: $chatLink")
        userViewModel.editInfoUser(
            token,
            fullName!!,
            gender,
            birthDay,
            address!!,
            phoneNumber!!,
            chatLink,
            avatar,
            password!!,
            "ACTIVE"
        ).observe(this) {
            it?.let {
                when(it.status){
                    ERROR ->   {
                        DialogAsk(this,"Thất bại","Vui lòng kiểm tra lại dữ liệu nhập")
                    }
                    SUCCESS -> {
                        if (it.data?.code() == 200){
                            Toast.makeText(this, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show()
                            loadingDialog.dismissDialog()
                            startActivity(Intent(this,UserActivity::class.java))
                            finishAffinity()
                        }
                        loadingDialog.dismissDialog()
                    }
                    LOADING -> {
                        loadingDialog.startLoadingDialog()
                    }
                }
            }

        }
    }
}