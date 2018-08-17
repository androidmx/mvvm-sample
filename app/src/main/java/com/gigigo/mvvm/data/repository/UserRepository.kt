package com.gigigo.mvvm.data.repository

import com.gigigo.mvvm.data.room.UserEntity
import io.reactivex.Observable

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
interface UserRepository {
    fun getListUsers(page: Int, perPage: Int): Observable<List<UserEntity>>
    fun getSingleUser(userId: Int): UserEntity
}