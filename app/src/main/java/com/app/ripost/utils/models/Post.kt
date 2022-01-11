package com.app.ripost.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
        val postID: String,
        val thumbnail: String,
        val createdBy: String,
        val videoUrl: String,
        val likes: Int,
        val comments: Int,
        val shares: Int,
        val createdAt: String
        ): Parcelable{
    constructor(): this("", "", "", "", 0, 0, 0, "")
}