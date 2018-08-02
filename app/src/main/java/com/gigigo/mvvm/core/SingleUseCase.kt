package com.gigigo.mvvm.core

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver

/**
 * @author JG - August 01, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class SingleUseCase<T, in P>(threadExecutor: Scheduler, uiThread: Scheduler):
        UseCase(threadExecutor, uiThread) {

    fun execute(observer: DisposableSingleObserver<T>, parameters: P) {
        val observable: Single<T> = createObservableUseCase(parameters)
                .subscribeOn(threadExecutor)
                .observeOn(uiThread)

        allCompositeDisposable.add(observable.subscribeWith(observer))
    }

    abstract fun createObservableUseCase(parameters: P):Single<T>
}