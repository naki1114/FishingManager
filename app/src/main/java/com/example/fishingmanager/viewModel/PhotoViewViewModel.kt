package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewViewModel : ViewModel() {

    val liveDataCloseStatus = MutableLiveData<Boolean>()
    val liveDataImage = MutableLiveData<String>()

    fun close() {

        liveDataCloseStatus.value = true

    } // close()


    fun setImage(image : String) {

        liveDataImage.value = image

    } // setImage

}