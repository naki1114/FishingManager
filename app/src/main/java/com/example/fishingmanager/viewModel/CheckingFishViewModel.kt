package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.model.CheckingFishModel
import com.example.fishingmanager.model.SplashModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckingFishViewModel(historyList : ArrayList<History>, val nickname : String) : ViewModel() {

    val TAG = "CheckingFishViewModel"
    val model = CheckingFishModel()

    var basicHistoryList = historyList
    val liveDataBasicCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataBasicHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataBasicFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataBasicUserInfo = MutableLiveData<UserInfo>()

    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataClickedFishImage = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()

    val liveDataLoadingStatus = MutableLiveData<Boolean>()


    fun init() {

        liveDataBasicHistoryList.value = basicHistoryList
        liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

    } // init()


    fun goPhotoView(image : String) {

        liveDataClickedFishImage.value = image

    } // goPhotoView()


    fun changeLayout(layout : String) {

        liveDataChangeLayout.value = layout

    } // changeLayout()


    fun refresh() {

        liveDataLoadingStatus.value = true

        model.requestCombine(nickname).enqueue(object : Callback<Combine> {
            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataBasicCollectionList.value = response.body()?.collection
                    liveDataBasicHistoryList.value = SplashModel().getHistoryList(response.body()?.history)
                    liveDataBasicFeedList.value = response.body()?.feed
                    liveDataBasicUserInfo.value = response.body()?.userInfo

                    basicHistoryList = liveDataBasicHistoryList.value!!
                    liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

                    liveDataLoadingStatus.value = false

                } else {
                    Log.d(TAG, "requestCombine - onResponse : isFailure : ${response.message()}")

                    liveDataBasicCollectionList.value = ArrayList()
                    liveDataBasicHistoryList.value = ArrayList()
                    liveDataBasicFeedList.value = ArrayList()
                    liveDataBasicUserInfo.value = UserInfo("","", -1, -1, -1, "")
                    liveDataLoadingStatus.value = false
                }

            }

            override fun onFailure(call: Call<Combine>, t: Throwable) {
                Log.d(TAG, "requestCombine - onFailure : $t")

                liveDataBasicCollectionList.value = ArrayList()
                liveDataBasicHistoryList.value = ArrayList()
                liveDataBasicFeedList.value = ArrayList()
                liveDataBasicUserInfo.value = UserInfo("","", -1, -1, -1, "")
                liveDataLoadingStatus.value = false
            }

        })

    } // refresh()

}