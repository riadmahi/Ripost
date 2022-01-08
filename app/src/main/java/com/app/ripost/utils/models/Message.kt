package com.app.ripost.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(val message: String,
                   val dateCreated: String,
                   val sendBy: String): Parcelable {
    constructor() : this("", "","")
}