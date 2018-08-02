package com.gigigo.mvvm.view

import android.content.Context
import android.widget.Toast

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}