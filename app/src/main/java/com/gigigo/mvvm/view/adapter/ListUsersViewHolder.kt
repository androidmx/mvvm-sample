package com.gigigo.mvvm.view.adapter

import android.view.View
import com.gigigo.mvvm.core.recycler.BaseViewHolder
import com.gigigo.mvvm.data.room.UserEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_user_item.view.*

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersViewHolder(override val container: View):
        BaseViewHolder<UserEntity>(container) {

    override fun bind(item: UserEntity) {
        super.bind(item)

        with(container) {
            text_view_name.text = item.getFullName()
            Picasso.get()
                    .load(item.avatar)
                    .resize(80, 80)
                    .centerCrop()
                    .into(image_view_avatar)
        }
    }
}