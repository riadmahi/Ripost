package com.app.ripost.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.home.MainActivity
import java.util.*
import kotlin.concurrent.timerTask

class SplashScreenActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "SplashScreenActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Log.d(TAG, "onCreate: started.")
        supportActionBar?.hide()
        val timer = Timer()
        timer.schedule(timerTask {
            Log.d(TAG, "onCreate: 1.8s done move to MainActivity.")
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1200)
    }
}