package com.gigigo.mvvm.data.repository

import com.gigigo.mvvm.model.User
import io.reactivex.Observable

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
interface UserRepository {
    fun getListUsers(page: Int, perPage: Int): Observable<List<User>>
    fun getSingleUser(userId: Int): User
}