package com.example.fishingmanager.model

import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface

class FeedModel {

    private val responseServer = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)


    // Retrofit) 게시글 데이터 서버로부터 가져오기
    fun getFeed() = responseServer.getFeed()


    // Retrofit) 게시글 진입시 조회수 업데이트
    fun updateViewCount(nickname: String, date: String) = responseServer.updateViewCount(nickname, date)


    // Retrofit) 댓글 목록 서버로부터 가져오기
    fun getComment(feedNum: Int) = responseServer.getComment(feedNum)


    // Retrofit) 댓글 저장
    fun insertComment(nickname: String, feedNum: String, content: String, date: String) = responseServer.insertComment(nickname, feedNum, content, date)


}