package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.model.ProfileModel

class ProfileViewModel : ViewModel() {

    val model = ProfileModel()

    val liveDataCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataFishList = MutableLiveData<ArrayList<SelectFish>>()

    val liveDataChangeTab = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataClickedMenu = MutableLiveData<Boolean>()

    lateinit var previousLayout : String

    fun init() {

        liveDataCollectionList.value = model.getCollectionList()
        liveDataHistoryList.value = model.getHistoryList()
        liveDataFishList.value = model.getFishList()

    } // init()


    fun changeTab(tab : String) {

        liveDataChangeTab.value = tab

    } // changeTab()


    fun changeLayout(layout : String) {

        previousLayout = liveDataChangeLayout.value!!
        liveDataChangeLayout.value = layout

    } // changeLayout()


    fun clickedMenu() {

        liveDataClickedMenu.value = true

    } // clickedMenu()

}