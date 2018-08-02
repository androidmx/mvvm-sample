package com.gigigo.mvvm.core.recycler

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
internal interface Binder<in T: Any> {
    fun bind(item: T)
}