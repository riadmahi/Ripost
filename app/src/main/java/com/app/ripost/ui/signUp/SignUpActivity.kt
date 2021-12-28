package com.app.ripost.ui.signUp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.settings.*
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.snippet_toolbar.*

class SignUpActivity : AppCompatActivity() {

    private var usernameVerified = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupWidgets()
        usernameVerification()
    }

    @SuppressLint("SetTextI18n")
    private fun setupWidgets(){
        toolbarText.text = "Sign Up"
        PushDownAnim.setPushDownAnimTo(back).setOnClickListener {
            finish()
        }

        PushDownAnim.setPushDownAnimTo(signUp).setOnClickListener {
            if(usernameVerified && email.text.toString() != "" && password.text.toString() != "")
                signUp()
            else
                Toast.makeText(this, "Field is blank or wrong.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp(){
        progress_bar.visibility = View.VISIBLE
        Firebase.auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
            if(it.isSuccessful){
                Firebase.auth.currentUser?.sendEmailVerification()
                createUser()
            }
        }
    }

    private fun createUser(){
        val user = User(
            Firebase.auth.currentUser!!.uid,
            username.text.toString(),
            "",
            email.text.toString(),
            "",
            "",
            "",
            DateUtils().getTimestamp(),
            0,
            0,
            0,
            certified = false,
            private = false,
            token = ""
        )

        FirebaseFirestore.getInstance().collection(getString(R.string.dbname_users)).document(user.uid).set(user)

        progress_bar.visibility = View.GONE
        openDisplayNameFragment()
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

    /**
     * Check if the field username is correct and valid
     * - match with the regex
     * - is available
     */

    private fun usernameVerification(){
        username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.contains(Regex("^[a-zA-Z0-9_]{4,}$"))) {
                    Log.d(TAG, "onTextChanged: all is good that username match with the regex.")
                    usernameError.visibility = View.GONE
                    if(!FirebaseMethods(this@SignUpActivity).checkIfUsernameExist(s.toString())){
                        //Username not exist
                        usernameVerified = true
                        usernameError.visibility = View.GONE
                    }else{
                        usernameError.text = "This username already exist."
                        usernameError.visibility = View.VISIBLE
                        usernameVerified = false
                    }

                } else {
                    Log.e(TAG, "onTextChanged: doesn't match with the regex.")
                    usernameError.text = "Your username contains forbidden characters"
                    usernameError.visibility = View.VISIBLE
                    usernameVerified = false
                }
            }
        })
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag("FROM_SIGN_UP")
        Log.d(TAG, "onBackPressed: fragment find $fragment")
        if (fragment != null) {
            Log.d(TAG, "onBackPressed: pressed")
        }else{
            //No fragment, close the activity
            super.onBackPressed()
        }
    }


    companion object{
        private const val TAG = "SignUpActivity"
    }
}