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
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.Weather
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.model.ConditionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConditionViewModel(
    weatherList: ArrayList<ConditionWeather>,
    tideList: ArrayList<ConditionTide>,
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
    val liveDataFishList = MutableLiveData<ArrayList<SelectFish>>()
    val liveDataLoadingStatus = MutableLiveData<Boolean>()

    val basicWeatherList = weatherList
    val basicIndexList = indexList
    var callBackStatus = 0


    init {

        liveDataSearchLocationList.value = model.getSearchLocationList("")
        liveDataSearchLocation.value = searchLocation
        liveDataCurrentLayout.value = "combine"
        liveDataDate.value = GetDate().getFormatDate3(GetDate().getTime())
        liveDataFishList.value = model.getFishList(indexList, searchLocation, liveDataDate.value!!)
        liveDataFish.value = model.getFish(liveDataFishList.value!!)

        liveDataWeatherList.value = model.changeWeatherList(weatherList, liveDataDate.value!!)
        liveDataTideList.value = tideList
        liveDataIndex.value = model.getIndex(indexList, liveDataDate.value!!, liveDataFish.value!!, liveDataSearchLocation.value!!.location)
        liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(indexList, searchLocation.location, liveDataDate.value!!))
        liveDataCombineList.value = model.getCombineList(
            liveDataWeatherList.value!!,
            liveDataTideList.value!!,
            liveDataIndexTotal.value!!
        )

    }


    fun changeLayout(layoutName: String) {

        previousLayout = liveDataCurrentLayout.value!!
        liveDataCurrentLayout.value = layoutName

    } // changeLayout()


    fun locationSearchAfterTextChanged(keyword : String) {

        liveDataSearchLocationList.value = model.getSearchLocationList(keyword)

    } // locationSearchAfterTextChanged


    fun changeDate(date : String) {

        liveDataLoadingStatus.value = true
        liveDataDate.value = date
        liveDataCurrentLayout.value = previousLayout
        liveDataWeatherList.value = model.changeWeatherList(basicWeatherList, liveDataDate.value!!)
        liveDataIndex.value = model.getIndex(
            basicIndexList,
            liveDataDate.value!!,
            liveDataFish.value!!,
            liveDataSearchLocation.value!!.location
        )

        var requestDate = ""
        requestDate = liveDataDate.value!!.substring(0,4) + liveDataDate.value!!.substring(5,7) + liveDataDate.value!!.substring(8,10)

        model.requestTide(requestDate, liveDataSearchLocation.value!!.obsCode, "json").enqueue(object : Callback<Tide> {
            override fun onResponse(call: Call<Tide>, response: Response<Tide>) {
                if (response.isSuccessful) {

                    liveDataTideList.value = model.getTideList(response)

                    liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(basicIndexList, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                    liveDataCombineList.value = model.getCombineList(
                        liveDataWeatherList.value!!,
                        liveDataTideList.value!!,
                        liveDataIndexTotal.value!!
                    )

                    liveDataLoadingStatus.value = false

                } else {

                    Log.d(TAG, "onResponse : isFailure : ${response.message()}")

                }
            }

            override fun onFailure(call: Call<Tide>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
            }

        })

    } // changeDate()


    fun changeLocation(location : SearchLocation) {

        callBackStatus = 0
        liveDataLoadingStatus.value = true

        liveDataSearchLocation.value = location
        liveDataCurrentLayout.value = previousLayout

        liveDataFishList.value =
            model.getFishList(basicIndexList, liveDataSearchLocation.value!!, liveDataDate.value!!)
        liveDataFish.value = model.getFish(liveDataFishList.value!!)

        liveDataIndex.value = model.getIndex(
            basicIndexList,
            liveDataDate.value!!,
            liveDataFish.value!!,
            liveDataSearchLocation.value!!.location
        )

        model.requestWeather("1", "1000", "JSON", GetDate().getFormatDate4(GetDate().getTime()), "2300", liveDataSearchLocation.value!!.lat, liveDataSearchLocation.value!!.lon)
            .enqueue(object : Callback<Weather> {
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                    if (response.isSuccessful) {

                        liveDataWeatherList.value =
                            model.getWeatherList(response, liveDataDate.value!!)

                        liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(basicIndexList, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                        callBackStatus++

                        if (callBackStatus == 2) {

                            liveDataCombineList.value = model.getCombineList(
                                liveDataWeatherList.value!!,
                                liveDataTideList.value!!,
                                liveDataIndexTotal.value!!
                            )

                            liveDataLoadingStatus.value = false

                        }

                    } else {
                        Log.d(TAG, "onResponse : isFailure : ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Log.d(TAG, "onFailure : $t")
                }

            })

        var requestDate = ""
        requestDate = liveDataDate.value!!.substring(0,4) + liveDataDate.value!!.substring(5,7) + liveDataDate.value!!.substring(8,10)

        model.requestTide(requestDate, liveDataSearchLocation.value!!.obsCode, "json").enqueue(object : Callback<Tide> {
            override fun onResponse(call: Call<Tide>, response: Response<Tide>) {
                if (response.isSuccessful) {

                    liveDataTideList.value = model.getTideList(response)

                    liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(basicIndexList, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                    callBackStatus++

                    if (callBackStatus == 2) {

                        liveDataCombineList.value = model.getCombineList(
                            liveDataWeatherList.value!!,
                            liveDataTideList.value!!,
                            liveDataIndexTotal.value!!
                        )

                        liveDataLoadingStatus.value = false

                    }

                } else {

                    Log.d(TAG, "onResponse : isFailure : ${response.message()}")

                }
            }

            override fun onFailure(call: Call<Tide>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
            }

        })

    } // changeLocation()


    fun changeFish(fishName : String) {

        liveDataFish.value = fishName

        liveDataIndex.value = model.getIndex(basicIndexList, liveDataDate.value!!, liveDataFish.value!!, liveDataSearchLocation.value!!.location)

        liveDataCurrentLayout.value = previousLayout

    } // changeFish()


}