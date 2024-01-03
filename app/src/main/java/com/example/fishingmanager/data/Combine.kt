package com.example.fishingmanager.data

data class Combine(val response: Response) {

    data class Response(
        val userInfo: UserInfo,
        val collection: ArrayList<com.example.fishingmanager.data.Collection>,
        val history: ArrayList<History>,
        val feed: ArrayList<Feed>
    )

}