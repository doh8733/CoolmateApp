package com.quannm18.coolmateapp.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

class ValidateData {
    companion object{
        fun String.isValidEmail() =
            !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

        private val VALID_NAME_VIETNAM_REGEX : Pattern =
            Pattern.compile("^[a-zA-Z_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêếìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\ ]+\$",Pattern.CASE_INSENSITIVE)

        fun String.validateEmail(): Boolean {
            val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this)
            return matcher.find()
        }
        fun String.validateName(): Boolean {
            val matcher: Matcher = VALID_NAME_VIETNAM_REGEX.matcher(this)
            return matcher.find()
        }
    }
}