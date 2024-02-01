package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.model.FeedModel

class FeedViewModel(val feedList : ArrayList<Feed>) : ViewModel() {

    val toReadLiveData = MutableLiveData<Feed>()
    val toWriteLiveData = MutableLiveData<Boolean>()
    val toFeedListLiveData = MutableLiveData<Boolean>()
    val searchLiveData = MutableLiveData<ArrayList<String>>()

    private val model = FeedModel()

    fun searchFeed(type : String, keyword : String) {
        val search = arrayListOf(type, keyword)
        searchLiveData.value = search
    } // searchFeed

    fun readFeed(feed : Feed) {

        toReadLiveData.value = feed

    } // readFeed

    fun addFeed() {

        toWriteLiveData.value = true

    } // addFeed

    fun toFeedListLayout() {

        toFeedListLiveData.value = true

    } // toFeedListLayout

}