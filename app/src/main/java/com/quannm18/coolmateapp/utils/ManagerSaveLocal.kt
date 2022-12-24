package com.quannm18.coolmateapp.utils

import com.orhanobut.hawk.Hawk
import com.quannm18.coolmateapp.model.user.UserLogin

class ManagerSaveLocal {
    companion object {
        const val PAYMENT = "Payment"
        const val ADDRESS = "Address"
        const val LOGIN = "UsernameAndPassword"
        const val VOUCHER = "Voucher"
        const val CART_ID = "CartID"
        const val ENABLE_NOTIFICATION = "EnableNotification"
        const val CHAT_LINK = "ChatUrl"
        const val PASS ="PASSWORD"
    }

    /**
     * Save
     */

    fun savePaymentMethod(isPayment: Int) {
        Hawk.put(PAYMENT, isPayment)
    }

    fun saveChatLink(chatLink: String) {
        Hawk.put(CHAT_LINK, chatLink)
    }

    fun saveAddress(address: String) {
        Hawk.put(ADDRESS, address)
    }

    fun saveLogin(userLogin: UserLogin) {
        Hawk.put(LOGIN, userLogin)
    }

    fun saveVoucher(voucher: String) {
        Hawk.put(VOUCHER, voucher)
    }

    fun saveCartID(cartId: String) {
        Hawk.put(CART_ID, cartId)
    }

    fun saveEnableNotification(isEnable: Boolean) {
        Hawk.put(ENABLE_NOTIFICATION, isEnable)
    }
    fun savePassword(password :String){
        Hawk.put(PASS,password)
    }

    /**
     * Get
     */
    fun getPaymentMethod(): Int {
        return Hawk.get<Int>(PAYMENT, -1)
    }

    fun getAddress(): String? {
        return Hawk.get<String>(ADDRESS, null)
    }

    fun getLogin(): UserLogin? {
        return Hawk.get<UserLogin>(LOGIN, null)
    }

    fun getVoucher(): String? {
        return Hawk.get<String>(VOUCHER, null)
    }

    fun getChatLink(): String? {
        return Hawk.get<String>(CHAT_LINK, null)
    }

    fun getCartID(): String? {
        return Hawk.get<String>(CART_ID)
    }

    fun getEnableNotification(): Boolean {
        return Hawk.get<Boolean>(ENABLE_NOTIFICATION, false)
    }
    fun getPassword():String{
        return Hawk.get(PASS)
    }

    /**
     * DELETE
     */

    fun deletePayment(): Boolean {
        return Hawk.delete(PAYMENT)
    }

    fun deleteChatLink(): Boolean {
        return Hawk.delete(CHAT_LINK)
    }

    fun deleteAddress(): Boolean {
        return Hawk.delete(ADDRESS)
    }

    fun deleteLogin(): Boolean {
        return Hawk.delete(LOGIN)
    }

    fun deleteVoucher(): Boolean {
        return Hawk.delete(VOUCHER)
    }

    fun deleteCartID(): Boolean {
        return Hawk.delete(CART_ID)
    }

    fun deleteEnableNotification(): Boolean {
        return Hawk.delete(ENABLE_NOTIFICATION)
    }
    fun deletePassword():Boolean{
        return Hawk.delete(PASS)
    }
}