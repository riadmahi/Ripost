package com.app.ripost.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.app.ripost.R
import com.app.ripost.ui.camera.CameraActivity
import com.app.ripost.ui.home.MainActivity
import com.app.ripost.ui.notification.NotificationActivity
import com.app.ripost.ui.profile.ProfileActivity
import com.app.ripost.ui.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationHelper {
    companion object{
        private const val TAG = "BottomNavigationHelper"
    }

    fun navigate(context: Activity, bottomNavigationView: BottomNavigationView){
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    Log.d(TAG, "navigate: Home ")
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                R.id.search -> {
                    Log.d(TAG, "navigate: Search")
                    val intent = Intent(context, SearchActivity::class.java)
                    context.startActivity(intent)
                }
                R.id.creation ->{
                    Log.d(TAG, "navigate: Camera")
                    val intent = Intent(context, CameraActivity::class.java)
                    context.startActivity(intent)
                }
                R.id.notifications -> {
                    Log.d(TAG, "navigate: Notifications")
                    val intent = Intent(context, NotificationActivity::class.java)
                    context.startActivity(intent)
                }
                R.id.profile -> {
                    Log.d(TAG, "navigate: Profile")
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                }
            }
            true
        }

    }
}