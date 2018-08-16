package com.gigigo.mvvm

import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.HttpException
import java.net.HttpURLConnection
import com.google.gson.Gson
import io.reactivex.Observable
import java.lang.reflect.Type
import java.net.SocketTimeoutException


/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ErrorHandling<T>(private val type: Type?): Function<Throwable, ObservableSource<T>> {

    var httpMapCodes = HashMap<Int, String>()

    init {
        /* 2XX: generally "OK" */

        // HTTP Status-Code 200: OK.
        httpMapCodes.put(HttpURLConnection.HTTP_OK, "OK")

        // HTTP Status-Code 201: Created.
        httpMapCodes.put(HttpURLConnection.HTTP_CREATED, "Created")

        // HTTP Status-Code 202: Accepted.
        httpMapCodes.put(HttpURLConnection.HTTP_ACCEPTED, "Accepted")

        // HTTP Status-Code 203: Non-Authoritative Information.
        httpMapCodes.put(HttpURLConnection.HTTP_NOT_AUTHORITATIVE, "Non-Authoritative Information")

        // HTTP Status-Code 204: No Content.
        httpMapCodes.put(HttpURLConnection.HTTP_NO_CONTENT, "No Content")

        // HTTP Status-Code 205: Reset Content.
        httpMapCodes.put(HttpURLConnection.HTTP_RESET, "Reset Content")

        // HTTP Status-Code 206: Partial Content.
        httpMapCodes.put(HttpURLConnection.HTTP_PARTIAL, "Partial Content")

        /* 3XX: relocation/redirect */

        // HTTP Status-Code 300: Multiple Choices.
        httpMapCodes.put(HttpURLConnection.HTTP_MULT_CHOICE, "Multiple Choices")

        // HTTP Status-Code 301: Moved Permanently.
        httpMapCodes.put(HttpURLConnection.HTTP_MOVED_PERM, "Moved Permanently")

        // HTTP Status-Code 302: Temporary Redirect.
        httpMapCodes.put(HttpURLConnection.HTTP_MOVED_TEMP, "Temporary Redirect")

        // HTTP Status-Code 303: See Other.
        httpMapCodes.put(HttpURLConnection.HTTP_SEE_OTHER, "See Other")

        // HTTP Status-Code 304: Not Modified.
        httpMapCodes.put(HttpURLConnection.HTTP_NOT_MODIFIED, "Not Modified")

        // HTTP Status-Code 305: Use Proxy.
        httpMapCodes.put(HttpURLConnection.HTTP_USE_PROXY, "Use Proxy")

        /* 4XX: client error */

        // HTTP Status-Code 400: Bad Request.
        httpMapCodes.put(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request")

        // HTTP Status-Code 401: Unauthorized.
        httpMapCodes.put(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized")

        // HTTP Status-Code 402: Payment Required.
        httpMapCodes.put(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "Payment Required")

        // HTTP Status-Code 403: Forbidden.
        httpMapCodes.put(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden")

        // HTTP Status-Code 404: Not Found.
        httpMapCodes.put(HttpURLConnection.HTTP_NOT_FOUND, "Not Found")

        // HTTP Status-Code 405: Method Not Allowed.
        httpMapCodes.put(HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed")

        // HTTP Status-Code 406: Not Acceptable.
        httpMapCodes.put(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "Not Acceptable")

        // HTTP Status-Code 407: Proxy Authentication Required.
        httpMapCodes.put(HttpURLConnection.HTTP_PROXY_AUTH, "Proxy Authentication Required")

        // HTTP Status-Code 408: Request Time-Out.
        httpMapCodes.put(HttpURLConnection.HTTP_CLIENT_TIMEOUT, "Request Time-Out")

        // HTTP Status-Code 409: Conflict.
        httpMapCodes.put(HttpURLConnection.HTTP_CONFLICT, "Conflict")

        // HTTP Status-Code 410: Gone.
        httpMapCodes.put(HttpURLConnection.HTTP_GONE, "Gone")

        // HTTP Status-Code 411: Length Required.
        httpMapCodes.put(HttpURLConnection.HTTP_LENGTH_REQUIRED, "Length Required")

        // HTTP Status-Code 412: Precondition Failed.
        httpMapCodes.put(HttpURLConnection.HTTP_PRECON_FAILED, "Precondition Failed")

        // HTTP Status-Code 413: Request Entity Too Large.
        httpMapCodes.put(HttpURLConnection.HTTP_ENTITY_TOO_LARGE, "Request Entity Too Large")

        // HTTP Status-Code 414: Request-URI Too Large.
        httpMapCodes.put(HttpURLConnection.HTTP_REQ_TOO_LONG, "Request-URI Too Large")

        // HTTP Status-Code 415: Unsupported Media Type.
        httpMapCodes.put(HttpURLConnection.HTTP_UNSUPPORTED_TYPE, "Unsupported Media Type")

        /* 5XX: server error */

        // HTTP Status-Code 500: Internal Server Error.
        httpMapCodes.put(HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error")

        // HTTP Status-Code 501: Not Implemented.
        httpMapCodes.put(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not Implemented")

        // HTTP Status-Code 502: Bad Gateway.
        httpMapCodes.put(HttpURLConnection.HTTP_BAD_GATEWAY, "Bad Gateway")

        // HTTP Status-Code 503: Service Unavailable.
        httpMapCodes.put(HttpURLConnection.HTTP_UNAVAILABLE, "Unavailable")

        // HTTP Status-Code 504: Gateway Timeout.
        httpMapCodes.put(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "Gateway Timeout")

        // HTTP Status-Code 505: HTTP Version Not Supported.
        httpMapCodes.put(HttpURLConnection.HTTP_VERSION, "HTTP Version Not Supported")
    }

    companion object {
        const val HTTP_DEFAULT = "The specific HTTP request has been interrupted"
    }

    @Throws(Exception::class)
    override fun apply(t: Throwable): ObservableSource<T> {
        when(t) {
            is HttpException -> {
                if(t.response() != null && t.response().errorBody() != null) {
                    val errorBody = t.response().errorBody()!!.string()
                    return Observable.error(getResponseState(errorBody, t.response().code()))
                }
            }
            is SocketTimeoutException -> {

            }
            is NullPointerException -> {

            }
        }

        return Observable.error(t)
    }

    private fun getResponseState(errorBody: String?, code: Int) : Exception {
        if(errorBody != null && (errorBody.startsWith("{") || errorBody.startsWith("["))) {
            val gson = Gson()
            var responseError: ResponseError?

            responseError = try {
                gson.fromJson(errorBody, type)
            } catch (e: Exception) {
                null
            }

            if(responseError == null) {
                return getErrorByHttpCode(code)
            } else {
                return Exception(responseError.getErrorMessage())
            }
        }

        return getErrorByHttpCode(code)
    }

    private fun getErrorByHttpCode(code: Int) : Exception {
        if(httpMapCodes.containsKey(code)) {
            return Exception("HTTP $code - ${httpMapCodes[code]}")
        }

        return Exception(HTTP_DEFAULT)
    }

    interface ResponseError {
        fun getErrorMessage(): String
        fun hasErrorMessage(): Boolean
    }
}