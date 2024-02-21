package com.example.fishingmanager.model

import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface

class FeedModel {

    private val responseServer = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    fun getFeed() = responseServer.getFeed()

    fun updateViewCount(nickname : String, date : String) = responseServer.updateViewCount(nickname, date)

    fun getComment(feedNum : Int) = responseServer.getComment(feedNum)

    fun insertComment(nickname : String, feedNum : String, content : String, date : String) = responseServer.insertComment(nickname, feedNum, content, date)

}