package com.example.fishingmanager.model

import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.HomeRecommend
import com.example.fishingmanager.data.HomeWeather
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.function.GetDate

class HomeModel {

    fun getWeather(weatherList : ArrayList<ConditionWeather>, location : String) : HomeWeather {

        val currentDate = GetDate().getFormatDate2(GetDate().getTime())
        val currentTime = GetDate().getFormatTime(GetDate().getTime())
        var skyImage = 0
        var temp = ""
        var humidity = ""
        var windSpeed = ""

        for (i in 0 until weatherList.size) {

            if (weatherList[i].date == currentDate && weatherList[i].time == currentTime) {

                skyImage = weatherList[i].skyImage
                temp = weatherList[i].temp
                humidity = weatherList[i].humidity
                windSpeed = weatherList[i].windSpeed

            }

        }

        return HomeWeather(location, skyImage, temp, humidity, windSpeed)

    } // getWeather()


    fun getRecommendList(indexList : ArrayList<Index.Item>) : ArrayList<HomeRecommend>{

        val list = ArrayList<HomeRecommend>()
        var date = ""
        var location = ""
        var fishName = ""
        var fishImage = 0

        for (i in 0 until indexList.size) {

            

        }

        return list

    }


}