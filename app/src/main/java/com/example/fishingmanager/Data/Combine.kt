package com.example.fishingmanager.Data

import kotlin.collections.Collection

data class Combine(val response: Response) {

    data class Response(
        val userInfo: UserInfo,
        val collection: ArrayList<com.example.fishingmanager.Data.Collection>,
        val history: ArrayList<History>,
        val feed: ArrayList<Feed>
    )

}