package com.gigigo.mvvm

import android.app.Application
import android.content.Context

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        // The Application Context can be used to:
        // - load resources,
        // - send broadcast messages and do other system like stuff
        // NOT on instantiating Views
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}