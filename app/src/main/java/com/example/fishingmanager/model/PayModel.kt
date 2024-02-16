package com.example.fishingmanager.model

import com.example.fishingmanager.R
import com.example.fishingmanager.data.PayTicket

class PayModel {

    val list = ArrayList<PayTicket>()


    fun getTicketList() : ArrayList<PayTicket> {

        list.add(PayTicket("FM 세트 한 달 이용권", R.drawable.checkingfish_ticket, R.drawable.remove_ad_ticket, "30일", "￦ 5,800 → ￦ 4,900/ 월", false))
        list.add(PayTicket("FM 세트 일 년 이용권", R.drawable.checkingfish_ticket, R.drawable.remove_ad_ticket, "365일", "￦ 69,600 → ￦ 49,000 / 년", true))
        list.add(PayTicket("어종 확인 한 달 이용권", R.drawable.checkingfish_ticket, 0, "30일", "￦ 3,900 / 월", false))
        list.add(PayTicket("어종 확인 일 년 이용권", R.drawable.checkingfish_ticket, 0, "365일", "￦ 46,800 → ￦ 39,000 / 년", true))
        list.add(PayTicket("광고 제거 한 달 이용권", R.drawable.remove_ad_ticket, 0, "30일", "￦ 1,900 / 월", false))
        list.add(PayTicket("광고 제거 일 년 이용권", R.drawable.remove_ad_ticket, 0, "365일", "￦ 22,800 → ￦ 19,000 / 년", true))

        return list

    } // getTicketList()

}