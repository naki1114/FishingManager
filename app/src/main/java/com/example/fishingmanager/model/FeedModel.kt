package com.example.fishingmanager.model

import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface

class FeedModel {

    private val responseServer = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    fun getFeed() = responseServer.getFeed()

}