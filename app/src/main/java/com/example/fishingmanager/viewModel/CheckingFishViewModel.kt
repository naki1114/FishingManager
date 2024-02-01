package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.History
import com.example.fishingmanager.model.CheckingFishModel

class CheckingFishViewModel(historyList : ArrayList<History>, val nickname : String) : ViewModel() {

    val model = CheckingFishModel()

    val basicHistoryList = historyList

    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataClickedFishImage = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()

    fun getHistoryList() {

        liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

    } // getHistoryList()


    fun goPhotoView(image : String) {

        liveDataClickedFishImage.value = image

    } // goPhotoView()


    fun changeLayout(layout : String) {

        liveDataChangeLayout.value = layout

    } // changeLayout()

}