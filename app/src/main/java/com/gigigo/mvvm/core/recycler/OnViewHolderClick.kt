package com.gigigo.mvvm.core.recycler

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
interface OnViewHolderClick<in T: Any> {
    fun onItemClick(item: T, position: Int)
}