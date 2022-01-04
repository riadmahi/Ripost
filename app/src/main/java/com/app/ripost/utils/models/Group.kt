package com.app.ripost.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(val groupID: String,
                 val createdBy: String,
                 val dateCreated: String,
                 val members: ArrayList<String>,
                 val recentMessage: String,
                 val seenBy: ArrayList<String>,
                 val color: ArrayList<String>,
                 val photoUrl: String,
                 val name: String,
                 val lastMessageSendAt: String): Parcelable{
    constructor() : this("", "", "", arrayListOf<String>(), "", arrayListOf<String>(), arrayListOf<String>(), "", "", "")
}