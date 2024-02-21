package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.CheckingFish
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.model.CheckingFishModel
import com.example.fishingmanager.model.SplashModel
import okhttp3.MultipartBody
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
    val liveDataCameraStatus = MutableLiveData<Boolean>()
    val liveDataClassifyStatus = MutableLiveData<Boolean>()
    val liveDataClassifyCompleteStatus = MutableLiveData<Boolean>()
    val liveDataCheckingFish = MutableLiveData<CheckingFish>()
    val liveDataSaveStatus = MutableLiveData<Boolean>()
    val liveDataSaveAndWriteStatus = MutableLiveData<Boolean>()
    val liveDataChangeFragment = MutableLiveData<String>()

    fun init() {

        liveDataBasicHistoryList.value = basicHistoryList
        liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

    } // init()


    fun startCamera() {

        liveDataCameraStatus.value = true

    } // startCamera()


    fun classify() {

        liveDataClassifyStatus.value = true

    } // classify()


    fun classifyComplete() {

        liveDataClassifyCompleteStatus.value = true

    } // classifyComplete()


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


    fun getDescription(fishName : String, arg : Float) {

        liveDataCheckingFish.value = model.getDescription(fishName, arg)

    } // getDescription()


    fun saveHistory() {

        liveDataSaveStatus.value = true

    } // saveHistory()


    fun saveAndWrite() {

        liveDataSaveAndWriteStatus.value = true

    } // saveAndWrite()


    fun saveHistoryServer(file : MultipartBody.Part, nickname : String, fishName : String, date : String) {

        model.requestSaveHistory(file, nickname, fishName, date).enqueue(object : Callback<ArrayList<History>> {
            override fun onResponse(call: Call<ArrayList<History>>, response: Response<ArrayList<History>>) {

                if (response.isSuccessful) {

                    liveDataBasicHistoryList.value = SplashModel().getHistoryList(response.body())
                    basicHistoryList = liveDataBasicHistoryList.value!!
                    liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

                    changeLayout("main")

                } else {
                    Log.d(TAG, "saveHistoryServer - onResponse : isFailure : ${response.message()}")
                }

            }
            override fun onFailure(call: Call<ArrayList<History>>, t: Throwable) {
                Log.d(TAG, "saveHistoryServer - onFailure : $t")
            }

        })

    } // saveHistoryServer()


    fun saveHistoryServerAndChangeFragment(file : MultipartBody.Part, nickname : String, fishName : String, date : String) {

        model.requestSaveHistory(file, nickname, fishName, date).enqueue(object : Callback<ArrayList<History>> {
            override fun onResponse(call: Call<ArrayList<History>>, response: Response<ArrayList<History>>) {

                if (response.isSuccessful) {

                    liveDataBasicHistoryList.value = SplashModel().getHistoryList(response.body())
                    basicHistoryList = liveDataBasicHistoryList.value!!
                    liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

                    liveDataChangeFragment.value = "write"

                } else {
                    Log.d(TAG, "saveHistoryServer - onResponse : isFailure : ${response.message()}")
                }

            }
            override fun onFailure(call: Call<ArrayList<History>>, t: Throwable) {
                Log.d(TAG, "saveHistoryServer - onFailure : $t")
            }

        })

    } // saveHistoryServer()

}