package com.zohoapplication.di.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.zohoapplication.App
import com.zohoapplication.R
import com.zohoapplication.utits.Constants

class SplashActivity : AppCompatActivity() {
    var handler: Handler? = Handler()
    var runnable: Runnable = Runnable {
        Constants.BASE_URL = "https://randomuser.me/"
        (applicationContext as App).refreshScope()
        startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initSplash()
    }

    private fun initSplash() {
        handler?.postDelayed(runnable, Constants.DELAY_2000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler?.removeCallbacks(runnable)
        handler = null
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }
}