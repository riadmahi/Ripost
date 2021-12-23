package com.app.ripost.UI.Home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2
import com.app.ripost.R
import com.app.ripost.Utils.Adapters.MainViewPagerAdapter
import com.app.ripost.Utils.BottomNavigationHelper
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom_navigation.*
import kotlinx.android.synthetic.main.snippet_home_toolbar.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                    pager.setCurrentItem(1, false);
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
            pager.currentItem = 2
        }

        PushDownAnim.setPushDownAnimTo(tvYours).setOnClickListener {
            pager.currentItem = 1
        }

        PushDownAnim.setPushDownAnimTo(chat).setOnClickListener {
            pager.currentItem = 0
        }
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
        private const val TAG = "MainActivity"
        private const val ACTIVITY_NUM = 0
    }

}

