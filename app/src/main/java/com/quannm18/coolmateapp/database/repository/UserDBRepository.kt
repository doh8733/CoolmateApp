package com.quannm18.coolmateapp.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.quannm18.coolmateapp.database.room.RoomDB
import com.quannm18.coolmateapp.model.user.UserInfo

class UserDBRepository(application: Application) {
    companion object {
        private var instance: UserDBRepository? = null
        fun newInstance(application: Application): UserDBRepository {
            if (instance == null) {
                instance = UserDBRepository(application)
            }
            return instance!!
        }
    }

    private val userDao = RoomDB.getInstance(application).userDao()

    fun insertUser(userInfo: UserInfo) {
        userDao.insertUser(userInfo)
    }

    fun insertUser(userInfoList: List<UserInfo>) {
        userDao.insertUser(userInfoList)
    }

    fun selectAll(): LiveData<List<UserInfo>> {
        return userDao.selectAll()
    }

    fun getUserByID(id: String): LiveData<UserInfo> {
        return userDao.getUserByID(id)
    }
}