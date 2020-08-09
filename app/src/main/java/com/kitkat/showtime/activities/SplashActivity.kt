package com.kitkat.showtime.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kitkat.showtime.R

class SplashActivity : BaseActivity() {

    var context : Context = this@SplashActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            finish()

        }, 2500)

    }
}