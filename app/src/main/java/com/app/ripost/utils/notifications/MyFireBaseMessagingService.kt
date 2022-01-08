package com.app.ripost.utils.notifications

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import com.app.ripost.R
import com.app.ripost.ui.home.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessagingService: FirebaseMessagingService() {
    private lateinit var title: String
    private lateinit var message: String
    companion object{
        private const val TAG = "MyFireBaseMessaging"
    }
    override fun onMessageReceived(@NonNull remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        title = remoteMessage.data["Title"].toString()
        message = remoteMessage.data["Message"].toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendOreoNotification()
        }else{

            sendOldVersionNotification()
        }

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        //Don't use.
        Log.d(TAG, "onNewToken: checking token....")

    }

    @SuppressLint("ServiceCast")
    @Suppress("deprecation")
    private fun sendOreoNotification(){
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val notificationIntent = Intent(this, MainActivity::class.java)
        val conPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val builder = NotificationCompat.Builder(applicationContext)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(conPendingIntent)
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())

        val oreoNotification = OreoNotification(this)
        oreoNotification.createChannel()
        val mBuilder = oreoNotification.getOreoNotification(title, message, defaultSound)
        oreoNotification.getManager()?.notify(System.currentTimeMillis().toInt(), mBuilder.build())

    }

    @Suppress("deprecation")
    private fun sendOldVersionNotification(){

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(applicationContext)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSound)

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}
