package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.HomeRecentCollection
import com.example.fishingmanager.data.HomeRecommend
import com.example.fishingmanager.data.HomeWeather
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.model.HomeModel

class HomeViewModel(weatherList: ArrayList<ConditionWeather>, location: String, indexList : ArrayList<Index.Item>) : ViewModel() {

    val model = HomeModel()
    val basicWeatherList = weatherList
    val basicIndexList = indexList
    val location = location

    val liveDataChangeFragment = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataWeather = MutableLiveData<HomeWeather>()
    val liveDataRecommendList = MutableLiveData<ArrayList<HomeRecommend>>()
    val liveDataClickedFishImage = MutableLiveData<String>()
    val liveDataRecentCollectionList = MutableLiveData<ArrayList<HomeRecentCollection>>()
    val liveDataHotFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataHotFeedNum = MutableLiveData<Int>()


    fun getWeather() {

        liveDataWeather.value = model.getWeather(basicWeatherList, location)

    } // getWeather()


    fun getRecommendList() {

        liveDataRecommendList.value = model.getRecommendList(basicIndexList)

    } // getRecommendList()


    fun getRecentCollectionList() {

        liveDataRecentCollectionList.value = model.getRecentCollectionList()

    } // getRecentCollectionList()


    fun getHotFeedList() {

        liveDataHotFeedList.value = model.getHotFeedList()

    } // getHotFeedList()


    fun changeFragment(fragment: String) {

        liveDataChangeFragment.value = fragment

    } // changeFragment()


    fun goPhotoView(image : String) {

        liveDataClickedFishImage.value = image

    } // goPhotoView()


    fun changeLayout(layout : String) {

        liveDataChangeLayout.value = layout

    }


    fun goHotFeed(feedNum : Int) {

        liveDataHotFeedNum.value = feedNum

    } // goHotFeed()

}