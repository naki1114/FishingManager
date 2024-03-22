package com.example.fishingmanager.data

data class UserInfo(
    val nickname : String,
    val profileImage : String,
    var checkingFishCount: Int,
    val checkingFishTicket: Int,
    val removeAdTicket: Int,
    val type : String
)
