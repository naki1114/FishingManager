package com.example.fishingmanager.function

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

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
    fun getFormatDate4(time : Long) : String {

        var format = SimpleDateFormat("yyyyMMdd")
        var formatTime : String = format.format(time)

        return (Integer.parseInt(formatTime) - 1).toString()

    } // getFormatDate4()


    @SuppressLint("SimpleDateFormat")
    fun getFormatDate5(time : Long) : String {

        var format = SimpleDateFormat("yyyy.MM.dd")
        var formatTime : String = format.format(time)

        return formatTime

    } // getFormatDate5()


    @SuppressLint("SimpleDateFormat")
    fun getFormatTime(time : Long) : String {

        var format = SimpleDateFormat("HH:00")
        var formatTime : String = format.format(time)

        return formatTime

    } // getFormatTime()


    fun getDayOfWeek(date: String): String {

        var dayOfWeek = ""
        val array = date.split("-")

        val year = Integer.parseInt(array[0])
        val month = Integer.parseInt(array[1])
        val day = Integer.parseInt(array[2])

        dayOfWeek =
            LocalDate.of(year, month, day).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

        return " (" + dayOfWeek.substring(0, 1) + ")"

    }


    @SuppressLint("SimpleDateFormat")
    fun getDaysLater(date : Int) : String {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DATE, date)

        val format = SimpleDateFormat("yyyy-MM-dd")
        val formatTime : String = format.format(calendar.time)

        return formatTime

    }


}