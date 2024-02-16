package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.model.ProfileModel
import com.prolificinteractive.materialcalendarview.CalendarDay

class ProfileViewModel(
    collectionList: ArrayList<Collection>, historyList: ArrayList<History>,
    val nickname: String
) : ViewModel() {

    val model = ProfileModel()
    val basicCollectionList = collectionList
    val basicHistoryList = historyList
    var userBasicHistoryList = ArrayList<History>()
    val calendarList = MutableLiveData<ArrayList<CalendarDay>>()

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

    var previousLayout: String = ""

    fun init() {

        userBasicHistoryList = model.getHistoryList(basicHistoryList, nickname)
        calendarList.value = model.getCalendarList(userBasicHistoryList)
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


    fun changeFragment() {

        liveDataChangeFragment.value = "pay"

    } // changeFragment()


    fun changeFish(fishName: String) {

        liveDataCurrentFish.value = fishName
        liveDataHistoryList.value = model.changeFish(basicHistoryList, nickname, fishName)
        liveDataChangeLayout.value = previousLayout

    } // changeFish()


    fun changeDate(date: String) {

        liveDataCurrentDate.value = date

    } // changeDate()

}