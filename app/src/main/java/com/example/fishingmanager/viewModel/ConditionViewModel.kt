package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.ConditionCombine
import com.example.fishingmanager.data.ConditionIndex
import com.example.fishingmanager.data.ConditionTide
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.SearchLocation
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.Weather
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.model.ConditionModel

class ConditionViewModel(
    weatherList: ArrayList<Weather.Item>,
    tideList: ArrayList<Tide.Item>,
    indexList: ArrayList<Index.Item>,
    sharedLocation : String
) : ViewModel() {

    val TAG = "ConditionViewModel"
    val model = ConditionModel()

    val liveDataCurrentLayout = MutableLiveData<String>()
    lateinit var previousLayout : String

    val liveDataCombineList = MutableLiveData<ArrayList<ConditionCombine>>()
    val liveDataWeatherList = MutableLiveData<ArrayList<ConditionWeather>>()
    val liveDataTideList = MutableLiveData<ArrayList<ConditionTide>>()
    val liveDataIndex = MutableLiveData<ConditionIndex>()
    val liveDataIndexTotal = MutableLiveData<ArrayList<Int>>()
    val liveDataSearchLocationList = MutableLiveData<ArrayList<SearchLocation>>()

    val liveDataDate = MutableLiveData<String>()
    val liveDataLocation = MutableLiveData<String>()
    val liveDataFish = MutableLiveData<String>()


    init {

        this.liveDataWeatherList.value = model.getWeatherList(weatherList)
        this.liveDataTideList.value = model.getTideList(tideList)
        this.liveDataIndex.value = model.getIndex(indexList)
        this.liveDataIndexTotal.value = model.getIndexList(indexList)
        this.liveDataCombineList.value = model.getCombineList(
            liveDataWeatherList.value!!,
            liveDataTideList.value!!,
            liveDataIndexTotal.value!!
        )
        liveDataSearchLocationList.value = model.getSearchLocationList("")
        liveDataLocation.value = sharedLocation
        liveDataCurrentLayout.value = "combine"
        liveDataDate.value = GetDate().getFormatDate3(GetDate().getTime())

    }


    fun changeLayout(layoutName: String) {

        previousLayout = liveDataCurrentLayout.value!!
        liveDataCurrentLayout.value = layoutName

    } // changeLayout()


    fun locationSearchAfterTextChanged(keyword : String) {

        liveDataSearchLocationList.value = model.getSearchLocationList(keyword)

    } // locationSearchAfterTextChanged


    fun setLocation(location : SearchLocation) {

        liveDataLocation.value = location.location
        liveDataCurrentLayout.value = previousLayout

    } // setLocation()


}