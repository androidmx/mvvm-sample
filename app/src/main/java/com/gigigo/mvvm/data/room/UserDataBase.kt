package com.gigigo.mvvm.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class UserDataBase
    : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: UserDataBase? = null

        fun getInstance(context: Context): UserDataBase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        UserDataBase::class.java, "Sample.db")
                        .build()
    }
}