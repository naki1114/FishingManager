package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.model.FeedModel

class FeedViewModel(val eedList : ArrayList<Feed>) : ViewModel() {

    private var TAG = "FeedViewModel"

    val toWriteLiveData = MutableLiveData<Boolean>()

    private val model = FeedModel()

    fun searchFeed(type : String, keyword : String) {
        Log.d(TAG, "spinner : $type, keyword : $keyword")
    } // searchFeed

    fun addFeed() {

        toWriteLiveData.value = true

    } // addFeed

}