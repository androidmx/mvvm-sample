package com.gigigo.mvvm.viewmodel

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import com.gigigo.mvvm.data.remote.RestService
import com.gigigo.mvvm.data.repository.UserRepository
import com.gigigo.mvvm.data.repository.UserRepositoryImp
import com.gigigo.mvvm.model.User
import io.reactivex.disposables.CompositeDisposable

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersViewModel: ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()
    private var userRepository: UserRepository = UserRepositoryImp(RestService.create())

    fun getListUsers(page: Int, perPage: Int): LiveData<List<User>> {
        return userRepository.getListUsers(page, perPage)
    }

    @OnLifecycleEvent(ON_DESTROY)
    private fun unSubscribeViewModel() {
        userRepository.getAllCompositeDisposables().forEach {
            compositeDisposable.addAll(it)
        }
        compositeDisposable.clear()
    }

    override fun onCleared() {
        unSubscribeViewModel()
        super.onCleared()
    }
}