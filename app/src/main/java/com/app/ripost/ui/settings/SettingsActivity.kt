package com.app.ripost.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.snippet_toolbar.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbarText.text = "Settings"

        displayName.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, DisplayNameFragment(), "FROM_SETTINGS")
                    .commit()
        }

        biography.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, BiographyFragment(), "FROM_SETTINGS")
                    .commit()
        }

        profilePhoto.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, ProfilePhotoFragment(), "FROM_SETTINGS")
                    .commit()
        }

        username.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, UsernameFragment(), "FROM_SETTINGS")
                    .commit()
        }

        PushDownAnim.setPushDownAnimTo(back).setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag("FROM_SETTINGS")
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
        private const val TAG = "SettingsActivity"
    }
}