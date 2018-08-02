package com.gigigo.mvvm.core.recycler

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class BaseViewHolder<T: Any>(open val container: View)
    : RecyclerView.ViewHolder(container), Binder<T>, View.OnClickListener {

    protected lateinit var item: T
    lateinit var itemClickListener: OnViewHolderClick<T>

    open fun setClickListener(listener: OnViewHolderClick<T>) {
        this.container.setOnClickListener(this)
        this.itemClickListener = listener
    }

    override fun bind(item: T) {
        this.item = item
    }

    override fun onClick(view: View?) {
        itemClickListener.onItemClick(item, adapterPosition)
    }
}