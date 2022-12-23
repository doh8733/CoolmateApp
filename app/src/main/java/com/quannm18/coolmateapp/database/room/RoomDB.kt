package com.quannm18.coolmateapp.database.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.quannm18.coolmateapp.database.dao.CartDao
import com.quannm18.coolmateapp.database.dao.UserDao
import com.quannm18.coolmateapp.model.cart.Cart
import com.quannm18.coolmateapp.model.cart.CartProduct
import com.quannm18.coolmateapp.model.cart.Products
import com.quannm18.coolmateapp.model.user.UserInfo

@Database(
    entities = [UserInfo::class, CartProduct::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao

    companion object {
        private const val DATABASE_NAME = "coolmate_app"
        private var instance: RoomDB? = null
        fun getInstance(application: Application): RoomDB {
            synchronized(RoomDB::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application,
                        RoomDB::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance!!
        }
    }

}