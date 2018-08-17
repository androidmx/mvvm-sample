package com.gigigo.mvvm.data.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity(tableName = "users")
class UserEntity(@PrimaryKey(autoGenerate = false) val id: Int = 0,
                 @ColumnInfo(name = "first_name") val firstName: String,
                 @ColumnInfo(name = "last_name") val lastName: String,
                 @ColumnInfo(name = "avatar") val avatar: String) {

    fun getFullName(): String {
        return "$lastName $lastName"
    }
}