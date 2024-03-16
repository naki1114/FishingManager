package com.example.fishingmanager.model

import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import okhttp3.MultipartBody

class WriteModel {

    private val responseServer = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    fun insertFeed(nickname: String, title: String, content: String, date: String) = responseServer.insertFeed(nickname, title, content, date)

    fun insertImageFeed(file: MultipartBody.Part, nickname: String, title: String, content: String, date: String) = responseServer.insertImageFeed(file, nickname, title, content, date)

}