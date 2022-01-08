package com.app.ripost.utils.notifications

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
interface APIService {
    @Headers(
            "Content-Type:application/json",
            "Authorization:key=AAAAlLmVPrc:APA91bEGScK1lWNoWenBSGxWWZeZ74J8B-odeSmnPU7tYy_f6iEnSp7INkNtHHLWg_sNN5wPaRLGn33sQRemWmRtoHR0N7_-rxxDscKhtek1X2rNmOiGT4_J6KSxVbIxXr6iBBx0cJZ2"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: NotificationSender?): Call<MyResponse?>
}