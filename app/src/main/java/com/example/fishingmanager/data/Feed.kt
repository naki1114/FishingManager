package com.example.fishingmanager.data

import java.io.Serializable

data class Feed(
    val nickname: String,
    val feedNum: Int,
    val title: String,
    val content: String,
    val feedImage: String,
    val viewCount: String,
    val date: String,
    val profileImage : String
) : Serializable