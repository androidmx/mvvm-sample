package com.gigigo.mvvm.data.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun insert(userEntity: UserEntity)

    @Insert(onConflict = REPLACE)
    fun insertAll(userEntity: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getListUsers(): Flowable<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getSingleUser(userId: String): Flowable<UserEntity>
}