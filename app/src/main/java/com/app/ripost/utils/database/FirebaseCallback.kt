package com.app.ripost.utils.database

import com.app.ripost.utils.models.Group

interface FirebaseCallback {
    fun progressUpload(progress: Int)
    fun onFinish()
    fun onFailure()

}

interface FirebaseCallbackSuccess{
    fun onSuccess()
}

interface FirebaseCallbackGroups{
    fun onSuccess(groups: MutableList<Group>)
}

interface  FirebaseCallbackGroup{
    fun onSuccess(group: Group)
}