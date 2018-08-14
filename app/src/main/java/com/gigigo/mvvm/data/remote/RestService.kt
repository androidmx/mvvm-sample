package com.gigigo.mvvm.data.remote

import com.gigigo.mvvm.BuildConfig
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
interface RestService {

    @GET("/api/users/")
    fun getListUsers(@Query("page") page: Int, @Query("per_page") perPage: Int): Observable<UserModel.ListUsers>

    @GET("/api/users/{userId}")
    fun getSingleUser(@Path("userId") userId: Int): Single<UserModel.SingleUser>

    companion object {
        fun create(): RestService {
            val loggingInterceptor = LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .build()

            val client = OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()


            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://reqres.in/") // todo August 01, 2018: change for BuildConfig.Host
                    .build()

            return retrofit.create(RestService::class.java)
        }
    }


}