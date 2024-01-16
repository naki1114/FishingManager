package com.example.fishingmanager.model

import android.util.Log
import com.example.fishingmanager.R
import com.example.fishingmanager.data.ConditionCombine
import com.example.fishingmanager.data.ConditionIndex
import com.example.fishingmanager.data.ConditionTide
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.SearchLocation
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.Weather
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import retrofit2.Response
import kotlin.math.roundToInt


class ConditionModel {

    private val weatherRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWeatherAPI().create(RetrofitInterface::class.java)

    private val tideRetrofitInterface: RetrofitInterface =
        RetrofitClient.getTideAPI().create(RetrofitInterface::class.java)

    val combineList = ArrayList<ConditionCombine>()
    val weatherList = ArrayList<ConditionWeather>()
    val tideList = ArrayList<ConditionTide>()
    lateinit var index: ConditionIndex
    lateinit var searchLocationList : ArrayList<SearchLocation>
    val selectFish = ArrayList<SelectFish>()
    val locationBaseList = ArrayList<SearchLocation>()

    init {

        locationBaseList.add(SearchLocation("가거도", "SO_0577", "34", "125"))
        locationBaseList.add(SearchLocation("거문도", "DT_0031", "34", "127"))
        locationBaseList.add(SearchLocation("거제도", "DT_0029", "35", "127"))
        locationBaseList.add(SearchLocation("계마항", "SO_1264", "35", "126"))
        locationBaseList.add(SearchLocation("국화도", "SO_0564", "37", "127"))
        locationBaseList.add(SearchLocation("김녕", "DT_0022", "33", "127"))
        locationBaseList.add(SearchLocation("남애항", "SO_0732", "38", "130"))
        locationBaseList.add(SearchLocation("대진항", "SO_0731", "39", "128"))
        locationBaseList.add(SearchLocation("모항항", "DT_0050", "37", "126"))
        locationBaseList.add(SearchLocation("방포항", "SO_1260", "37", "126"))
        locationBaseList.add(SearchLocation("부산남부", "DT_0005", "35", "129"))
        locationBaseList.add(SearchLocation("부산동부", "DT_0005", "35", "129"))
        locationBaseList.add(SearchLocation("부산서부", "DT_0005", "35", "129"))
        locationBaseList.add(SearchLocation("비금도", "SO_0543", "34", "126"))
        locationBaseList.add(SearchLocation("비양도", "DT_0004", "34", "127"))
        locationBaseList.add(SearchLocation("상왕등도", "SO_1253", "36", "126"))
        locationBaseList.add(SearchLocation("상태도", "SO_1255", "34", "125"))
        locationBaseList.add(SearchLocation("서귀포", "DT_0010", "33", "127"))
        locationBaseList.add(SearchLocation("성산포", "DT_0022", "33", "127"))
        locationBaseList.add(SearchLocation("신시도", "DT_0018", "36", "127"))
        locationBaseList.add(SearchLocation("신지도", "DT_0027", "34", "127"))
        locationBaseList.add(SearchLocation("아야진항", "SO_1276", "38", "129"))
        locationBaseList.add(SearchLocation("어청도", "DT_0037", "36", "126"))
        locationBaseList.add(SearchLocation("연도", "SO_1269", "36", "126"))
        locationBaseList.add(SearchLocation("영흥도", "DT_0043", "37", "126"))
        locationBaseList.add(SearchLocation("외옹치항", "DT_0012", "38", "129"))
        locationBaseList.add(SearchLocation("욕지도", "DT_0014", "35", "128"))
        locationBaseList.add(SearchLocation("울릉도", "DT_0013", "37", "131"))
        locationBaseList.add(SearchLocation("울산", "DT_0020", "36", "129"))
        locationBaseList.add(SearchLocation("울진 후정", "SO_0735", "37", "129"))
        locationBaseList.add(SearchLocation("추자도", "DT_0021", "34", "126"))
        locationBaseList.add(SearchLocation("포항", "DT_0901", "36", "129"))
        locationBaseList.add(SearchLocation("하조도", "DT_0028", "34", "126"))
        locationBaseList.add(SearchLocation("후포", "DT_0011", "37", "129"))

    }

