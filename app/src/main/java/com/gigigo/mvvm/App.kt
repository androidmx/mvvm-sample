package com.gigigo.mvvm

import android.app.Application
import android.content.Context
import android.support.annotation.Nullable
import android.util.Log
import android.util.Log.INFO
import com.facebook.stetho.Stetho
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * @author JG - August 16, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class App : Application() {

    init {
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
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


    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
    }

    /** A tree which logs important information for crash reporting.  */
    class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            FakeCrashLibrary.log(priority, tag!!, message)

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t)
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t)
                }
            }
        }

        fun isLoggable(priority: Int, @Nullable tag: String): Boolean {
            return priority >= INFO
        }

    }

    /** Not a real crash reporting library!  */
    class FakeCrashLibrary private constructor() {

        init {
            throw AssertionError("No instances.")
        }

        companion object {
            fun log(priority: Int, tag: String, message: String) {
                // TODO add log entry to circular buffer.
            }

            fun logWarning(t: Throwable) {
                // TODO report non-fatal warning.
            }

            fun logError(t: Throwable) {
                // TODO report non-fatal error.
            }
        }
    }
}