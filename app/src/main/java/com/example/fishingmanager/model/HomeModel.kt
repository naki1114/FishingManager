package com.example.fishingmanager.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fishingmanager.R
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
        
        val veryGoodList = ArrayList<HomeRecommend>()

        for (i in 0 until indexList.size) {

            if (indexList[i].total_score == "매우좋음") {

                val date = indexList[i].date.substring(5,7) + " / " + indexList[i].date.substring(8,10) + GetDate().getDayOfWeek(indexList[i].date)
                val location = indexList[i].name + " - " + indexList[i].time_type
                val fishName = indexList[i].fish_name
                val fishImage = getFishImage(fishName)

                veryGoodList.add(HomeRecommend(date, location, fishName, fishImage))

            }
            
        }


        
        return veryGoodList
        
    } // getRecommendList()


    fun getFishImage(fishName : String) : Int {

        var fishImage = 0

        when (fishName) {

            "우럭" -> fishImage = R.drawable.uruck

            "광어" -> fishImage = R.drawable.gawnga

            "볼락" -> fishImage = R.drawable.bollock

            "농어" -> fishImage = R.drawable.nonga

            "열기" -> fishImage = R.drawable.yulgi

            "참돔" -> fishImage = R.drawable.chamdom

            "감성돔" -> fishImage = R.drawable.gamsungdom

            "돌돔" -> fishImage = R.drawable.doldom

            "벵에돔" -> fishImage = R.drawable.bengedom

        }

        return fishImage

    }
    

}