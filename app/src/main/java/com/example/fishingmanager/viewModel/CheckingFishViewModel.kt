package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.History
import com.example.fishingmanager.model.CheckingFishModel

class CheckingFishViewModel : ViewModel() {

    val model = CheckingFishModel()

    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataClickedFishImage = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()

    fun getHistoryList() {

        liveDataHistoryList.value = model.getHistoryList()

    } // getHistoryList()


    fun goPhotoView(image : String) {

        liveDataClickedFishImage.value = image

    } // goPhotoView()


    fun changeLayout(layout : String) {

        liveDataChangeLayout.value = layout

    } // changeLayout()

}