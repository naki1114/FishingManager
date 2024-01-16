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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConditionViewModel(
    weatherList: ArrayList<ConditionWeather>,
    tideList: ArrayList<Tide.Item>,
    indexList: ArrayList<Index.Item>,
    searchLocation: SearchLocation
) : ViewModel() {

    val model = ConditionModel()
    val TAG = "ConditionViewModel"

    val liveDataCurrentLayout = MutableLiveData<String>()
    lateinit var previousLayout : String

    val liveDataCombineList = MutableLiveData<ArrayList<ConditionCombine>>()
    val liveDataWeatherList = MutableLiveData<ArrayList<ConditionWeather>>()
    val liveDataTideList = MutableLiveData<ArrayList<ConditionTide>>()
    val liveDataIndex = MutableLiveData<ConditionIndex>()
    val liveDataIndexTotal = MutableLiveData<ArrayList<Int>>()
    val liveDataSearchLocationList = MutableLiveData<ArrayList<SearchLocation>>()

    val liveDataDate = MutableLiveData<String>()
    val liveDataSearchLocation = MutableLiveData<SearchLocation>()
    val liveDataFish = MutableLiveData<String>()


    init {

        this.liveDataWeatherList.value = weatherList
        this.liveDataTideList.value = model.getTideList(tideList, GetDate().getFormatDate2(GetDate().getTime()))
        this.liveDataIndex.value = model.getIndex(indexList)
        this.liveDataIndexTotal.value = model.getIndexList(indexList)
        this.liveDataCombineList.value = model.getCombineList(
            liveDataWeatherList.value!!,
            liveDataTideList.value!!,
            liveDataIndexTotal.value!!
        )
        liveDataSearchLocationList.value = model.getSearchLocationList("")
        liveDataSearchLocation.value = searchLocation
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

        liveDataSearchLocation.value = location
        liveDataCurrentLayout.value = previousLayout

    } // setLocation()

    fun changeDate(date : String) {

        model.requestWeather("1", "1000", "JSON", date, "2300", liveDataSearchLocation.value!!.lat, liveDataSearchLocation.value!!.lon)
            .enqueue(object : Callback<Weather> {
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                    if (response.isSuccessful) {

                        val basicWeatherList = model.getBasicWeatherList(response)

                        liveDataWeatherList.value = model.getWeatherList(basicWeatherList, date)

                    } else {
                        Log.d(TAG, "onResponse : isFailure : ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Log.d(TAG, "onFailure : $t")
                }

            })

    } // changeDate()


}