package com.example.fishingmanager.Function

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
    fun getFormatDate2(time : Long) : String {

        var format = SimpleDateFormat("yyyyMMdd")
        var formatTime : String = format.format(time)

        return formatTime

    } // getFormatDate2()

    @SuppressLint("SimpleDateFormat")
    fun getFormatDate3(time : Long) : String {

        var format = SimpleDateFormat("yyyy-MM-dd")
        var formatTime : String = format.format(time)

        return formatTime

    } // getFormatDate2()


    @SuppressLint("SimpleDateFormat")
    fun getFormatTime(time : Long) : String {

        var format = SimpleDateFormat("yyMMddHHmm")
        var formatTime : String = format.format(time)

        return formatTime

    } // getFormatTime()


}