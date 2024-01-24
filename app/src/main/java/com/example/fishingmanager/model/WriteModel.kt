package com.example.fishingmanager.model

import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface

class WriteModel {

    private val responseServer = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    fun insertFeed(nickname : String, title : String, content : String, date : Int) = responseServer.insertFeed(nickname, title, content, date)

}