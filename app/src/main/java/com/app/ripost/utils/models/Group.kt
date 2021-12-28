package com.app.ripost.utils.models

data class Group(val groupID: String,
                 val createdBy: String,
                 val dateCreated: String,
                 val members: ArrayList<String>,
                 val recentMessage: String,
                 val seenBy: ArrayList<String>) {
}