    fun getBasicWeatherList(response : Response<Weather>) : ArrayList<Weather.Item> {

        val list = ArrayList<Weather.Item>()
        val responseList : List<Weather.Item> = response.body()!!.response.body.items.item
        val listSize = responseList.size

        for (i in 0 until listSize) {

            if (responseList[i].category == "TMP" || responseList[i].category == "REH" ||
                responseList[i].category == "WSD" || responseList[i].category == "SKY" || responseList[i].category == "PTY") {

                list.add(responseList[i])

            }

        }

        return list

    } // getBasicWeatherList()


    fun requestWeather(
        pageNo: String,
        numOfRows: String,
        dataType: String,
        baseDate: String,
        baseTime: String,
        nx: String,
        ny: String
    ) = weatherRetrofitInterface.requestWeather(pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny)

    fun requestTide(baseDate: String, location: String, resultType: String) =
        tideRetrofitInterface.requestTide(baseDate, location, resultType)


    fun getCombineList(weatherList : ArrayList<ConditionWeather>, tideList : ArrayList<ConditionTide>, indexTotal : ArrayList<Int>): ArrayList<ConditionCombine> {

        var time : String = ""
        var indexImage : Int = 0
        var skyImage : Int = 0
        var temp : String = ""
        var tide : String = ""

        for (i in 0 until weatherList.size) {

            time = weatherList[i].time
            temp = weatherList[i].temp
            skyImage = weatherList[i].skyImage

            if (weatherList.size / 2 > i) {

                indexImage = indexTotal[0]

            } else {

                indexImage = indexTotal[1]

            }

            for (j in 0 until tideList.size) {

                if (weatherList[i].time.substring(0,2) == tideList[j].time.substring(0,2)) {

                    if (tideList[j].tide == "간조") {

                        tide = "간조 " + tideList[j].time

                    } else {

                        tide = "만조 " + tideList[j].time

                    }

                    break

                } else {

                    tide = ""

                }

            }

            val conditionCombine = ConditionCombine(time, indexImage, skyImage, temp, tide)

            combineList.add(conditionCombine)

        }

        return combineList

    } // getCombineList()


    fun getWeatherList(responseList : ArrayList<Weather.Item>, date : String): ArrayList<ConditionWeather> {

        var time: String = ""
        var temp: String = ""
        var skyImage: Int = 0
        var humidity: String = ""
        var windSpeed: String = ""

        for (i in 0 until responseList.size) {

            time = responseList[i].fcstTime.substring(0,2) + ":" + responseList[i].fcstTime.substring(2,4)

            if (responseList[i].category == "TMP") {

                temp = responseList[i].fcstValue + "˚C"

            } else if (responseList[i].category == "SKY") {

                when (responseList[i].fcstValue) {

                    "0", "1", "2", "3", "4", "5" -> skyImage = R.drawable.sun
                    "6", "7", "8" -> skyImage = R.drawable.cloudy
                    "9", "10" -> skyImage = R.drawable.fade

                }

            } else if (responseList[i].category == "PTY") {

                when (responseList[i].fcstValue) {

                    "1", "4" -> skyImage = R.drawable.rain
                    "2", "3" -> skyImage = R.drawable.snow

                }

            } else if (responseList[i].category == "WSD") {

                windSpeed = responseList[i].fcstValue + "m/s"

            } else if (responseList[i].category == "REH") {

                humidity = responseList[i].fcstValue + "%"

                val conditionWeather = ConditionWeather(time, skyImage, temp, humidity, windSpeed)
                weatherList.add(conditionWeather)

            }

        }

        return weatherList

    } // getWeatherList()


