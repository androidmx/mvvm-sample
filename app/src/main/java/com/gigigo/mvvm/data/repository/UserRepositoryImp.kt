package com.gigigo.mvvm.data.repository

import com.gigigo.mvvm.data.remote.RestService
import com.gigigo.mvvm.data.remote.UserModel
import com.gigigo.mvvm.data.room.UserDao
import com.gigigo.mvvm.data.room.UserEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class UserRepositoryImp(private val restService: RestService, private val userDao: UserDao): UserRepository {

    override fun getListUsers(page: Int, perPage: Int): Observable<List<UserEntity>> {
//        return Observable.concatArray(
//                getUsersFromApi(page, perPage)
//                /*getUsersFromDB()*/).distinct()

//        return getUsersFromApi(page, perPage).mergeWith(getUsersFromDB())
//                .collect({ HashSet<Int>() }, { set, v-> set.add(v) })
//                .flatMap(???({ Observable.from() }))

        return getUsersFromApi(page, perPage)

    }

    override fun getSingleUser(userId: Int): UserEntity {
        return UserEntity(0, "", "", "")
    }

    private fun getUsersFromDB(): Observable<List<UserEntity>> {
        return userDao.getListUsers().filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Timber.d("Dispatching ${it.size} users from DB...")
                }
    }

    private fun getUsersFromApi(page: Int, perPage: Int): Observable<List<UserEntity>> {
        return restService.getListUsers(page, perPage)
                .map { o:UserModel.ListUsers? ->
                    return@map collectionTransform(o!!)
                }
                .doOnNext {
                    Timber.d("Dispatching ${it?.size} users from API...")
                    storeUsersInDB(it)
                }
    }

    private fun storeUsersInDB(users: List<UserEntity>) {
        Observable.fromCallable { userDao.insertAll(users) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Timber.d("Inserted ${users.size} users from API in DB...")
                }
    }



    private fun transform(data: UserModel.Data): UserEntity {
        return UserEntity(data.id,
                data.firstName,
                data.lastName,
                data.avatar)
    }

    private fun collectionTransform(listUsers: UserModel.ListUsers?): List<UserEntity> {
        val users = ArrayList<UserEntity>()
        listUsers?.data?.forEach {
            users.add(transform(it))
        }

        return users
    }

}