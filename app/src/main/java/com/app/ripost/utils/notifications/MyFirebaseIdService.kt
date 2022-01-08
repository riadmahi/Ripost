package com.app.ripost.utils.notifications

import com.app.ripost.utils.database.FirebaseMethods
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseIdService: FirebaseMessagingService(){
    @Suppress("deprecation")
    override fun onNewToken(s:String){
        super.onNewToken(s)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken:String = FirebaseMessaging.getInstance().token.toString()
        if(firebaseUser!=null){
            updateToken(refreshToken)
        }
    }
    private fun updateToken(refreshToken:String){
        FirebaseMethods(applicationContext).updateToken(refreshToken)
    }
}