    fun getTideList(responseList: ArrayList<Tide.Item>, date : String): ArrayList<ConditionTide> {

        var time : String = ""
        var upDownImage: Int = 0
        var tide : String = ""
        var waterHeightImage: Int = 0
        var waterHeight : String = ""

        for (i in 0 until responseList.size) {

            time = responseList[i].tph_time.substring(0)
            waterHeight = responseList[i].tph_level + "cm"

            when (responseList[i].hl_code) {

                "고조" -> tide = "만조"
                "저조" -> tide = "간조"

            }

            when (responseList[i].hl_code) {

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


    fun getIndex(responseList: ArrayList<Index.Item>): ConditionIndex {

        var fishName = "우럭"
        var amWaterTemp: String = ""
        var amWaveHeight: String = ""
        var amTide: String = ""
        var amTotal: String = ""
        var amTotalImage: Int = 0
        var pmWaterTemp: String = ""
        var pmWaveHeight: String = ""
        var pmTide: String = ""
        var pmTotal: String = ""
        var pmTotalImage: Int = 0

        for (i in 0 until responseList.size) {

            if (responseList[i].date == GetDate().getFormatDate3(GetDate().getTime()) && responseList[i].fish_name == fishName && responseList[i].name == "거문도") {

                if (responseList[i].time_type == "오전") {

                    amWaterTemp = responseList[i].water_temp
                    amWaveHeight = responseList[i].wave_height
                    amTide = responseList[i].tide_time_score
                    amTotal = responseList[i].total_score

                    when (responseList[i].total_score) {

                        "매우나쁨" -> amTotalImage = R.drawable.index_very_bad
                        "나쁨" -> amTotalImage = R.drawable.index_bad
                        "보통" -> amTotalImage = R.drawable.index_normal
                        "좋음" -> amTotalImage = R.drawable.index_good
                        "매우좋음" -> amTotalImage = R.drawable.index_very_good

                    }

                } else {

                    pmWaterTemp = responseList[i].water_temp
                    pmWaveHeight = responseList[i].wave_height
                    pmTide = responseList[i].tide_time_score
                    pmTotal = responseList[i].total_score

                    when (responseList[i].total_score) {

                        "매우나쁨" -> pmTotalImage = R.drawable.index_very_bad
                        "나쁨" -> pmTotalImage = R.drawable.index_bad
                        "보통" -> pmTotalImage = R.drawable.index_normal
                        "좋음" -> pmTotalImage = R.drawable.index_good
                        "매우좋음" -> pmTotalImage = R.drawable.index_very_good

                    }

                }

            }

        }

        index = ConditionIndex(
            amWaterTemp,
            amWaveHeight,
            amTide,
            amTotal,
            amTotalImage,
            pmWaterTemp,
            pmWaveHeight,
            pmTide,
            pmTotal,
            pmTotalImage
        )

        return index

    } // getIndex()


    fun getIndexList(responseList: ArrayList<Index.Item>) : ArrayList<Int> {

        val list = ArrayList<Int>()
        var amTotal : Int = 0
        var pmTotal : Int = 0
        var amTotalSum = 0
        var pmTotalSum = 0
        val amTotalAvg : Int
        val pmTotalAvg : Int

        for (i in 0 until responseList.size) {

            if (responseList[i].time_type == "오전") {

                when (responseList[i].total_score) {

                    "매우나쁨" -> amTotalSum += 1
                    "나쁨" -> amTotalSum += 2
                    "보통" -> amTotalSum += 3
                    "좋음" -> amTotalSum += 4
                    "매우좋음" -> amTotalSum += 5

                }

            } else {

                when (responseList[i].total_score) {

                    "매우나쁨" -> pmTotalSum += 1
                    "나쁨" -> pmTotalSum += 2
                    "보통" -> pmTotalSum += 3
                    "좋음" -> pmTotalSum += 4
                    "매우좋음" -> pmTotalSum += 5

                }

            }

        }

        amTotalAvg = (amTotalSum / (responseList.size / 2)).toDouble().roundToInt()
        pmTotalAvg = (pmTotalSum / (responseList.size / 2)).toDouble().roundToInt()

        when (amTotalAvg) {

            1 -> amTotal = R.drawable.index_very_bad
            2 -> amTotal = R.drawable.index_bad
            3 -> amTotal = R.drawable.index_normal
            4 -> amTotal = R.drawable.index_good
            5 -> amTotal = R.drawable.index_very_good

        }

        when (pmTotalAvg) {

            1 -> pmTotal = R.drawable.index_very_bad
            2 -> pmTotal = R.drawable.index_bad
            3 -> pmTotal = R.drawable.index_normal
            4 -> pmTotal = R.drawable.index_good
            5 -> pmTotal = R.drawable.index_very_good

        }

        list.add(amTotal)
        list.add(pmTotal)

        return list

    } // getIndexList()


    fun getSearchLocationList(keyword: String): ArrayList<SearchLocation> {

        searchLocationList = ArrayList()

        for (i in 0 until locationBaseList.size) {

            if (keyword == "") {

                searchLocationList = locationBaseList

            } else {

                if (locationBaseList[i].location.contains(keyword)) {

                    searchLocationList.add(locationBaseList[i])

                }

            }

        }

        return searchLocationList

    } // getlocationBaseList()


}