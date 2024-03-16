package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewViewModel: ViewModel() {

    val liveDataCloseStatus = MutableLiveData<Boolean>()
    val liveDataImage = MutableLiveData<String>()


    // 프래그먼트 닫기
    fun close() {

        liveDataCloseStatus.value = true

    } // close()


    // 이미지 적용
    fun setImage(image: String) {

        liveDataImage.value = image

    } // setImage()


}