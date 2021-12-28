package com.app.ripost.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.app.ripost.R
import com.app.ripost.ui.welcome.WelcomeActivity
import com.app.ripost.utils.adapters.MainViewPagerAdapter
import com.app.ripost.utils.BottomNavigationHelper
import com.google.firebase.auth.FirebaseAuth
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom_navigation.*
import kotlinx.android.synthetic.main.snippet_home_toolbar.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        setupBottomNavigation()
        setupViewPager()
    }

    private fun setupViewPager(){
        val fragmentList = arrayListOf(
                MessageFragment(),
                ChatFragment(),
                HomeFragment(),
                ViralsFragment()
        )

        val adapter = MainViewPagerAdapter(
                fragmentList,
                this.supportFragmentManager,
                lifecycle
        )

        pager.adapter = adapter
        pager.setCurrentItem(2, false)
        pager.offscreenPageLimit = 3
        toolbarState()
        toolbarNavigation()
        updateUIToolbar(2)
    }

    private fun toolbarState(){
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {

                println("state: $state")
            }

            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                println("scrolled page $position")
            }


            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position ==0) {
                    pager.setCurrentItem(1, false)
                }
                updateUIToolbar(position)
            }
        })


        Log.d(TAG, "updateUIToolbar: current item: ${pager.currentItem}")

    }

    private fun updateUIToolbar(position: Int){
        when (position) {
            1 -> {
                chat.setColorFilter(ContextCompat.getColor(this, R.color.white))
                tvVirals.setTextColor(this.resources.getColor(R.color.placeholder, null))
                tvYours.setTextColor(this.resources.getColor(R.color.placeholder, null))
            }
            2 -> {
                chat.setColorFilter(ContextCompat.getColor(this, R.color.placeholder))
                tvVirals.setTextColor(this.resources.getColor(R.color.placeholder, null))
                tvYours.setTextColor(this.resources.getColor(R.color.white, null))
            }
            3 -> {
                chat.setColorFilter(ContextCompat.getColor(this, R.color.placeholder))
                tvVirals.setTextColor(this.resources.getColor(R.color.white, null))
                tvYours.setTextColor(this.resources.getColor(R.color.placeholder, null))
            }
            else -> {
            }
        }
    }

    private fun toolbarNavigation(){
        PushDownAnim.setPushDownAnimTo(tvVirals).setOnClickListener {
            pager.currentItem = 3
        }

        PushDownAnim.setPushDownAnimTo(tvYours).setOnClickListener {
            pager.currentItem = 2
        }

        PushDownAnim.setPushDownAnimTo(chat).setOnClickListener {
            pager.currentItem = 1
        }
    }

    fun openNewChatFragment(){
        Log.d(TAG, "openNewChatFragment: started")
        supportFragmentManager.beginTransaction()
                .add(R.id.container, NewChatFragment(), "FROM_MAIN")
                .commit()
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
        val currentUser = auth.currentUser
        if(currentUser == null){
            Log.d(TAG, "onStart: user is logged out")
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag("FROM_MAIN")
        Log.d(TAG, "onBackPressed: fragment find $fragment")
        if (fragment != null) {
            Log.d(TAG, "onBackPressed: pressed")
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }else{
            //No fragment, close the activity
            if(pager.currentItem != 2)
                pager.currentItem = 2
            else
                super.onBackPressed()
        }
    }

    companion object{
        private const val TAG = "MainActivity"
        private const val ACTIVITY_NUM = 0
    }

}

