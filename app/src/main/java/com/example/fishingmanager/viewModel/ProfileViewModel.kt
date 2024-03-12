package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.model.ProfileModel
import com.example.fishingmanager.model.SplashModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(collectionList: ArrayList<Collection>, historyList: ArrayList<History>,
                       var userInfo: UserInfo, val nickname: String
) : ViewModel() {

    val TAG = "ProfileViewModel"

    val model = ProfileModel()
    val liveDataUserInfo = MutableLiveData<UserInfo>()
    var basicCollectionList = collectionList
    var basicHistoryList = historyList
    var userBasicHistoryList = ArrayList<History>()
    var liveDataCalendarList = MutableLiveData<ArrayList<CalendarDay>>()

    val liveDataBasicCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataBasicHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataBasicFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataBasicUserInfo = MutableLiveData<UserInfo>()
    val liveDataCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataFishList = MutableLiveData<ArrayList<SelectFish>>()
    val liveDataReadMoreFish = MutableLiveData<Collection>()

    val liveDataChangeTab = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataClickedMenu = MutableLiveData<Boolean>()
    val liveDataChangeFragment = MutableLiveData<String>()

    val liveDataCurrentFish = MutableLiveData<String>()
    val liveDataCurrentDate = MutableLiveData<String>()
    val liveDataShowDialog = MutableLiveData<String>()
    val liveDataLogoutStatus = MutableLiveData<Boolean>()
    val liveDataDeleteAccountStatus = MutableLiveData<Boolean>()

    val liveDataClickedFishImage = MutableLiveData<UserInfo>()
    val liveDataGoToGallery = MutableLiveData<Boolean>()

    var previousLayout: String = ""

    val liveDataLoadingStatus = MutableLiveData<Boolean>()

    fun init() {

        liveDataUserInfo.value = userInfo
        liveDataBasicHistoryList.value = basicHistoryList
        liveDataBasicCollectionList.value = basicCollectionList
        userBasicHistoryList = model.getHistoryList(basicHistoryList, nickname)
        liveDataCalendarList.value = model.getCalendarList(userBasicHistoryList)
        liveDataCollectionList.value = model.getCollectionList(basicCollectionList, nickname)
        liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)
        liveDataFishList.value = model.getFishList(basicHistoryList, nickname)
        liveDataCurrentFish.value = "전 체"
        liveDataCurrentDate.value = "전 체"

    } // init()


    fun readMoreFish(collection: Collection) {

        if (collection.fishName != "") {
            liveDataReadMoreFish.value = collection
            changeLayout("readMore")
        }

    } // selectedFish()


    fun changeTab(tab: String) {

        liveDataChangeTab.value = tab

    } // changeTab()


    fun changeLayout(layout: String) {

        if (previousLayout != "") {
            previousLayout = liveDataChangeLayout.value!!
        } else {
            previousLayout = "main"
        }
        liveDataChangeLayout.value = layout

    } // changeLayout()


    fun backToMainLayout() {

        liveDataChangeLayout.value = previousLayout

    } // backToMainLayout()


    fun clickedMenu() {

        liveDataClickedMenu.value = true

    } // clickedMenu()


    fun changeFragment(fragment : String) {

        liveDataChangeFragment.value = fragment

    } // changeFragment()


    fun changeFish(fishName: String) {

        liveDataCurrentFish.value = fishName
        liveDataHistoryList.value = model.refreshHistoryList(basicHistoryList, nickname, fishName, liveDataCurrentDate.value!!)
        liveDataChangeLayout.value = previousLayout

    } // changeFish()


    fun changeDate(date: String) {

        if (date != "전 체") {

            val formatDate = date.substring(2,4) + "." + date.substring(5,7) + "." + date.substring(8,10)
            liveDataCurrentDate.value = formatDate
            liveDataHistoryList.value = model.refreshHistoryList(basicHistoryList, nickname, liveDataCurrentFish.value!!, formatDate)

        } else {

            liveDataCurrentDate.value = date
            liveDataHistoryList.value = model.refreshHistoryList(basicHistoryList, nickname, liveDataCurrentFish.value!!, date)

        }

        liveDataChangeLayout.value = previousLayout

    } // changeDate()


    fun showDialog(dialogName : String) {

        liveDataShowDialog.value = dialogName

    } // showDialog()


    fun changeLogoutStatus(status : Boolean) {

        liveDataLogoutStatus.value = status

    } // changeLogoutStatus()


    fun changeDeleteAccountStatus(status : Boolean) {

        liveDataDeleteAccountStatus.value = status

    } // changeDeleteAccountStatus()


    fun deleteAccount(nickname: String, type: String) {

        model.requestDeleteAccount(nickname, type).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful && response.body() == "successDelete") {

                    changeFragment("start")

                } else {
                    Log.d(TAG, "deleteAccount - onResponse : isFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "deleteAccount - onFailure : $t")
            }

        })

    } // deleteAccount()


    fun goPhotoView(userInfo: UserInfo) {

        liveDataClickedFishImage.value = userInfo

    } // goPhotoView()


    fun goToGallery() {

        liveDataGoToGallery.value = true

    } // goToGallery()


    fun refresh() {

        liveDataLoadingStatus.value = true

        model.requestCombine(nickname).enqueue(object : Callback<Combine> {
            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataBasicCollectionList.value = response.body()?.collection
                    liveDataBasicHistoryList.value = SplashModel().getHistoryList(response.body()?.history)
                    liveDataBasicFeedList.value = response.body()?.feed
                    liveDataBasicUserInfo.value = response.body()?.userInfo

                    basicCollectionList = liveDataBasicCollectionList.value!!
                    basicHistoryList = liveDataBasicHistoryList.value!!
                    userBasicHistoryList = model.getHistoryList(basicHistoryList, nickname)

                    liveDataCalendarList.value = model.getCalendarList(userBasicHistoryList)
                    liveDataCollectionList.value = model.getCollectionList(basicCollectionList, nickname)
                    liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)
                    liveDataFishList.value = model.getFishList(basicHistoryList, nickname)

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