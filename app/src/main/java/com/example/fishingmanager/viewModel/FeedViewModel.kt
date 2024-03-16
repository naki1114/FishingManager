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

class FeedViewModel(val nickname: String): ViewModel() {

    private var TAG = "FeedViewModel"

    val toReadLiveData = MutableLiveData<Feed>()
    val toWriteLiveData = MutableLiveData<Boolean>()
    val toFeedListLiveData = MutableLiveData<Boolean>()
    val searchLiveData = MutableLiveData<ArrayList<String>>()
    val feedList = MutableLiveData<ArrayList<Feed>>()
    val feedCommentList = MutableLiveData<ArrayList<Comment>>()

    private val model = FeedModel()

    lateinit var feed: Feed


    // 게시글 검색
    fun searchFeed(type: String, keyword: String) {

        val search = arrayListOf(type, keyword)
        searchLiveData.value = search

    } // searchFeed()


    // 특정 게시글 상세 보기
    fun readFeed(feed: Feed) {

        this.feed = feed

        // 게시글 진입시 조회수 업데이트
        model.updateViewCount(feed.nickname, feed.date).enqueue(object: Callback<ArrayList<Feed>> {

            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {

                if (response.isSuccessful) {

                    val msg = response.body()

                    feedList.value = msg

                }

            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

        // 게시글 진입시 댓글 조회
        model.getComment(feed.feedNum).enqueue(object: Callback<ArrayList<Comment>> {

            override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {

                if (response.isSuccessful) {

                    val msg = response.body()

                    feedCommentList.value = msg

                }

            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

        toReadLiveData.value = feed

    } // readFeed()


    // 게시글 추가
    fun addFeed() {

        toWriteLiveData.value = true

    } // addFeed()


    // 게시글 목록 레이아웃으로 전환
    fun toFeedListLayout() {

        toFeedListLiveData.value = true

    } // toFeedListLayout()


    // 게시글 목록 조회
    fun getFeed() {

        model.getFeed().enqueue(object: Callback<ArrayList<Feed>> {

            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {

                if (response.isSuccessful) {

                    val msg = response.body()

                    feedList.value = msg

                }
            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {

                Log.d(TAG, "onFailure")

            }

        })

    } // getFeed()


    // 댓글 입력
    fun insertComment(content: String) {

        val feedNum = feed.feedNum.toString()
        val date = System.currentTimeMillis().toString()

        if (content.isNotEmpty()) {

            model.insertComment(nickname, feedNum, content, date).enqueue(object: Callback<ArrayList<Comment>> {

                override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {

                    if (response.isSuccessful) {

                        val msg = response.body()

                        feedCommentList.value = msg

                    }

                }

                override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {

                    Log.d(TAG, "onFailure")

                }

            })

        }

    } // insertComment()


    // Home Fragment에서 HOT 게시글 클릭했을 때 게시글 상세 보기
    fun goFeedView (feed: Feed) {

        toReadLiveData.value = feed

    } // goFeedView()


}