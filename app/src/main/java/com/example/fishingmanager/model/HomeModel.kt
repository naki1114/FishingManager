package com.example.fishingmanager.model

import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.HomeWeather
import com.example.fishingmanager.function.GetDate

class HomeModel {

    fun getWeather(weatherList : ArrayList<ConditionWeather>) : HomeWeather {

        val currentDate = GetDate().getFormatDate3(GetDate().getTime())
        val currentTime = ""
        var time = ""
        var skyImage = 0
        var temp = ""
        var humidity = ""
        var windSpeed = ""
        var location = ""

        for (i in 0 until weatherList.size) {

            if (weatherList[i].date == currentDate && weatherList[i].time == currentTime) {

                time = weatherList[i].time
                temp = weatherList[i].temp
                humidity = weatherList[i].humidity
                windSpeed = weatherList[i].windSpeed


            }

        }

        return HomeWeather(time, skyImage, temp, humidity, windSpeed, location)

    }

}