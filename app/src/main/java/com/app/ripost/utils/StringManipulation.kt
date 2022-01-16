package com.app.ripost.utils

import java.util.regex.Pattern

class StringManipulation {

    fun getTags(text: String): ArrayList<String>{
        val mTags = ArrayList<String>()
        val pattern = Pattern.compile("#\\w+")

        val matcher = pattern.matcher(text)
        while (matcher.find())
        {
            mTags.add(matcher.group())
        }
        return mTags
    }
}