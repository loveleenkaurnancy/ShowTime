package com.kitkat.showtime.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kitkat.showtime.utilities.SharedPreferenceUtil

open class BaseActivity : AppCompatActivity() {

    var context_base : Context = this
    lateinit var sharedPreferenceUtil : SharedPreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        sharedPreferenceUtil = SharedPreferenceUtil(context_base)
    }


}