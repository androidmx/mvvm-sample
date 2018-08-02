package com.gigigo.mvvm.data.remote

import com.google.gson.annotations.SerializedName

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
object UserModel {
    data class Data(@SerializedName("id") val id: Int,
                    @SerializedName("first_name") val firstName: String,
                    @SerializedName("last_name") val lastName: String,
                    @SerializedName("avatar") val avatar: String)

    data class SingleUser(@SerializedName("data") val data: Data)

    data class ListUsers(
            @SerializedName("page") val page: Int,
            @SerializedName("per_page") val perPage: Int,
            @SerializedName("total") val total: Int,
            @SerializedName("total_pages") val totalPages: Int,
            @SerializedName("data") val data: List<Data>)
}