package com.example.fishingmanager.model

import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish

class ProfileModel {

    fun getCollectionList(collectionList : ArrayList<Collection>, nickname : String) : ArrayList<Collection> {

        val list = ArrayList<Collection>()

        for (i in 0 until collectionList.size) {

            if (collectionList[i].nickname == nickname) {

                list.add(collectionList[i])

            }

        }

        return list

    } // getCollectionList()


    fun getHistoryList(historyList : ArrayList<History>, nickname : String) : ArrayList<History> {

        val list = ArrayList<History>()

        for (i in 0 until historyList.size) {

            if (historyList[i].nickname == nickname) {

                list.add(historyList[i])

            }

        }

        return list

    } // getHistoryList()


    fun getFishList() : ArrayList<SelectFish> {

        val list = ArrayList<SelectFish>()



        return list

    } // getFishList()

}