package com.gigigo.mvvm.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.gigigo.mvvm.data.remote.RestService
import com.gigigo.mvvm.data.remote.UserModel
import com.gigigo.mvvm.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class UserRepositoryImp(private val restService: RestService): UserRepository {

    private val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    override fun getAllCompositeDisposables(): List<Disposable> {
        return allCompositeDisposable
    }

    override fun getListUsers(page: Int, perPage: Int): LiveData<List<User>> {
        val mutableLiveData = MutableLiveData<List<User>>()

        val disposable = restService.getListUsers(page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listUsers: UserModel.ListUsers ->
                    mutableLiveData.value = collectionTransform(listUsers)
                }, { throwable: Throwable? -> throwable?.printStackTrace() })

        allCompositeDisposable.add(disposable)

        return mutableLiveData
    }

    override fun getSingleUser(userId: Int): LiveData<User> {
        val mutableLiveData = MutableLiveData<User>()


        return mutableLiveData
    }

    private fun transform(data: UserModel.Data): User {
        return User(data.id,
                data.firstName + " " + data.lastName,
                data.avatar)
    }

    private fun collectionTransform(listUsers: UserModel.ListUsers): List<User> {
        val users = ArrayList<User>()
        listUsers.data.forEach {
            users.add(transform(it))
        }

        return users
    }
}