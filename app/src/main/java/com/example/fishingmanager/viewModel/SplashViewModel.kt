package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.ConditionTide
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.data.Weather
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.model.SplashModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel : ViewModel() {

    val TAG : String = "SplashViewModel"
    val model = SplashModel()

    val liveDataWeatherList = MutableLiveData<ArrayList<ConditionWeather>>()
    val liveDataTideList = MutableLiveData<ArrayList<ConditionTide>>()
    val liveDataIndexList = MutableLiveData<ArrayList<Index.Item>>()
    val liveDataCombineData = MutableLiveData<Boolean>()
    val liveDataCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataUserInfo = MutableLiveData<UserInfo>()
    val liveDataFailureWeather = MutableLiveData<Boolean>()
    val liveDataFailureTide = MutableLiveData<Boolean>()
    val liveDataFailureIndex = MutableLiveData<Boolean>()
    val liveDataFailureCombine = MutableLiveData<Boolean>()
    val liveDataFinishStatus = MutableLiveData<Boolean>()


    // Model에서 요청한 날씨 API에 대한 Callback
    fun getWeather(pageNo : String, numOfRows : String, dataType : String, baseDate : String, baseTime : String, nx : String, ny : String) {

        model.requestWeather(pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny).enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {

                    liveDataWeatherList.value = model.getWeatherList(response)

                } else {
                    Log.d(TAG, "getWeather - onResponse : isFailure : ${response.message()}")
                    liveDataFailureWeather.value = true
                    liveDataWeatherList.value = ArrayList()
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.d(TAG, "getWeather - onFailure : $t")
                liveDataFailureWeather.value = true
                liveDataWeatherList.value = ArrayList()
            }
        })

    } // getWeather()


    // Model에서 요청한 조석 API에 대한 Callback
    fun getTide(baseDate : String, location : String, resultType : String) {

        model.requestTide(baseDate, location, resultType).enqueue(object : Callback<Tide> {
            override fun onResponse(call: Call<Tide>, response: Response<Tide>) {
                if (response.isSuccessful) {

                    liveDataTideList.value = model.getTideList(response)

                } else {
                    Log.d(TAG, "getTide - onResponse : isFailure : ${response.message()}")
                    liveDataFailureTide.value = true
                    liveDataTideList.value = ArrayList()
                }
            }

            override fun onFailure(call: Call<Tide>, t: Throwable) {
                Log.d(TAG, "getTide - onFailure : $t")
                liveDataFailureTide.value = true
                liveDataTideList.value = ArrayList()
            }
        })

    } // getTide()


    // Model에서 요청한 지수 API에 대한 Callback
    fun getIndex(type : String, resultType : String) {

        model.requestIndex(type, resultType).enqueue(object : Callback<Index> {
            override fun onResponse(call: Call<Index>, response: Response<Index>) {
                if (response.isSuccessful) {

                    liveDataIndexList.value = model.getIndexList(response)

                } else {
                    Log.d(TAG, "getIndex - onResponse : isFailure : ${response.message()}")
                    liveDataFailureIndex.value = true
                    liveDataIndexList.value = ArrayList()
                }
            }

            override fun onFailure(call: Call<Index>, t: Throwable) {
                Log.d(TAG, "getIndex - onFailure : $t")
                liveDataFailureIndex.value = true
                liveDataIndexList.value = ArrayList()
            }
        })

    } // getIndex()


    // Model에서 요청한 Web Server - Database 데이터에 대한 Callback
    fun getCombine(nickname : String) {

        model.requestCombine(nickname).enqueue(object : Callback<Combine> {
            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataCollectionList.value = response.body()?.collection
                    liveDataHistoryList.value = model.getHistoryList(response.body()?.history)
                    liveDataFeedList.value = response.body()?.feed
                    liveDataUserInfo.value = response.body()?.userInfo

                    liveDataCombineData.value = true

                } else {
                    Log.d(TAG, "getCombine - onResponse : isFailure : ${response.message()}")
                    liveDataFailureCombine.value = true
                    liveDataCollectionList.value = ArrayList()
                    liveDataHistoryList.value = ArrayList()
                    liveDataFeedList.value = ArrayList()
                    liveDataUserInfo.value = UserInfo(-1, -1, -1)
                }

            }

            override fun onFailure(call: Call<Combine>, t: Throwable) {
                Log.d(TAG, "getCombine - onFailure : $t")
                liveDataFailureCombine.value = true
            }

        })

    } // getCombine()


    fun finish() {

        liveDataFinishStatus.value = true

    } // finish()


}