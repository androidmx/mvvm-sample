package com.gigigo.mvvm.viewmodel

import android.arch.lifecycle.*
import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.persistence.room.Room
import com.gigigo.mvvm.App
import com.gigigo.mvvm.ErrorHandling
import com.gigigo.mvvm.data.remote.RestService
import com.gigigo.mvvm.data.repository.UserRepository
import com.gigigo.mvvm.data.repository.UserRepositoryImp
import com.gigigo.mvvm.data.room.UserDataBase
import com.gigigo.mvvm.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersViewModel: ViewModel(), LifecycleObserver {
    private val userDataBase: UserDataBase = Room.databaseBuilder(App.applicationContext(), UserDataBase::class.java, "mvvm-database").build()
    private val userRepository: UserRepository = UserRepositoryImp(RestService.create(), userDataBase.userDao())


    private val compositeDisposable = CompositeDisposable()

    val error: MutableLiveData<Throwable> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getListUsers(page: Int, perPage: Int): LiveData<List<User>> {
        isLoading.value = true
        val mutableLiveData = MutableLiveData<List<User>>()
        val disposable = userRepository.getListUsers(page, perPage)
                .onErrorResumeNext(ErrorHandling<List<User>>(DefaultError::class.java))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableLiveData.value = it
                    isLoading.value = false
                }, {
                    e: Throwable? -> error.value = e
                    isLoading.value = false
                })

        compositeDisposable.add(disposable)

        return mutableLiveData
    }

    @Suppress("MemberVisibilityCanPrivate")
    @OnLifecycleEvent(ON_DESTROY)
    fun dispose() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    class DefaultError: ErrorHandling.ResponseError {
        var error:String? = null

        override fun hasErrorMessage(): Boolean {
            return error != null
        }

        override fun getErrorMessage(): String {
            return error.toString()
        }
    }
}