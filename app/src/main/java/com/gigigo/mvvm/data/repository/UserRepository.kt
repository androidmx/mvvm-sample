package com.gigigo.mvvm.data.repository

import android.arch.lifecycle.LiveData
import com.gigigo.mvvm.model.User
import io.reactivex.disposables.Disposable

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
interface UserRepository {
    fun getAllCompositeDisposables(): List<Disposable>
    fun getListUsers(page: Int, perPage: Int): LiveData<List<User>>
    fun getSingleUser(userId: Int): LiveData<User>
}