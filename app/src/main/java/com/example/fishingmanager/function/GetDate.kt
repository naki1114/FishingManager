package com.example.fishingmanager.function

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class GetDate {


    // 현재 시스템 시간 확인
    fun getTime(): Long {

        return System.currentTimeMillis()

    } // getTime()


    // 시스템 시간을 yyyyMMdd 형식으로 변환
    @SuppressLint("SimpleDateFormat")
    fun getFormatDate2(time: Long): String {

        val format = SimpleDateFormat("yyyyMMdd")

        return format.format(time)

    } // getFormatDate2()


    // 시스템 시간을 yyyy-MM-dd 형식으로 변환
    @SuppressLint("SimpleDateFormat")
    fun getFormatDate3(time: Long): String {

        val format = SimpleDateFormat("yyyy-MM-dd")

        return format.format(time)

    } // getFormatDate3()


    // 하루 전 시스템 시간을 yyyyMMdd 형식으로 변환
    @SuppressLint("SimpleDateFormat")
    fun getFormatDate4(time: Long): String {

        val format = SimpleDateFormat("yyyyMMdd")
        val formatTime: String = format.format(time)

        return (Integer.parseInt(formatTime) - 1).toString()

    } // getFormatDate4()


    // 시스템 시간을 yyyy.MM.dd 형식으로 변환
    @SuppressLint("SimpleDateFormat")
    fun getFormatDate5(time: Long): String {

        val format = SimpleDateFormat("yyyy.MM.dd")

        return format.format(time)

    } // getFormatDate5()


    // 시스템 시간을 HH:00 형식으로 변환
    @SuppressLint("SimpleDateFormat")
    fun getFormatTime(time: Long): String {

        val format = SimpleDateFormat("HH:00")

        return format.format(time)

    } // getFormatTime()


    // 요일 확인
    fun getDayOfWeek(date: String): String {

        val dayOfWeek: String
        val array = date.split("-")

        val year = Integer.parseInt(array[0])
        val month = Integer.parseInt(array[1])
        val day = Integer.parseInt(array[2])

        dayOfWeek =
            LocalDate.of(year, month, day).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

        return " (" + dayOfWeek.substring(0, 1) + ")"

    } // getDayOfWeek()


    //
    @SuppressLint("SimpleDateFormat")
    fun getDaysLater(date: Int): String {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DATE, date)

        val format = SimpleDateFormat("yyyy-MM-dd")

        return format.format(calendar.time)

    } // getDaysLater()


}