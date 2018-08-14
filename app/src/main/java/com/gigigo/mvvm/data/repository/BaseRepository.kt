package com.gigigo.mvvm.data.repository

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

/**
 * @author JG - August 08, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class BaseRepository(private var threadExecutor: Scheduler,
                              private var uiThread: Scheduler) {

    private val compositeDisposable = CompositeDisposable()

    fun dispose() {
        compositeDisposable.clear()
    }

    fun <T> subscribe(observable: Observable<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        val disposable = observable
                .subscribeOn(threadExecutor)
                .observeOn(uiThread)
                .subscribe(onNext, onError)

        compositeDisposable.add(disposable)
    }

    fun <T> subscribe(single: Single<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        val disposable = single
                .subscribeOn(threadExecutor)
                .observeOn(uiThread)
                .subscribe(onNext, onError)

        compositeDisposable.add(disposable)
    }
}