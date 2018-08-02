package com.gigigo.mvvm.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.gigigo.mvvm.R
import com.gigigo.mvvm.core.recycler.BaseAdapter
import com.gigigo.mvvm.core.recycler.OnViewHolderClick
import com.gigigo.mvvm.model.User
import com.gigigo.mvvm.view.toast

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersAdapter: BaseAdapter<User>() {

    override fun getLayoutId(position: Int, item: User): Int {
        return R.layout.layout_user_item
    }

    override fun getViewHolder(container: View, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = ListUsersViewHolder(container)
        viewHolder.setClickListener(object: OnViewHolderClick<User> {
            override fun onItemClick(item: User, position: Int) {
                container.context.toast(item.fullName, Toast.LENGTH_SHORT)
            }
        })
        return viewHolder
    }
}