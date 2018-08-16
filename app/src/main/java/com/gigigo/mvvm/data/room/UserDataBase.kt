package com.gigigo.mvvm.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class UserDataBase
    : RoomDatabase() {

    abstract fun userDao(): UserDao
}