package com.app.ripost.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.settings.SettingsActivity
import com.app.ripost.utils.adapters.ProfileGridViewAdapter
import com.app.ripost.utils.BottomNavigationHelper
import com.app.ripost.utils.database.FirebaseCallback
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.database.FirebaseRetrieveUserCallback
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.layout_bottom_navigation.*
import kotlinx.android.synthetic.main.snippet_profile_information.*

class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
        PushDownAnim.setPushDownAnimTo(editProfile).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        setupUserInformation()
        val photos : MutableList<String> = mutableListOf()
        photos.add("https://images.unsplash.com/photo-1640289753542-b02666f4a664?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639999629202-85a8f62d4a18?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDMyfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640257717883-db9294d0d7cd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzMnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639735333018-b29254e71121?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDUxfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640289753542-b02666f4a664?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639999629202-85a8f62d4a18?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDMyfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640257717883-db9294d0d7cd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzMnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639735333018-b29254e71121?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDUxfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640289753542-b02666f4a664?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639999629202-85a8f62d4a18?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDMyfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640257717883-db9294d0d7cd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzMnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639735333018-b29254e71121?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDUxfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640289753542-b02666f4a664?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639999629202-85a8f62d4a18?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDMyfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640257717883-db9294d0d7cd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzMnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639735333018-b29254e71121?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDUxfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639999629202-85a8f62d4a18?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDMyfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640257717883-db9294d0d7cd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzMnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639735333018-b29254e71121?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDUxfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640289753542-b02666f4a664?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639999629202-85a8f62d4a18?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDMyfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1640257717883-db9294d0d7cd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzMnx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60")
        photos.add("https://images.unsplash.com/photo-1639735333018-b29254e71121?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDUxfHFQWXNEenZKT1ljfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60")


        profileGridView.isExpanded = true
        val mPhotoGridViewAdapter = ProfileGridViewAdapter(photos, this)
        //profileGridView.adapter = mPhotoGridViewAdapter
    }
    private fun setupUserInformation(){
        //Retrieve user data
        FirebaseMethods(this).getUserInformation(object: FirebaseRetrieveUserCallback{
            @SuppressLint("SetTextI18n")
            override fun onFinish(user: User) {
                username.text = "@${user.username}"
                displayName.text = user.displayName
                if (user.photoUrl != ""){
                    Glide.with(this@ProfileActivity)
                        .load(user.photoUrl)
                        .centerCrop()
                        .into(profile_photo)

                }
                biography.text = user.biography
            }
        })

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
        private const val TAG = "ProfileActivity"
        private const val ACTIVITY_NUM = 4
    }

}