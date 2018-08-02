package com.gigigo.mvvm.core

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class UseCase(protected var threadExecutor: Scheduler, protected var uiThread: Scheduler) {
    protected val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    fun dispose() {
        allCompositeDisposable.forEach {
            if(!it.isDisposed) it.dispose()
        }
    }
}