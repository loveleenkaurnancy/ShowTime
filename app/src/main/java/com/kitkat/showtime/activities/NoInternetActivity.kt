package com.kitkat.showtime.activities

import android.os.Bundle
import com.kitkat.showtime.R
import kotlinx.android.synthetic.main.activity_no_internet.*

class NoInternetActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)

        back.setOnClickListener {
            finish()
        }
    }
}