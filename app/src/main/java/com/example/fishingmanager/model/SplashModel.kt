package com.example.fishingmanager.model

import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.Weather
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.network.RetrofitInterface

class SplashModel {

    private val weatherRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWeatherAPI().create(RetrofitInterface::class.java)

    private val tideRetrofitInterface: RetrofitInterface =
        RetrofitClient.getTideAPI().create(RetrofitInterface::class.java)

    private val indexRetrofitInterface: RetrofitInterface =
        RetrofitClient.getIndexAPI().create(RetrofitInterface::class.java)

    private val webServerRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

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


    fun requestIndex(type: String, resultType: String) =
        indexRetrofitInterface.requestIndex(type, resultType)


    fun requestCombine(nickname : String) =
        webServerRetrofitInterface.requestDB(nickname)


    fun getWeatherList(response : retrofit2.Response<Weather>) : ArrayList<Weather.Item> {

        val list = ArrayList<Weather.Item>()
        val responseList : List<Weather.Item> = response.body()!!.response.body.items.item
        val listSize = responseList.size

        for (i in 0 until listSize) {

            if (responseList[i].fcstDate == GetDate().getFormatDate2(GetDate().getTime())) {

                if (responseList[i].category == "TMP" || responseList[i].category == "REH" || responseList[i].category == "WSD" || responseList[i].category == "SKY") {

                    list.add(responseList[i])

                }

            }

        }

        return list

    } // getWeatherList()


    fun getTideList(response : retrofit2.Response<Tide>) : ArrayList<Tide.Item> {

        val list = ArrayList<Tide.Item>()
        val responseList : ArrayList<Tide.Item> = response.body()!!.result.data
        val listSize = responseList.size
        var tideString = ""

        for (i in 0 until listSize) {

            if (responseList[i].hl_code == "고조") {

                tideString = responseList[i].tph_time.substring(11, 16)

            } else {

                tideString = responseList[i].tph_time.substring(11, 16)

            }

            list.add(Tide.Item(responseList[i].tph_level, tideString, responseList[i].hl_code))

        }

        return list

    } // getTideList()


    fun getIndexList(response : retrofit2.Response<Index>) : ArrayList<Index.Item> {

        val list = ArrayList<Index.Item>()
        val responseList : ArrayList<Index.Item> = response.body()!!.result.data
        val listSize = responseList.size

        for (i in 0 until listSize) {

            if (responseList[i].date == GetDate().getFormatDate3(GetDate().getTime())) {

                list.add(responseList[i])

            }

        }

        return list

    } // getIndexList()


}