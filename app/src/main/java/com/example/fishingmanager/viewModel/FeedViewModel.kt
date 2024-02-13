package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.model.FeedModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedViewModel : ViewModel() {

    private var TAG = "FeedViewModel"

    val toReadLiveData = MutableLiveData<Feed>()
    val toWriteLiveData = MutableLiveData<Boolean>()
    val toFeedListLiveData = MutableLiveData<Boolean>()
    val searchLiveData = MutableLiveData<ArrayList<String>>()
    val feedList = MutableLiveData<ArrayList<Feed>>()

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

    fun getFeed() {

        model.getFeed().enqueue(object : Callback<ArrayList<Feed>> {

            override fun onResponse(call : Call<ArrayList<Feed>>, response : Response<ArrayList<Feed>>) {
                if (response.isSuccessful) {
                    var msg = response.body()

                    feedList.value = msg
                }
            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {
                Log.d(TAG, "onFailure")
            }

        })

    }

}