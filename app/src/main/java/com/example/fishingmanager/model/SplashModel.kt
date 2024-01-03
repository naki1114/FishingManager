package com.example.fishingmanager.model

import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface

class SplashModel {

    private val weatherRetrofitInterface: RetrofitInterface =
        RetrofitClient().getWeatherAPI().create(RetrofitInterface::class.java)

    private val tideRetrofitInterface: RetrofitInterface =
        RetrofitClient().getTideAPI().create(RetrofitInterface::class.java)

    private val indexRetrofitInterface: RetrofitInterface =
        RetrofitClient().getIndexAPI().create(RetrofitInterface::class.java)

    private val webServerRetrofitInterface: RetrofitInterface =
        RetrofitClient().getWebServer().create(RetrofitInterface::class.java)

    fun getWeather(
        pageNo: String,
        numOfRows: String,
        dataType: String,
        baseDate: String,
        baseTime: String,
        nx: String,
        ny: String
    ) = weatherRetrofitInterface.requestWeather(pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny)

    fun getTide(baseDate: String, location: String, resultType: String) =
        tideRetrofitInterface.requestTide(baseDate, location, resultType)

    fun getIndex(type: String, resultType: String) =
        indexRetrofitInterface.requestIndex(type, resultType)

    fun getCombine(nickname : String) =
        webServerRetrofitInterface.requestDB(nickname)

}