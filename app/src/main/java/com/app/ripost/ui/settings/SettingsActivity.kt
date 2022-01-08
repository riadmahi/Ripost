package com.app.ripost.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.welcome.WelcomeActivity
import com.app.ripost.utils.models.User
import com.google.firebase.auth.FirebaseAuth
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.snippet_toolbar.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mUser: User? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        auth = FirebaseAuth.getInstance()
        mUser = intent.getParcelableExtra("EXTRA_USER")
        toolbarText.text = "Settings"

        val args = Bundle()
        args.putParcelable("EXTRA_USER",mUser!!)
        displayName.setOnClickListener {
            val fragment = DisplayNameFragment()
            fragment.arguments = args
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, "FROM_SETTINGS")
                    .commit()
        }

        biography.setOnClickListener {
            val fragment = BiographyFragment()
            fragment.arguments = args
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, "FROM_SETTINGS")
                    .commit()
        }

        profilePhoto.setOnClickListener {
            val fragment = ProfilePhotoFragment()
            fragment.arguments = args
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, "FROM_SETTINGS")
                    .commit()
        }

        username.setOnClickListener {
            val fragment = UsernameFragment()
            fragment.arguments = args
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, "FROM_SETTINGS")
                    .commit()
        }
        logOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, WelcomeActivity::class.java)
            finishAffinity()
            startActivity(intent)
            finish()
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