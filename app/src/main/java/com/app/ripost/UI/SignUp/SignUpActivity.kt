package com.app.ripost.UI.SignUp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.UI.Settings.BiographyFragment
import com.app.ripost.UI.Settings.BirthdayFragment
import com.app.ripost.UI.Settings.DisplayNameFragment
import com.app.ripost.UI.Settings.ProfilePhotoFragment
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.snippet_toolbar.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        toolbarText.text = "Sign Up"
        PushDownAnim.setPushDownAnimTo(back).setOnClickListener {
            finish()
        }
        PushDownAnim.setPushDownAnimTo(signUp).setOnClickListener {
            openDisplayNameFragment()
        }
    }

    private fun openDisplayNameFragment(){
        supportFragmentManager.beginTransaction()
                .add(R.id.container, DisplayNameFragment(), "FROM_SIGN_UP")
                .commit()
    }

    fun openBirthdayFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.container, BirthdayFragment(), "FROM_SIGN_UP").commit()
    }


    fun openBiographyFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.container, BiographyFragment(), "FROM_SIGN_UP").commit()
    }

    fun openProfilePhotoFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.container, ProfilePhotoFragment(), "FROM_SIGN_UP").commit()
    }
}