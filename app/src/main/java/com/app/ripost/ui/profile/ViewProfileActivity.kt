package com.app.ripost.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_profile.*
import kotlinx.android.synthetic.main.snippet_profile_information.*
import kotlinx.android.synthetic.main.snippet_toolbar.*

class ViewProfileActivity : AppCompatActivity() {

    private var mUser: User? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        editProfile.setImageResource(R.drawable.ic_flag)
        mUser = intent.getParcelableExtra("EXTRA_USER")
        setupUserInformation()
        back.setOnClickListener {
            finish()
        }

        if(mUser?.private == true){
            accountVisibility.visibility = View.VISIBLE
        }else{
            if(checkIfFriend()){
                //Load the gridView
            }
        }

    }

    private fun checkIfFriend(): Boolean{
        return true
    }
    @SuppressLint("SetTextI18n")
    private fun setupUserInformation(){
        toolbarText.text = mUser?.username
        username.text = "@${mUser?.username}"
        displayName.text = mUser?.displayName
        if (mUser?.photoUrl != ""){
            Glide.with(this@ViewProfileActivity)
                    .load(mUser?.photoUrl)
                    .centerCrop()
                    .into(profile_photo)

        }
        if(mUser?.biography ==  "")
            biography.visibility = View.GONE
        biography.text = mUser?.biography
    }



}