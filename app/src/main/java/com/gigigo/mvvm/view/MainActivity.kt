package com.gigigo.mvvm.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gigigo.mvvm.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListUsersFragment.newInstance())
                    .commitNow()
        }
    }
}
