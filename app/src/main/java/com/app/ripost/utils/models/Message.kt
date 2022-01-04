package com.app.ripost.utils.models

data class Message(val message: String,
                   val dateCreated: String,
                   val sendBy: String) {
    constructor() : this("", "","")
}