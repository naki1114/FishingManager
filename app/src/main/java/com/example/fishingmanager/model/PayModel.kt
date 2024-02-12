package com.example.fishingmanager.model

import com.example.fishingmanager.R
import com.example.fishingmanager.data.PayTicket

class PayModel {

    val list = ArrayList<PayTicket>()


    fun getTicketList() : ArrayList<PayTicket> {

        list.add(PayTicket("FM 세트 한 달 이용권", R.drawable.chamdom, R.drawable.snow, "30일", "￦ 4,900 / 월"))
        list.add(PayTicket("FM 세트 일 년 이용권", R.drawable.chamdom, R.drawable.snow, "365일", "￦ 49,000 / 년"))
        list.add(PayTicket("어종 확인 한 달 이용권", R.drawable.chamdom, 0, "30일", "￦ 3,900 / 월"))
        list.add(PayTicket("어종 확인 일 년 이용권", R.drawable.chamdom, 0, "365일", "￦ 39,000 / 년"))
        list.add(PayTicket("광고 제거 한 달 이용권", R.drawable.chamdom, 0, "30일", "￦ 1,900 / 월"))
        list.add(PayTicket("광고 제거 일 년 이용권", R.drawable.chamdom, 0, "365일", "￦ 19,000 / 년"))

        return list

    } // getTicketList()

}