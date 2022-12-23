package com.quannm18.coolmateapp.view.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.hbb20.CountryCodePicker
import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_info_edit.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class InfoEditActivity : BaseActivity() {
    private lateinit var loadingDialog: LoadingDialog
    private var chatLink = ""
    private var gender = ""
    private var birthday = ""
    private lateinit var i: Intent
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var radioButton: RadioButton
    private lateinit var auth: FirebaseAuth
    private lateinit var codeSend: String
    private lateinit var countryCode: String
    private val ccp: CountryCodePicker by lazy { findViewById<CountryCodePicker>(R.id.ccp) }
    private val userViewModel: UserViewModel by viewModels()

    override fun layoutID(): Int = R.layout.activity_info_edit
    override fun initData() {
        //khoi tao tao data ,truoc khi khoi tao view
        Hawk.init(this).build()
        initDatePicker()
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("vi")
        gender = "NAM"
        Hawk.put("GENDER", gender)
        ccp.setCountryForPhoneCode(84)
        ccp.registerCarrierNumberEditText(edtPhone)
        // observeInputPhoneCode()
    }

    override fun initView() {
        //Khoi tao view
        marginStatusBar(listOf(imgBack))
        marginNavigationBar(listOf(btnEditUser))
        loadingDialog = LoadingDialog(this)
    }

    override fun listenLiveData() {
        //lang nghe su livedata

    }

    override fun listeners() {
        //dung su kien
        btnEditUser.setOnClickListener {
            validate()
        }
        imgBack.setOnClickListener {
            finish()
        }
        selectRadio()
        tilBirthday.editText?.setOnClickListener {
            datePickerDialog.show()
        }
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun initDatePicker() {
        val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            month + 1
            val date = makeDateString(dayOfMonth, month, year)
            tilBirthday.editText?.setText(date)
            val datePush = makeDateStringFormart(dayOfMonth, month, year)
            Hawk.put("BIRTHDAY", datePush)
            val br = "" + Hawk.get("BIRTHDAY")
            Log.e("TAG", "initDatePicker: ${br}")
        }
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        datePickerDialog = DatePickerDialog(this, date, year, month, day)
    }

    private val sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    private fun makeDateString(dayOfMonth: Int, month: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return sdf.format(calendar.time)
    }

    private val formart: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private fun makeDateStringFormart(dayOfMonth: Int, month: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return formart.format(calendar.time)
    }


    private fun selectRadio() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rdoNu) {
                gender = "NU"
                Hawk.put("GENDER", gender)
                Toast.makeText(this@InfoEditActivity, gender, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun validate() {
        val fullname = tilUserName.editText?.text.toString()
        val password = tilPassword.editText?.text.toString()
        val phoneNumber = tilPhoneNumber.editText?.text.toString()
        val address = tilAddress.editText?.text.toString()
        val birthday = tilBirthday.editText?.text.toString()

        if (fullname.isEmpty()) {
            tilUserName.isErrorEnabled = true
            tilUserName.error = "Không được để trống"
            return
        } else {
            if (fullname.length < 4) {
                tilUserName.isErrorEnabled = true
                tilUserName.error = "Họ tên tối thiểu 4 ký tự"
                return
            } else {
                tilUserName.isErrorEnabled = false
            }
        }
        if (birthday.isEmpty()) {
            tilBirthday.isErrorEnabled = true
            tilBirthday.error = "Không được để trống"
            return
        } else {
            tilBirthday.isErrorEnabled = false
        }
        if (password.trim().isEmpty()) {
            tilPassword.isErrorEnabled = true
            tilPassword.error = "Không được để trống"
            return
        } else {
            if (password.trim() != Hawk.get("PASSWORD")) {
                tilPassword.isErrorEnabled = true
                tilPassword.error = "Sai mật khẩu đăng nhập"
                return
            } else {
                tilPassword.isErrorEnabled = false
            }
        }
        if (phoneNumber.isEmpty()) {
            tilPhoneNumber.isErrorEnabled = true
            tilPhoneNumber.error = "Không được để trống"
            return
        } else {
            tilPhoneNumber.isErrorEnabled = false

        }
        if (address.trim().isEmpty()) {
            tilAddress.isErrorEnabled = true
            tilAddress.error = "Không được để trống"
            return
        } else {
            tilAddress.isErrorEnabled = false
        }

        ccp.setPhoneNumberValidityChangeListener { isValidate ->
            if (isValidate) {
                tilPhoneNumber.isErrorEnabled = false
                loadingDialog.startLoadingDialog()
                verifyPhoneNumber("+84" + tilPhoneNumber.editText?.text.toString())
            } else {
                tilPhoneNumber.isErrorEnabled = true
                tilPhoneNumber.error = "Sai định dạng số điện thoại"
                return@setPhoneNumberValidityChangeListener
            }
        }
    }

    private fun observeInputPhoneCode() {
        tilPhoneNumber.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                ccp.setPhoneNumberValidityChangeListener { isValidate ->
                    if (isValidate) {
                        tilPhoneNumber.isErrorEnabled = true
                        tilPhoneNumber.error = "Sai định dạng số điện thoại"
                    } else {
                        tilPhoneNumber.isErrorEnabled = false
                    }
                }
            }

        })
    }

    private fun checkPhoneCode() {

    }

    private fun onClickConfirmCode(otp: String) {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(codeSend, otp)
        signInWithPhoneAuthCredential(credential)
    }


    private fun verifyPhoneNumber(phone: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@InfoEditActivity)
            // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@InfoEditActivity, "failed", Toast.LENGTH_SHORT).show()
                    Log.e("EXXXX", "onVerificationFailed: $p0")
                }

                override fun onCodeSent(
                    verifyID: String,
                    p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verifyID, p1)
                    codeSend = verifyID

                    gotoNextActivity("+84${tilPhoneNumber.editText?.text?.substring(1)}", verifyID)
                    loadingDialog.dismissDialog()

                }
            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@InfoEditActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")

                    val user = task.result?.user
                    Toast.makeText(this, "Thanh cong ${user?.phoneNumber}", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this@InfoEditActivity, "fail", Toast.LENGTH_SHORT).show()
                        Log.e("ERROR", "signInWithPhoneAuthCredential: ${task.exception}")
                    }
                    // Update UI
                }
            }
    }

    private fun gotoNextActivity(phone: String, verifyID: String) {
        val fullName = tilUserName.editText?.text.toString()
        val password = tilPassword.editText?.text.toString()
        val address = tilAddress.editText?.text.toString()
        Log.e("TAG", "gotoNextActivity: $phone")
//        i = Intent(this, VerifyActivity::class.java)
        i.putExtra("FULLNAME", fullName)
        i.putExtra("PASSWORD", password)
        i.putExtra("PHONE", phone)
        i.putExtra("ADDRESS", address)
        i.putExtra("verification_id", verifyID)
        startActivity(i)
    }

}