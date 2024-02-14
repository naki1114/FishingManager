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
import com.example.fishingmanager.model.SplashModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConditionViewModel(
    weatherList: ArrayList<ConditionWeather>,
    tideList: ArrayList<ConditionTide>,
    indexList: ArrayList<Index.Item>,
    searchLocation: SearchLocation,
    nickname: String
) : ViewModel() {

    val model = ConditionModel()
    val splashModel = SplashModel()
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
    val liveDataRefreshLoadingStatus = MutableLiveData<Boolean>()

    val liveDataBasicWeatherList = MutableLiveData<ArrayList<ConditionWeather>>()
    val liveDataBasicTideList = MutableLiveData<ArrayList<ConditionTide>>()
    val liveDataBasicIndexList = MutableLiveData<ArrayList<Index.Item>>()
    var callBackStatus = 0
    val nickname = nickname


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
        liveDataBasicWeatherList.value = weatherList
        liveDataBasicTideList.value = tideList
        liveDataBasicIndexList.value = indexList

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
        liveDataWeatherList.value = model.changeWeatherList(liveDataBasicWeatherList.value!!, liveDataDate.value!!)
        liveDataIndex.value = model.getIndex(
            liveDataBasicIndexList.value!!,
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

                    liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(liveDataBasicIndexList.value!!, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                    liveDataCombineList.value = model.getCombineList(
                        liveDataWeatherList.value!!,
                        liveDataTideList.value!!,
                        liveDataIndexTotal.value!!
                    )

                    liveDataLoadingStatus.value = false

                } else {

                    Log.d(TAG, "onResponse : isFailure : ${response.message()}")
                    liveDataCombineList.value = ArrayList()
                    liveDataTideList.value = ArrayList()
                    liveDataLoadingStatus.value = false
                }
            }

            override fun onFailure(call: Call<Tide>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
                liveDataCombineList.value = ArrayList()
                liveDataTideList.value = ArrayList()
                liveDataLoadingStatus.value = false
            }

        })

    } // changeDate()


    fun changeLocation(location : SearchLocation) {

        callBackStatus = 0
        liveDataLoadingStatus.value = true

        liveDataSearchLocation.value = location
        liveDataCurrentLayout.value = previousLayout

        liveDataFishList.value =
            model.getFishList(liveDataBasicIndexList.value!!, liveDataSearchLocation.value!!, liveDataDate.value!!)
        liveDataFish.value = model.getFish(liveDataFishList.value!!)

        liveDataIndex.value = model.getIndex(
            liveDataBasicIndexList.value!!,
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

                        liveDataBasicWeatherList.value = model.getBasicWeatherList(response)

                        callBackStatus++

                        if (callBackStatus == 2) {

                            liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(liveDataBasicIndexList.value!!, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                            liveDataCombineList.value = model.getCombineList(
                                liveDataWeatherList.value!!,
                                liveDataTideList.value!!,
                                liveDataIndexTotal.value!!
                            )

                            liveDataLoadingStatus.value = false

                        }

                    } else {
                        Log.d(TAG, "onResponse : isFailure : ${response.message()}")
                        liveDataWeatherList.value = ArrayList()
                        liveDataCombineList.value = ArrayList()
                        liveDataLoadingStatus.value = false
                    }

                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Log.d(TAG, "onFailure : $t")
                    liveDataWeatherList.value = ArrayList()
                    liveDataCombineList.value = ArrayList()
                    liveDataLoadingStatus.value = false
                }

            })

        val requestDate = liveDataDate.value!!.substring(0,4) + liveDataDate.value!!.substring(5,7) + liveDataDate.value!!.substring(8,10)

        model.requestTide(requestDate, liveDataSearchLocation.value!!.obsCode, "json").enqueue(object : Callback<Tide> {
            override fun onResponse(call: Call<Tide>, response: Response<Tide>) {
                if (response.isSuccessful) {

                    liveDataTideList.value = model.getTideList(response)

                    callBackStatus++

                    if (callBackStatus == 2) {

                        liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(liveDataBasicIndexList.value!!, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                        liveDataCombineList.value = model.getCombineList(
                            liveDataWeatherList.value!!,
                            liveDataTideList.value!!,
                            liveDataIndexTotal.value!!
                        )

                        liveDataLoadingStatus.value = false

                    }

                } else {
                    Log.d(TAG, "onResponse : isFailure : ${response.message()}")
                    liveDataCombineList.value = ArrayList()
                    liveDataTideList.value = ArrayList()
                    liveDataLoadingStatus.value = false
                }
            }

            override fun onFailure(call: Call<Tide>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
                liveDataCombineList.value = ArrayList()
                liveDataTideList.value = ArrayList()
                liveDataLoadingStatus.value = false
            }

        })

    } // changeLocation()


    fun changeFish(fishName : String) {

        liveDataFish.value = fishName

        liveDataIndex.value = model.getIndex(liveDataBasicIndexList.value!!, liveDataDate.value!!, liveDataFish.value!!, liveDataSearchLocation.value!!.location)

        liveDataCurrentLayout.value = previousLayout

    } // changeFish()


    fun refresh() {

        callBackStatus = 0
        liveDataRefreshLoadingStatus.value = true

        model.requestWeather("1", "1000", "JSON", GetDate().getFormatDate4(GetDate().getTime()), "2300", liveDataSearchLocation.value!!.lat, liveDataSearchLocation.value!!.lon)
            .enqueue(object : Callback<Weather> {
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                    if (response.isSuccessful) {

                        liveDataWeatherList.value =
                            model.getWeatherList(response, liveDataDate.value!!)

                        liveDataBasicWeatherList.value = model.getBasicWeatherList(response)

                        callBackStatus++

                        if (callBackStatus == 3) {

                            liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(liveDataBasicIndexList.value!!, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                            liveDataCombineList.value = model.getCombineList(
                                liveDataWeatherList.value!!,
                                liveDataTideList.value!!,
                                liveDataIndexTotal.value!!
                            )

                            liveDataRefreshLoadingStatus.value = false

                        }

                    } else {
                        Log.d(TAG, "onResponse : isFailure : ${response.message()}")
                        liveDataWeatherList.value = ArrayList()
                        liveDataCombineList.value = ArrayList()
                        liveDataRefreshLoadingStatus.value = false
                    }

                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Log.d(TAG, "onFailure : $t")
                    liveDataWeatherList.value = ArrayList()
                    liveDataCombineList.value = ArrayList()
                    liveDataRefreshLoadingStatus.value = false
                }

            })

        val requestDate = liveDataDate.value!!.substring(0,4) + liveDataDate.value!!.substring(5,7) + liveDataDate.value!!.substring(8,10)

        model.requestTide(requestDate, liveDataSearchLocation.value!!.obsCode, "json").enqueue(object : Callback<Tide> {
            override fun onResponse(call: Call<Tide>, response: Response<Tide>) {
                if (response.isSuccessful) {

                    liveDataTideList.value = model.getTideList(response)

                    callBackStatus++

                    if (callBackStatus == 3) {

                        liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(liveDataBasicIndexList.value!!, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                        liveDataCombineList.value = model.getCombineList(
                            liveDataWeatherList.value!!,
                            liveDataTideList.value!!,
                            liveDataIndexTotal.value!!
                        )

                        liveDataLoadingStatus.value = false

                    }

                } else {
                    Log.d(TAG, "onResponse : isFailure : ${response.message()}")
                    liveDataTideList.value = ArrayList()
                    liveDataCombineList.value = ArrayList()
                    liveDataRefreshLoadingStatus.value = false
                }
            }

            override fun onFailure(call: Call<Tide>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
                liveDataTideList.value = ArrayList()
                liveDataCombineList.value = ArrayList()
                liveDataRefreshLoadingStatus.value = false
            }

        })

        model.requestIndex("SF", "json").enqueue(object  : Callback<Index> {

            override fun onResponse(call: Call<Index>, response: Response<Index>) {

                if (response.isSuccessful) {

                    liveDataBasicIndexList.value = splashModel.getIndexList(response)
                    liveDataIndex.value = model.getIndex(liveDataBasicIndexList.value!!, liveDataDate.value!!, liveDataFish.value!!, liveDataSearchLocation.value!!.location)

                    callBackStatus++

                    if (callBackStatus == 3) {

                        liveDataIndexTotal.value = model.getIndexTotalList(model.getIndexList(liveDataBasicIndexList.value!!, liveDataSearchLocation.value!!.location, liveDataDate.value!!))

                        liveDataCombineList.value = model.getCombineList(
                            liveDataWeatherList.value!!,
                            liveDataTideList.value!!,
                            liveDataIndexTotal.value!!
                        )

                        liveDataLoadingStatus.value = false

                    }

                } else {
                    Log.d(TAG, "requestIndex - onResponse : isFailure : ${response.message()}")
                    liveDataIndex.value = ConditionIndex("", "", "", "", 0, "", "", "", "", 0)
                    liveDataCombineList.value = ArrayList()
                    liveDataRefreshLoadingStatus.value = false
                }


            }

            override fun onFailure(call: Call<Index>, t: Throwable) {
                Log.d(TAG, "requestIndex - onFailure : $t")
                liveDataIndex.value = ConditionIndex("", "", "", "", 0, "", "", "", "", 0)
                liveDataCombineList.value = ArrayList()
                liveDataRefreshLoadingStatus.value = false
            }

        })


    } // refresh()

}