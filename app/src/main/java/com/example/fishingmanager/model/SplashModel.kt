package com.example.fishingmanager.model

import android.util.Log
import com.example.fishingmanager.R
import com.example.fishingmanager.data.ConditionTide
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.Weather
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.network.RetrofitInterface

class SplashModel {

    // Retrofit 통신을 위한 객체 초기화
    private val weatherRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWeatherAPI().create(RetrofitInterface::class.java)

    private val tideRetrofitInterface: RetrofitInterface =
        RetrofitClient.getTideAPI().create(RetrofitInterface::class.java)

    private val indexRetrofitInterface: RetrofitInterface =
        RetrofitClient.getIndexAPI().create(RetrofitInterface::class.java)

    private val webServerRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    // 날씨 API 요청
    fun requestWeather(
        pageNo: String,
        numOfRows: String,
        dataType: String,
        baseDate: String,
        baseTime: String,
        nx: String,
        ny: String
    ) = weatherRetrofitInterface.requestWeather(pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny)

    // 조석 API 요청
    fun requestTide(baseDate: String, location: String, resultType: String) =
        tideRetrofitInterface.requestTide(baseDate, location, resultType)

    // 지수 API 요청
    fun requestIndex(type: String, resultType: String) =
        indexRetrofitInterface.requestIndex(type, resultType)

    // Web Server DB 요청
    fun requestCombine(nickname : String) =
        webServerRetrofitInterface.requestDB(nickname)


    // 응답 받은 날씨 데이터 중, 필요한 부분만 가공하여 리턴
    fun getWeatherList(response: retrofit2.Response<Weather>): ArrayList<ConditionWeather> {

        val list = ArrayList<Weather.Item>()
        val responseList: List<Weather.Item> = response.body()!!.response.body.items.item
        val listSize = responseList.size

        for (i in 0 until listSize) {

            if (responseList[i].category == "TMP" || responseList[i].category == "REH" ||
                responseList[i].category == "WSD" || responseList[i].category == "SKY" || responseList[i].category == "PTY"
            ) {

                list.add(responseList[i])

            }

        }

        val weatherList = ArrayList<ConditionWeather>()
        var time: String = ""
        var temp: String = ""
        var skyImage: Int = 0
        var humidity: String = ""
        var windSpeed: String = ""
        var date: String = ""

        for (i in 0 until list.size) {

            date = list[i].fcstDate
            time = list[i].fcstTime.substring(0, 2) + ":" + list[i].fcstTime.substring(2, 4)

            // 온도
            if (list[i].category == "TMP") {

                temp = list[i].fcstValue + "˚C"

                // 하늘 상태
            } else if (list[i].category == "SKY") {

                when (list[i].fcstValue) {

                    "0", "1", "2", "3", "4", "5" -> skyImage = R.drawable.sun
                    "6", "7", "8" -> skyImage = R.drawable.cloudy
                    "9", "10" -> skyImage = R.drawable.fade

                }

                // 강수 타입
            } else if (list[i].category == "PTY") {

                when (list[i].fcstValue) {

                    "1", "4" -> skyImage = R.drawable.rain
                    "2", "3" -> skyImage = R.drawable.snow

                }

                // 풍속
            } else if (list[i].category == "WSD") {

                windSpeed = list[i].fcstValue + "m/s"

                // 습도
            } else if (list[i].category == "REH") {

                humidity = list[i].fcstValue + "%"

                val conditionWeather = ConditionWeather(time, skyImage, temp, humidity, windSpeed, date)
                weatherList.add(conditionWeather)

            }

        }

        return weatherList

    } // getWeatherList()


    // 응답 받은 조석 데이터 중, 필요한 부분만 가공하여 리턴
    fun getTideList(response: retrofit2.Response<Tide>): ArrayList<ConditionTide> {

        val list = ArrayList<Tide.Item>()
        val responseList: ArrayList<Tide.Item> = response.body()!!.result.data
        val listSize = responseList.size
        var tideString = ""

        for (i in 0 until listSize) {

            tideString = responseList[i].tph_time.substring(11, 16)

            list.add(Tide.Item(responseList[i].tph_level, tideString, responseList[i].hl_code))

        }

        val tideList = ArrayList<ConditionTide>()
        var time: String = ""
        var upDownImage: Int = 0
        var tide: String = ""
        var waterHeightImage: Int = 0
        var waterHeight: String = ""

        for (i in 0 until responseList.size) {

            time = list[i].tph_time
            waterHeight = list[i].tph_level + "cm"

            when (list[i].hl_code) {

                "고조" -> tide = "만조"
                "저조" -> tide = "간조"

            }

            when (list[i].hl_code) {

                "고조" -> {
                    upDownImage = R.drawable.tide_high
                    waterHeightImage = R.drawable.water_height_high
                }

                "저조" -> {
                    upDownImage = R.drawable.tide_low
                    waterHeightImage = R.drawable.water_height_low
                }

            }

            val conditionTide = ConditionTide(
                time,
                upDownImage,
                tide,
                waterHeightImage,
                waterHeight
            )
            tideList.add(conditionTide)

        }

        return tideList

    } // getTideList()


    // 응답 받은 지수 데이터 중, 필요한 부분만 가공하여 리턴
    fun getIndexList(response : retrofit2.Response<Index>) : ArrayList<Index.Item> {

        val list = ArrayList<Index.Item>()
        val responseList : ArrayList<Index.Item> = response.body()!!.result.data
        val listSize = responseList.size

        for (i in 0 until listSize) {

                list.add(responseList[i])

        }

        return list

    } // getIndexList()


}