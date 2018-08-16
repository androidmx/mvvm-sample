package com.gigigo.mvvm.data.remote

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import java.io.IOException
import java.net.InetAddress

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class DefaultNetwork(context: Context) : BroadcastReceiver(), NetworkRequestInterceptor.Network {

    private val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun onReceive(context: Context?, intent: Intent?) { }

    override fun isConnected(): Boolean {
        return connectivityManager.activeNetworkInfo != null
                && connectivityManager.activeNetworkInfo.isConnectedOrConnecting
    }

    override fun connectivityType(): Int {
        return if(connectivityManager.activeNetworkInfo != null)
            connectivityManager.activeNetworkInfo.type else TYPE_NONE
    }

    override fun isReachable(host: String, timeout: Int): Boolean {
        if(!isConnected()) return false

        var reachable: Boolean = false

        try {
            reachable = InetAddress.getByName(host).isReachable(timeout)
        } catch (e: IOException) {
            Log.e("", Log.getStackTraceString(e))
        }

        return reachable
    }

    companion object {
        const val TYPE_NONE = -1
    }
}