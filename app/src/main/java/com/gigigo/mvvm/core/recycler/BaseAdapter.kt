package com.gigigo.mvvm.core.recycler

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class BaseAdapter<T: Any>:
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: MutableList<T> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false),
                viewType)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<T>).bind(data[position])
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        data.add(item)
        notifyItemInserted(data.size)
    }

    fun addAll(items: Collection<T>) {
        val previousPosition = data.size
        data.addAll(items)
        notifyItemRangeInserted(previousPosition, items.size)
    }

    @LayoutRes
    protected abstract fun getLayoutId(position: Int, item: T): Int

    protected abstract fun getViewHolder(container: View, viewType: Int): RecyclerView.ViewHolder

}