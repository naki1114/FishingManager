package com.example.fishingmanager.model

import com.example.fishingmanager.data.History

class CheckingFishModel {

    fun getHistoryList(historyList : ArrayList<History>, nickname : String) : ArrayList<History> {

        val list = ArrayList<History>()

        for (i in 0 until historyList.size) {

            if (historyList[i].nickname == nickname) {

                list.add(historyList[i])

            }

        }

        return list

    } // getHistoryList()

}