package com.example.fishingmanager.model

import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish

class ProfileModel {

    lateinit var collection: Collection


    fun getCollectionList(
        collectionList: ArrayList<Collection>,
        nickname: String
    ): ArrayList<Collection> {

        val list = ArrayList<Collection>()

        for (i in 0 until collectionList.size) {

            if (collectionList[i].nickname == nickname) {

                list.add(collectionList[i])

            }

        }

        return list

    } // getCollectionList()


    fun getHistoryList(historyList: ArrayList<History>, nickname: String): ArrayList<History> {

        val list = ArrayList<History>()

        for (i in 0 until historyList.size) {

            if (historyList[i].nickname == nickname) {

                list.add(historyList[i])

            }

        }

        return list

    } // getHistoryList()


    fun getFishList(): ArrayList<SelectFish> {

        val list = ArrayList<SelectFish>()



        return list

    } // getFishList()


    fun checkCollection(collectionList: ArrayList<Collection>): ArrayList<Collection> {

        val list = ArrayList<Collection>()

        for (i in 0 until 47) {

            when (i) {

                0 -> {
                    addCollection(collectionList, "가자미")
                }

                1 -> {
                    addCollection(collectionList, "갈치")
                }

                2 -> {
                    addCollection(collectionList, "감성돔")
                }

                3 -> {
                    addCollection(collectionList, "갑오징어")
                }

                4 -> {
                    addCollection(collectionList, "고등어")
                }

                5 -> {
                    addCollection(collectionList, "광어")
                }

                6 -> {
                    addCollection(collectionList, "꽁치")
                }

                7 -> {
                    addCollection(collectionList, "가자미")
                }

                8 -> {

                }

                9 -> {

                }

                10 -> {

                }

                11 -> {

                }

                12 -> {

                }

                13 -> {

                }

                14 -> {

                }

                15 -> {

                }

                16 -> {

                }

                17 -> {

                }

                18 -> {

                }

                19 -> {

                }

                20 -> {

                }

                21 -> {

                }

                22 -> {

                }

                23 -> {

                }

                24 -> {

                }

                25 -> {

                }

                26 -> {

                }

                27 -> {

                }

                28 -> {

                }

                29 -> {

                }

                30 -> {

                }

                31 -> {

                }

                32 -> {

                }

                33 -> {

                }

                34 -> {

                }

                35 -> {

                }

                36 -> {

                }

                37 -> {

                }

                38 -> {

                }

                39 -> {

                }

                40 -> {

                }

                41 -> {

                }

                42 -> {

                }

                43 -> {

                }

                44 -> {

                }

                45 -> {

                }

                46 -> {

                }

            }

            if (list[i-1].fishName == collection.fishName) {
                list.add(Collection("", "", 0, "", ""))
            } else {
                list.add(collection)
            }

        }

        return list

    } // checkCollection()


    fun addCollection(list : ArrayList<Collection>, fish : String) {

        for (j in 0 until list.size) {
            if (list[j].fishName == fish) {
                collection = Collection(
                    list[j].nickname,
                    list[j].fishName,
                    list[j].fishImage,
                    list[j].fishLength,
                    list[j].date
                )
                break
            }
        }

    } // addCollection()


}