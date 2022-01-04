package com.app.ripost.utils.database

import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.Message

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

interface FirebaseCallbackMsg{
    fun onSuccess(messages: MutableList<Message>)
}

interface FirebaseCallbackBool{
    fun onSuccess(bool: Boolean)
}

interface  FirebaseCallbackFriends{
    fun onSuccess(friends: MutableList<String>)
}