package com.gigigo.mvvm.view.adapter

import android.view.View
import com.gigigo.mvvm.core.recycler.BaseViewHolder
import com.gigigo.mvvm.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_user_item.view.*

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersViewHolder(override val container: View):
        BaseViewHolder<User>(container) {

    override fun bind(item: User) {
        super.bind(item)

        with(container) {
            text_view_name.text = item.fullName
            Picasso.get()
                    .load(item.avatar)
                    .resize(80, 80)
                    .centerCrop()
                    .into(image_view_avatar)
        }
    }
}