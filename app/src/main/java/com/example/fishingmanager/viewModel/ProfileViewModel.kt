package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.model.ProfileModel

class ProfileViewModel(
    collectionList: ArrayList<Collection>, historyList: ArrayList<History>,
    val nickname: String
) : ViewModel() {

    val model = ProfileModel()
    val basicCollectionList = collectionList
    val basicHistoryList = historyList

    val liveDataCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataFishList = MutableLiveData<ArrayList<SelectFish>>()
    val liveDataSelectedFish = MutableLiveData<Collection>()

    val liveDataChangeTab = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataClickedMenu = MutableLiveData<Boolean>()

    var previousLayout: String = ""

    fun init() {

        liveDataCollectionList.value = model.getCollectionList(basicCollectionList, nickname)
        liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)
        liveDataFishList.value = model.getFishList()

    } // init()


    fun selectedFish(collection: Collection) {

        if (collection.fishName != "") {
            liveDataSelectedFish.value = collection
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

}