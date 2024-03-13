package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Comment
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.model.FeedModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedViewModel(val nickname : String) : ViewModel() {

    private var TAG = "FeedViewModel"

    val toReadLiveData = MutableLiveData<Feed>()
    val toWriteLiveData = MutableLiveData<Boolean>()
    val toFeedListLiveData = MutableLiveData<Boolean>()
    val searchLiveData = MutableLiveData<ArrayList<String>>()
    val feedList = MutableLiveData<ArrayList<Feed>>()
    val feedCommentList = MutableLiveData<ArrayList<Comment>>()

    private val model = FeedModel()

    lateinit var feed : Feed

    fun searchFeed(type : String, keyword : String) {
        val search = arrayListOf(type, keyword)
        searchLiveData.value = search
    } // searchFeed

    fun readFeed(feed : Feed) {

        this.feed = feed

        model.updateViewCount(feed.nickname, feed.date).enqueue(object : Callback<ArrayList<Feed>> {

            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {
                if (response.isSuccessful) {
                    var msg = response.body()

                    feedList.value = msg
                }
            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {

            }

        })

        model.getComment(feed.feedNum).enqueue(object : Callback<ArrayList<Comment>> {

            override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {
                if (response.isSuccessful) {
                    var msg = response.body()

                    feedCommentList.value = msg
                }
            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {

            }

        })

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

    } // getFeed

    fun insertComment(content : String) {

        val feedNum = feed.feedNum.toString()
        val date = System.currentTimeMillis().toString()

        if (content.isNotEmpty()) {

            model.insertComment(nickname, feedNum, content, date).enqueue(object : Callback<ArrayList<Comment>> {

                override fun onResponse(call : Call<ArrayList<Comment>>, response : Response<ArrayList<Comment>>) {
                    if (response.isSuccessful) {
                        var msg = response.body()

                        feedCommentList.value = msg
                    }
                }

                override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                    Log.d(TAG, "onFailure")
                }

            })

        }

    } // insertComment


    fun goFeedView (feed : Feed) {

        toReadLiveData.value = feed

    }

}