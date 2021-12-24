package com.app.ripost.ui.signIn

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.search.SearchActivity
import com.app.ripost.ui.search.SearchFragment
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.snippet_toolbar.*

class SignInActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        toolbarText.text = "Sign In"
        PushDownAnim.setPushDownAnimTo(back).setOnClickListener{
            finish()
        }

        forgotPassword.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, ForgotPasswordFragment(), "SIGN_IN")
                    .commit()
        }
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag("SIGN_IN")
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
        private const val TAG = "SignInActivity"
    }
}