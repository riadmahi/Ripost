package com.app.ripost.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.utils.database.FirebaseCallbackSuccess
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_view_profile.*
import kotlinx.android.synthetic.main.snippet_profile_follow.*
import kotlinx.android.synthetic.main.snippet_profile_information.*
import kotlinx.android.synthetic.main.snippet_toolbar.*
import kotlinx.android.synthetic.main.snippet_view_profile_follow.*

class ViewProfileActivity : AppCompatActivity() {

    private var mUser: User? =null
    private var isFriend = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        Log.d(TAG, "onCreate: started.")
        editProfile.visibility = View.GONE
        report.visibility = View.VISIBLE
        mUser = intent.getParcelableExtra("EXTRA_USER")
        setupUserInformation()
        back.setOnClickListener {
            finish()
        }

        updateFollowButtonUI()
        if(mUser?.private == true){
            accountVisibility.visibility = View.VISIBLE
        }else{
            if(checkIfFriend()){
                //Load the gridView
                Log.d(TAG, "onCreate: They are friends.")
            }
        }
        setupWidgets()
    }



    private fun checkIfFriend(): Boolean{
        return true
    }


    @SuppressLint("SetTextI18n")
    private fun setupUserInformation(){
        toolbarText.text = mUser?.username
        username.text = "@${mUser?.username}"
        displayName.text = mUser?.displayName
        numFollowers.text = mUser?.followers.toString()
        numFollowing.text = mUser?.following.toString()
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

    private fun setupWidgets(){
        PushDownAnim.setPushDownAnimTo(follow).setOnClickListener {
            if(mUser?.private == false) {
                follow.visibility = View.GONE
                unFollow.visibility = View.VISIBLE
                FirebaseMethods(this).addFollower(mUser?.uid.toString())
                FirebaseMethods(this).addFollowing(mUser?.uid.toString())
                numFollowers.text = mUser?.followers.toString()

                FirebaseMethods(this).isFollowingMe(mUser?.uid.toString(), object : FirebaseCallbackSuccess {
                    override fun onSuccess() {
                        FirebaseMethods(this@ViewProfileActivity).addFriend(mUser?.uid.toString())
                        isFriend = true
                    }
                })


            }else{
                FirebaseMethods(this).askPermissionToFollow(mUser?.uid.toString())
                follow.visibility = View.GONE
                unFollow.visibility = View.GONE
                waitResponse.visibility = View.VISIBLE
            }
        }

        PushDownAnim.setPushDownAnimTo(unFollow).setOnClickListener {
            unFollow.visibility = View.GONE
            follow.visibility = View.VISIBLE
            FirebaseMethods(this).removeFollower(mUser?.uid.toString())
            FirebaseMethods(this).removeFollowing(mUser?.uid.toString())
            numFollowers.text = mUser?.followers?.minus(1).toString()
            if (isFriend)
                FirebaseMethods(this).removeFriend(mUser?.uid.toString())
        }

        PushDownAnim.setPushDownAnimTo(waitResponse).setOnClickListener {
            unFollow.visibility = View.GONE
            follow.visibility = View.VISIBLE

        }

        PushDownAnim.setPushDownAnimTo(sendMessage).setOnClickListener {
            FirebaseMethods(this).removeUserInWaitList(mUser?.uid.toString())
        }
    }


    private fun updateFollowButtonUI(){
        var isFollow = false
        FirebaseMethods(this).checkIfIsFollower(mUser?.uid.toString(), object : FirebaseCallbackSuccess {
            override fun onSuccess() {
                follow.visibility = View.GONE
                unFollow.visibility = View.VISIBLE
                isFollow = true
            }
        })
        FirebaseMethods(this).waitTheFollowPermission(mUser?.uid.toString(), object : FirebaseCallbackSuccess{
            override fun onSuccess() {
                follow.visibility = View.GONE
                unFollow.visibility = View.GONE
                waitResponse.visibility = View.VISIBLE
                isFollow = true
            }
        })

        if(!isFollow){
            follow.visibility = View.VISIBLE
        }


    }

    companion object{
        private const val TAG = "ViewProfileActivity"
    }





}