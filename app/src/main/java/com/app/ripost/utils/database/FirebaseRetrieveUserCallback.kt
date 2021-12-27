package com.app.ripost.utils.database

import com.app.ripost.utils.models.User

interface FirebaseRetrieveUserCallback {
    fun onFinish(user: User)
}