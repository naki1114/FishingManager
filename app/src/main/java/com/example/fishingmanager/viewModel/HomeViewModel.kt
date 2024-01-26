package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.ConditionWeather
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
    val liveDataWeather = MutableLiveData<HomeWeather>()
    val liveDataRecommendList = MutableLiveData<ArrayList<HomeRecommend>>()

    fun getWeather() {

        liveDataWeather.value = model.getWeather(basicWeatherList, location)

    } // getWeather()


    fun getRecommendList() {

        liveDataRecommendList.value = model.getRecommendList(basicIndexList)

    } // getRecommendList()


    fun changeFragment(fragment: String) {

        liveDataChangeFragment.value = fragment

    } // changeFragment()

}