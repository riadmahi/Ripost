package com.app.ripost.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.profile.ViewProfileActivity
import com.app.ripost.utils.BottomNavigationHelper
import com.app.ripost.utils.models.User
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_bottom_navigation.*

class SearchActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupBottomNavigation()

        PushDownAnim.setPushDownAnimTo(searchInput).setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, SearchFragment(), "SEARCH")
                    .commit()
        }
    }

    private fun setupBottomNavigation(){
        Log.d(TAG, "setupBottomNavigation: started.")
        BottomNavigationHelper().navigate(this, bottom_navigation)
        val menu: Menu = bottom_navigation.menu
        val menuItem: MenuItem = menu.getItem(ACTIVITY_NUM)
        menuItem.isChecked = true
    }

    fun openViewProfile(user: User){
        val intent = Intent(this, ViewProfileActivity::class.java).putExtra("EXTRA_USER", user)
        startActivity(intent)
    }

    override fun onStart() {
        overridePendingTransition(0, 0)
        super.onStart()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag("SEARCH")
        Log.d(TAG, "onBackPressed: fragment find $fragment")
        if (fragment != null) {
            Log.d(TAG, "onBackPressed: pressed")
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }else{
            //No fragment, close the activity
            super.onBackPressed()
        }
    }

    companion object{
        private const val TAG = "SearchActivity"
        private const val ACTIVITY_NUM = 1
    }


}