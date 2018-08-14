package com.gigigo.mvvm.viewmodel

import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import com.gigigo.mvvm.data.remote.RestService
import com.gigigo.mvvm.data.repository.BaseRepository
import com.gigigo.mvvm.data.repository.UserRepository
import com.gigigo.mvvm.data.repository.UserRepositoryImp
import com.gigigo.mvvm.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersViewModel: ViewModel(), LifecycleObserver {
    private var userRepository: UserRepository = UserRepositoryImp(RestService.create(),
            Schedulers.io(),
            AndroidSchedulers.mainThread())

    fun getListUsers(page: Int, perPage: Int): LiveData<List<User>> {
        return userRepository.getListUsers(page, perPage)
    }

    @Suppress("MemberVisibilityCanPrivate")
    @OnLifecycleEvent(ON_DESTROY)
    fun dispose() {
        (userRepository as BaseRepository).dispose()
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }
}