package com.app.ripost.UI.Notification

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.Utils.BottomNavigationHelper
import kotlinx.android.synthetic.main.layout_bottom_navigation.*

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation(){
        Log.d(TAG, "setupBottomNavigation: started.")
        BottomNavigationHelper().navigate(this, bottom_navigation)
        val menu: Menu = bottom_navigation.menu
        val menuItem: MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }

    override fun onStart() {
        overridePendingTransition(0, 0)
        super.onStart()
    }

    companion object{
        private const val TAG = "NotificationActivity"
        private const val ACTIVITY_NUM = 3
    }

}