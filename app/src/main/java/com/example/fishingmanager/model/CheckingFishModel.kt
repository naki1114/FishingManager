package com.example.fishingmanager.model

import com.example.fishingmanager.data.History
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface

class CheckingFishModel {

    private val webServerRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    fun getHistoryList(historyList : ArrayList<History>, nickname : String) : ArrayList<History> {

        val list = ArrayList<History>()

        for (i in 0 until historyList.size) {

            if (historyList[i].nickname == nickname) {

                list.add(historyList[i])

            }

        }

        return list

    } // getHistoryList()


    fun requestCombine(nickname: String) = webServerRetrofitInterface.requestDB(nickname)

}