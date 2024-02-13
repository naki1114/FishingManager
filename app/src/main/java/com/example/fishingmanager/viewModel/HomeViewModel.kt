package com.example.fishingmanager.viewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.ConditionIndex
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.HomeRecentCollection
import com.example.fishingmanager.data.HomeRecommend
import com.example.fishingmanager.data.HomeWeather
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.SearchLocation
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.data.Weather
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.model.HomeModel
import com.example.fishingmanager.model.SplashModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    weatherList: ArrayList<ConditionWeather>,
    location: SearchLocation,
    nickname: String,
    indexList: ArrayList<Index.Item>,
    historyList : ArrayList<History>,
    feedList : ArrayList<Feed>
) : ViewModel() {

    val TAG = "HomeViewModel"

    val model = HomeModel()
    val splashModel = SplashModel()

    var basicWeatherList = weatherList
    var basicIndexList = indexList
    val location = location
    val nickname = nickname
    var basicHistoryList = historyList
    var basicFeedList = feedList

    val liveDataChangeFragment = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataWeather = MutableLiveData<HomeWeather>()
    val liveDataRecommendList = MutableLiveData<ArrayList<HomeRecommend>>()
    val liveDataClickedFishImage = MutableLiveData<String>()
    val liveDataRecentCollectionList = MutableLiveData<ArrayList<History>>()
    val liveDataHotFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataHotFeedNum = MutableLiveData<Int>()

    val liveDataBasicWeatherList = MutableLiveData<ArrayList<ConditionWeather>>()
    val liveDataBasicIndexList = MutableLiveData<ArrayList<Index.Item>>()
    val liveDataBasicCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataBasicHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataBasicFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataBasicUserInfo = MutableLiveData<UserInfo>()

    val liveDataWeatherLoadingStatus = MutableLiveData<Boolean>()
    val liveDataIndexLoadingStatus = MutableLiveData<Boolean>()
    val liveDataCombineLoadingStatus = MutableLiveData<Boolean>()


    fun getWeather() {

        liveDataWeather.value = model.getWeather(basicWeatherList, location.location)

    } // getWeather()


    fun getRecommendList() {

        liveDataRecommendList.value = model.getRecommendList(basicIndexList)

    } // getRecommendList()


    fun getRecentCollectionList() {

        liveDataRecentCollectionList.value = model.getRecentCollectionList(basicHistoryList)

    } // getRecentCollectionList()


    fun getHotFeedList() {

        liveDataHotFeedList.value = model.getHotFeedList(basicFeedList)

    } // getHotFeedList()


    fun changeFragment(fragment: String) {

        liveDataChangeFragment.value = fragment

    } // changeFragment()


    fun goPhotoView(image: String) {

        liveDataClickedFishImage.value = image

    } // goPhotoView()


    fun changeLayout(layout: String) {

        liveDataChangeLayout.value = layout

    }


    fun goHotFeed(feedNum: Int) {

        liveDataHotFeedNum.value = feedNum

    } // goHotFeed()


    fun requestWeather() {

        liveDataWeatherLoadingStatus.value = true

        model.requestWeather("1", "1000", "JSON", GetDate().getFormatDate4(GetDate().getTime()), "2300", location.lat, location.lon)
            .enqueue(object : Callback<Weather> {

            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: 들어옴!!!")
                    liveDataBasicWeatherList.value = splashModel.getWeatherList(response)
                    basicWeatherList = liveDataBasicWeatherList.value!!

                    getWeather()

                    liveDataWeatherLoadingStatus.value = false

                } else {
                    Log.d(TAG, "requestWeather - onResponse : isFailure : ${response.message()}")
                    liveDataWeather.value = HomeWeather("", 0, "", "", "")
                }

            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.d(TAG, "requestWeather - onFailure : $t")
                liveDataWeather.value = HomeWeather("", 0, "", "", "")
            }

        })

    } // requestWeather()


    fun requestIndex() {

        liveDataIndexLoadingStatus.value = true

        model.requestIndex("SF", "json").enqueue(object  : Callback<Index> {

            override fun onResponse(call: Call<Index>, response: Response<Index>) {

                if (response.isSuccessful) {

                    liveDataBasicIndexList.value = splashModel.getIndexList(response)
                    basicIndexList = liveDataBasicIndexList.value!!

                    getRecommendList()

                    liveDataIndexLoadingStatus.value = false

                } else {
                    Log.d(TAG, "requestIndex - onResponse : isFailure : ${response.message()}")
                    liveDataRecommendList.value = ArrayList()
                }


            }

            override fun onFailure(call: Call<Index>, t: Throwable) {
                Log.d(TAG, "requestIndex - onFailure : $t")
                liveDataRecommendList.value = ArrayList()
            }

        })

    } // requestIndex()


    fun requestCombine() {

        liveDataCombineLoadingStatus.value = true

        model.requestCombine(nickname).enqueue(object : Callback<Combine> {

            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataBasicCollectionList.value = response.body()?.collection
                    liveDataBasicHistoryList.value = response.body()?.history
                    liveDataBasicFeedList.value = response.body()?.feed
                    liveDataBasicUserInfo.value = response.body()?.userInfo

                    basicHistoryList = liveDataBasicHistoryList.value!!
                    basicFeedList = liveDataBasicFeedList.value!!

                    getRecentCollectionList()
                    getHotFeedList()

                    liveDataCombineLoadingStatus.value = false

                } else {
                    Log.d(TAG, "requestCombine - onResponse : isFailure : ${response.message()}")
                    liveDataRecentCollectionList.value = ArrayList()
                    liveDataHotFeedList.value = ArrayList()
                }

            }

            override fun onFailure(call: Call<Combine>, t: Throwable) {
                Log.d(TAG, "requestCombine - onFailure : $t")
                liveDataRecentCollectionList.value = ArrayList()
                liveDataHotFeedList.value = ArrayList()
            }

        })

    } // requestCombine()


}