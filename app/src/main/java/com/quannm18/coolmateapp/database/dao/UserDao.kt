package com.quannm18.coolmateapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quannm18.coolmateapp.model.user.UserInfo

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userInfo: UserInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userList: List<UserInfo>)

    @Query("SELECT * FROM user_table")
    fun selectAll(): LiveData<List<UserInfo>>

    @Query("select * from user_table where id = :id")
    fun getUserByID(id: String): LiveData<UserInfo>
}