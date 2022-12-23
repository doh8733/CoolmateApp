package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quannm18.coolmateapp.model.address.Address
import com.quannm18.coolmateapp.model.address.CityProvince
import com.quannm18.coolmateapp.model.address.CommuneWard
import com.quannm18.coolmateapp.model.address.District
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class GSonViewModel(application: Application) :
    AndroidViewModel(application) {


    fun getCity(context: Context): MutableList<CityProvince> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("address/tinh_tp.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listCity = object : TypeToken<MutableList<CityProvince>>() {}.type
        return Gson().fromJson(jsonString, listCity)
    }

    fun getCommune(context: Context): MutableList<CommuneWard> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("address/xa_phuong.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listCity = object : TypeToken<MutableList<CommuneWard>>() {}.type
        return Gson().fromJson(jsonString, listCity)
    }

    fun getDistricts(context: Context): MutableList<District> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("address/quan_huyen.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listCity = object : TypeToken<MutableList<District>>() {}.type
        return Gson().fromJson(jsonString, listCity)
    }

    suspend fun getListCityAddress(): MutableList<Address> {
        return withContext(Dispatchers.IO) {
            val mCityList: MutableList<CityProvince> =
                getCity(getApplication<Application>().applicationContext)
            val mAddressCitys = mutableListOf<Address>()
            mCityList.map {
                mAddressCitys.add(Address(type = "0", code = it.code, it.name_with_type, it.name))
            }
            mAddressCitys
        }
    }

    suspend fun getListCommuneAddress(code: String): MutableList<Address> {
        return withContext(Dispatchers.IO) {
            val mCityList: MutableList<CommuneWard> =
                getCommune(getApplication<Application>().applicationContext).filter { it.parent_code == code }
                    .toMutableList()
            val mAddressCommune = mutableListOf<Address>()
            mCityList.map {
                mAddressCommune.add(Address(type = "2", code= it.code, it.path_with_type, it.name))
            }
            mAddressCommune
        }
    }

    suspend fun getListDistrictAddress(code: String): MutableList<Address> {
        return withContext(Dispatchers.IO) {
            val mCityList: MutableList<District> =
                getDistricts(getApplication<Application>().applicationContext).filter { it.parent_code == code }
                    .toMutableList()
            val mAddressCommune = mutableListOf<Address>()
            mCityList.map {
                mAddressCommune.add(Address(type = "1", code=it.code, it.name_with_type, it.name))
            }
            mAddressCommune
        }
    }
}