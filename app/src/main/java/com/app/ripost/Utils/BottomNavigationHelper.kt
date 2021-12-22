package com.app.ripost.Utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.app.ripost.R
import com.app.ripost.UI.Camera.CameraActivity
import com.app.ripost.UI.Home.MainActivity
import com.app.ripost.UI.Notification.NotificationActivity
import com.app.ripost.UI.Profile.ProfileActivity
import com.app.ripost.UI.Search.SearchActivity
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