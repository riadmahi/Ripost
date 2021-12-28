package com.app.ripost.utils.database

interface FirebaseCallback {
    fun progressUpload(progress: Int)
    fun onFinish()
    fun onFailure()

}

interface FirebaseCallbackSuccess{
    fun onSuccess()
}