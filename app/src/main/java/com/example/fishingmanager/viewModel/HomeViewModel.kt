package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.HomeWeather
import com.example.fishingmanager.model.HomeModel

class HomeViewModel(weatherList: ArrayList<ConditionWeather>, location: String) : ViewModel() {

    val model = HomeModel()
    val basicWeatherList = weatherList
    val liveDataWeather = MutableLiveData<HomeWeather>()
    val location = location

    fun getWeather() {

        liveDataWeather.value = model.getWeather(basicWeatherList, location)

    }

}