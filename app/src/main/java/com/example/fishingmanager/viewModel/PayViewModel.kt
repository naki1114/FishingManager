package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.PayTicket
import com.example.fishingmanager.model.PayModel

class PayViewModel : ViewModel() {

    val model = PayModel()

    val liveDataTicketList = MutableLiveData<ArrayList<PayTicket>>()
    val liveDataBackStatus = MutableLiveData<Boolean>()
    val liveDataProduct = MutableLiveData<PayTicket>()


    fun getTicketList() {

        liveDataTicketList.value = model.getTicketList()

    } // getTicketList()


    fun backToPrevious() {

        liveDataBackStatus.value = true

    } // backToPrevious()


    fun startPayStep(payTicket: PayTicket) {

        liveDataProduct.value = payTicket

    } // startPayStep()

}