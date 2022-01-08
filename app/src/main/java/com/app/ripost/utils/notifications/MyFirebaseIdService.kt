package com.app.ripost.utils.notifications

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseIdService: FirebaseMessagingService(){
    @Suppress("deprecation")
    override fun onNewToken(s:String){
        super.onNewToken(s)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken:String = FirebaseInstanceId.getInstance().token.toString()
        if(firebaseUser!=null){
            updateToken(refreshToken)
        }
    }
    private fun updateToken(refreshToken:String){
        FirebaseDatabase.getInstance().getReference(getString(R.string.dbname_users))
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(getString(R.string.token))
            .setValue(refreshToken)
    }
}