package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
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
    val location: SearchLocation,
    val nickname: String,
    indexList: ArrayList<Index.Item>,
    historyList: ArrayList<History>,
    feedList: ArrayList<Feed>
): ViewModel() {

    private val TAG = "HomeViewModel"

    val model = HomeModel()
    val splashModel = SplashModel()

    var basicWeatherList = weatherList
    var basicIndexList = indexList
    var basicHistoryList = historyList
    var basicFeedList = feedList

    val liveDataChangeFragment = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataWeather = MutableLiveData<HomeWeather>()
    val liveDataRecommendList = MutableLiveData<ArrayList<HomeRecommend>>()
    val liveDataClickedFishImage = MutableLiveData<String>()
    val liveDataRecentCollectionList = MutableLiveData<ArrayList<History>>()
    val liveDataHotFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataHotFeedValue = MutableLiveData<Feed>()

    val liveDataBasicWeatherList = MutableLiveData<ArrayList<ConditionWeather>>()
    val liveDataBasicIndexList = MutableLiveData<ArrayList<Index.Item>>()
    val liveDataBasicCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataBasicHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataBasicFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataBasicUserInfo = MutableLiveData<UserInfo>()

    val liveDataWeatherLoadingStatus = MutableLiveData<Boolean>()
    val liveDataIndexLoadingStatus = MutableLiveData<Boolean>()
    val liveDataCombineLoadingStatus = MutableLiveData<Boolean>()


    // Weather 데이터
    fun getWeather() {

        liveDataWeather.value = model.getWeather(basicWeatherList, location.location)

    } // getWeather()


    // 추천 어종 목록
    fun getRecommendList() {

        liveDataRecommendList.value = model.getRecommendList(basicIndexList)

    } // getRecommendList()


    // 최근 잡은 물고기 목록
    fun getRecentCollectionList() {

        liveDataRecentCollectionList.value = model.getRecentCollectionList(basicHistoryList)

    } // getRecentCollectionList()


    // HOT 게시글 목록
    fun getHotFeedList() {

        liveDataHotFeedList.value = model.getHotFeedList(basicFeedList)

    } // getHotFeedList()


    // 프래그먼트 전환
    fun changeFragment(fragment: String) {

        liveDataChangeFragment.value = fragment

    } // changeFragment()


    // 사진 크게 보기
    fun goPhotoView(image: String) {

        liveDataClickedFishImage.value = image

    } // goPhotoView()


    // 레이아웃 전환
    fun changeLayout(layout: String) {

        liveDataChangeLayout.value = layout

    } // changeLayout()


    // HOT 게시글로 이동
    fun goHotFeed(feed: Feed) {

        liveDataHotFeedValue.value = feed

    } // goHotFeed()


    // Retrofit) Weather 데이터 요청
    fun requestWeather() {

        liveDataWeatherLoadingStatus.value = true

        model.requestWeather("1", "1000", "JSON", GetDate().getFormatDate4(GetDate().getTime()), "2300", location.lat, location.lon)
            .enqueue(object: Callback<Weather> {

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
                    liveDataWeatherLoadingStatus.value = false

                }

            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {

                Log.d(TAG, "requestWeather - onFailure : $t")
                liveDataWeather.value = HomeWeather("", 0, "", "", "")
                liveDataWeatherLoadingStatus.value = false

            }

        })

    } // requestWeather()


    // Retrofit) Index 데이터 요청
    fun requestIndex() {

        liveDataIndexLoadingStatus.value = true

        model.requestIndex("SF", "json").enqueue(object: Callback<Index> {

            override fun onResponse(call: Call<Index>, response: Response<Index>) {

                if (response.isSuccessful) {

                    liveDataBasicIndexList.value = splashModel.getIndexList(response)
                    basicIndexList = liveDataBasicIndexList.value!!

                    getRecommendList()

                    liveDataIndexLoadingStatus.value = false

                } else {

                    Log.d(TAG, "requestIndex - onResponse : isFailure : ${response.message()}")
                    liveDataRecommendList.value = ArrayList()
                    liveDataIndexLoadingStatus.value = false

                }

            }

            override fun onFailure(call: Call<Index>, t: Throwable) {

                Log.d(TAG, "requestIndex - onFailure : $t")
                liveDataRecommendList.value = ArrayList()
                liveDataIndexLoadingStatus.value = false

            }

        })

    } // requestIndex()


    // Retrofit) Combine 데이터 요청
    fun requestCombine() {

        liveDataCombineLoadingStatus.value = true

        model.requestCombine(nickname).enqueue(object: Callback<Combine> {

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
                    liveDataCombineLoadingStatus.value = false

                }

            }

            override fun onFailure(call: Call<Combine>, t: Throwable) {

                Log.d(TAG, "requestCombine - onFailure : $t")
                liveDataRecentCollectionList.value = ArrayList()
                liveDataHotFeedList.value = ArrayList()
                liveDataCombineLoadingStatus.value = false

            }

        })

    } // requestCombine()


}