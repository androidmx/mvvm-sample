package com.gigigo.mvvm.data.repository

import com.gigigo.mvvm.data.remote.RestService
import com.gigigo.mvvm.data.remote.UserModel
import com.gigigo.mvvm.data.room.UserDao
import com.gigigo.mvvm.model.User
import io.reactivex.Observable
import timber.log.Timber

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class UserRepositoryImp(private val restService: RestService, private val userDao: UserDao): UserRepository {

    override fun getListUsers(page: Int, perPage: Int): Observable<List<User>> {
        return Observable.concatArray(getUsersFromApi(page, perPage))
    }

    override fun getSingleUser(userId: Int): User {
        return User(0, "", "")
    }


    private fun getUsersFromApi(page: Int, perPage: Int): Observable<List<User>> {
        return restService.getListUsers(page, perPage)
                .map { o:UserModel.ListUsers? ->
                    return@map collectionTransform(o!!)
                }
                .doOnNext {
                    Timber.d("Dispatching ${it?.size} users from API...")
                }
    }


    private fun transform(data: UserModel.Data): User {
        return User(data.id,
                data.firstName + " " + data.lastName,
                data.avatar)
    }

    private fun collectionTransform(listUsers: UserModel.ListUsers?): List<User> {
        val users = ArrayList<User>()
        listUsers?.data?.forEach {
            users.add(transform(it))
        }

        return users
    }

}