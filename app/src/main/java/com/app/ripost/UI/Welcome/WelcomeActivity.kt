package com.app.ripost.UI.Welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.UI.SignIn.SignInActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        animButton()
        PushDownAnim.setPushDownAnimTo(signIn).setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        PushDownAnim.setPushDownAnimTo(signUp).setOnClickListener {

        }
    }

    private fun animButton(){
        signIn.alpha = 0f
        signIn.translationY = 50F
        signIn.animate().alpha(1f).translationYBy(-50F).duration = 1000

        signUp.alpha = 0f
        signUp.translationY = 50F
        signUp.animate().alpha(1f).translationYBy(-50F).duration = 1000
    }
}