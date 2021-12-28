package com.app.ripost.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String,
           val username: String,
           val displayName: String,
           val email: String,
           val birthday: String,
           val photoUrl: String,
           val biography: String,
           val dateCreated: String,
           val followers: Int,
           val following: Int,
           val posts: Int,
           val certified: Boolean,
           val private: Boolean,
           val token: String): Parcelable{
    constructor() : this("", "", "", "", "", "", "", "", 0,
        0,0,false, false, "")
}