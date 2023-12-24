package com.example.fishingmanagerclone.Function

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class GetDate {

    fun getTime() : Long {

        return System.currentTimeMillis()

    } // getTime()


    @SuppressLint("SimpleDateFormat")
    fun getFormatDate(time : Long) : String {

        var format = SimpleDateFormat("yyMMdd")
        var formatTime : String = format.format(time)

        return formatTime

    } // getFormatDate()


    @SuppressLint("SimpleDateFormat")
    fun getFormatTime(time : Long) : String {

        var format = SimpleDateFormat("yyMMddHHmm")
        var formatTime : String = format.format(time)

        return formatTime

    } // getFormatTime()


}