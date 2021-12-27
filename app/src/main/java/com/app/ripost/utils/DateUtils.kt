package com.app.ripost.utils

import java.text.SimpleDateFormat
import java.util.*


class DateUtils {
    /**
     * Get the current time.
     */
    fun getTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA)
        sdf.timeZone = TimeZone.getTimeZone("Canada/Pacific")
        return sdf.format(Date())
    }
}