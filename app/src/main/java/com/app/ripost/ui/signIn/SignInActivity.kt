package com.app.ripost.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.home.MainActivity
import com.app.ripost.ui.signUp.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.snippet_toolbar.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setupWidgets()
    }

    @SuppressLint("SetTextI18n")
    private fun setupWidgets(){
        toolbarText.text = "Sign In"

        PushDownAnim.setPushDownAnimTo(back).setOnClickListener{
            finish()
        }

        forgotPassword.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, ForgotPasswordFragment(), "SIGN_IN")
                .commit()
        }

        createAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        PushDownAnim.setPushDownAnimTo(signIn).setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        finishAffinity() //Close welcome activity
                        startActivity(intent)
                        finish() // Close the current activity
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
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