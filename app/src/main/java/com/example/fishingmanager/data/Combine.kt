package com.example.fishingmanager.data

data class Combine(val userInfo: UserInfo,
                   val collection: ArrayList<Collection>,
                   val history: ArrayList<History>,
                   val feed: ArrayList<Feed>)