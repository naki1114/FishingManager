package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.model.ProfileModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(collectionList: ArrayList<Collection>, historyList: ArrayList<History>,
                       var userInfo: UserInfo
) : ViewModel() {

    val TAG = "ProfileViewModel"

    val model = ProfileModel()
    val liveDataUserInfo = MutableLiveData<UserInfo>()
    lateinit var nickname : String
    var profileImageStatus : Boolean = false
    val basicCollectionList = collectionList
    val basicHistoryList = historyList
    var userBasicHistoryList = ArrayList<History>()
    val liveDataCalendarList = MutableLiveData<ArrayList<CalendarDay>>()

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

    fun init() {

        profileImageStatus = liveDataUserInfo.value?.profileImage != null
        liveDataUserInfo.value = checkProfileImage()
        nickname = liveDataUserInfo.value?.nickname.toString()
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


    fun deleteAccount(nickname: String) {

        model.requestDeleteAccount(nickname).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful && response.body() == "success") {

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

        if (profileImageStatus) {
            liveDataClickedFishImage.value = userInfo
        } else {
            liveDataClickedFishImage.value = UserInfo(userInfo.nickname, "", userInfo.checkingFishCount, userInfo.checkingFishTicket, userInfo.removeAdTicket, userInfo.type)
        }


    } // goPhotoView()


    fun goToGallery() {

        liveDataGoToGallery.value = true

    } // goToGallery()


    fun checkProfileImage() : UserInfo {

        if (profileImageStatus) {
            return userInfo
        } else {
            return UserInfo(userInfo.nickname, "", userInfo.checkingFishCount, userInfo.checkingFishTicket, userInfo.removeAdTicket, userInfo.type)
        }

    }

}