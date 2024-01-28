package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.PayTicket

class PayViewModel : ViewModel() {

    val liveDataBackStatus = MutableLiveData<Boolean>()
    val liveDataProduct = MutableLiveData<PayTicket>()

    fun backToPrevious() {

        liveDataBackStatus.value = true

    } // backToPrevious()


    fun startPayStep(payTicket: PayTicket) {

        liveDataProduct.value = payTicket

    } // startPayStep()

}