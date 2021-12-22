package com.app.ripost.UI.SignIn

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.thekhaeng.pushdownanim.PushDownAnim
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
    }

}