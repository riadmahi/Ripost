package com.app.ripost.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
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

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    fun getDate(date: String):String {


        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        formatter.timeZone = TimeZone.getTimeZone("Canada/Pacific")
        val value = formatter.parse(date)

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") //this format changeable

        dateFormatter.timeZone = TimeZone.getDefault()
        val newDate = dateFormatter.format(value)
        var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val mDate = sdf.parse(newDate)
        Log.d(TAG, "getDate: $")
        val mMs = mDate.time
        sdf  = SimpleDateFormat("EEEE, dd MMMM yyyy") // the format of your date
        var time = ""
        if(todayDate()==sdf.format(mMs)){
            time = "Today"
        }else {
            time += sdf.format(mMs)
        }
        return time
    }


    @SuppressLint("SimpleDateFormat")
    fun getDay(date: String): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        formatter.timeZone = TimeZone.getTimeZone("Canada/Pacific")
        val value = formatter.parse(date)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") //this format changeable
        dateFormatter.timeZone = TimeZone.getDefault()
        val newDate = dateFormatter.format(value!!)
        var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val mDate = sdf.parse(newDate)
        Log.d(TAG, "getDate: $")
        val mMs = mDate?.time
        sdf  = SimpleDateFormat("dd-MM") // the format of your date
        return sdf.format(mMs)
    }

    fun getTime(date: String): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        formatter.timeZone = TimeZone.getTimeZone("Canada/Pacific")
        val value = formatter.parse(date)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") //this format changeable
        dateFormatter.timeZone = TimeZone.getDefault()
        val newDate = dateFormatter.format(value!!)
        var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val mDate = sdf.parse(newDate)
        Log.d(TAG, "getDate: $")
        val mMs = mDate?.time
        sdf  = SimpleDateFormat("HH:mm") // the format of your date
        return sdf.format(mMs)
    }

    @SuppressLint("SimpleDateFormat")
    private fun todayDate(): String{
        val sdf  = SimpleDateFormat("dd-MM") // the format of your date
        return sdf.format(Date())
    }
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    fun timeLeft(date: String): String{
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA)
        sdf.timeZone = TimeZone.getTimeZone("Canada/Pacific")
        val mDate = sdf.parse(date)
        val mChatMs = mDate.time
        val currentDate = sdf.parse(getTimestamp())
        val currentMs = currentDate.time
        val diff = currentMs - mChatMs
        val sec = diff/1000
        Log.d(TAG, "timeLeft: sec = $sec")
        return when {
            sec < 60 -> {
                "now"
            }
            sec < 3600 -> {
                val min = sec / 60
                "${min}min ago"
            }
            sec< 86400 -> {
                val h = sec / 3600
                "${h}h ago"
            }
            else -> {
                val day = sec / 86400
                "${day}day ago"
            }
        }

    }
}