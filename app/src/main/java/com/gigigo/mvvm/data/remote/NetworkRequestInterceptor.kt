package com.gigigo.mvvm.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class NetworkRequestInterceptor(private var network: Network) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        if(network.isConnected()) {
            return interceptResponse(chain!!)
        } else {
            throw NetworkException(messageNetworkException())
        }
    }

    @Throws(IOException::class)
    abstract fun interceptResponse(chain: Interceptor.Chain): Response

    open fun messageNetworkException(): String {
        return "Parece que su conexión a Internet está desactivada." +
                "\n\nPor favor, enciéndala y vuelva a intentarlo."
    }

    interface Network {
        fun isConnected(): Boolean
        fun connectivityType(): Int
        fun isReachable(host: String, timeout: Int): Boolean
    }

    class NetworkException(message: String): IOException(message)
}