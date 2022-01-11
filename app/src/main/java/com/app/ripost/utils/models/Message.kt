package com.app.ripost.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
        val messageID: String,
        var message: String,
        val dateCreated: String,
        val sendBy: String,
        val reactions: ArrayList<String>): Parcelable {
    constructor() : this("", "", "","", ArrayList())
}