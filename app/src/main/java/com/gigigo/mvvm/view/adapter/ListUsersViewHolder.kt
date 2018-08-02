package com.gigigo.mvvm.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gigigo.mvvm.R
import com.gigigo.mvvm.core.recycler.BaseViewHolder
import com.gigigo.mvvm.model.User
import com.squareup.picasso.Picasso

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersViewHolder(override val container: View):
        BaseViewHolder<User>(container) {
    private var textViewName: TextView = container.findViewById(R.id.text_view_name)
    private var imageViewAvatar: ImageView = container.findViewById(R.id.image_view_avatar)

    override fun bind(item: User) {
        super.bind(item)

        textViewName.text = item.fullName
        Picasso.get()
                .load(item.avatar)
                .resize(80, 80)
                .centerCrop()
                .into(imageViewAvatar)
    }
}