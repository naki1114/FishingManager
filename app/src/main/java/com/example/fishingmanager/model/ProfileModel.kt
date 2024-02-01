package com.example.fishingmanager.model

import com.example.fishingmanager.R
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.function.GetDate

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

        return checkCollection(list)

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

        for (i in 0 until 30) {

            collection = Collection("", "", R.drawable.imgcollection, "", "", "")

            when (i) {

                0 -> addCollection(collectionList, "가자미", 0, "")
                1 -> addCollection(collectionList, "갈치", 0, "")
                2 -> addCollection(collectionList, "감성돔", R.drawable.gamsungdom, "참돔, 돌돔, 벵에돔과 함께 4대 돔으로 불리며 그중 으뜸이다.\n지역에 따라 감시, 감생이 등으로 불리기도 한다.\n\n동아시아 연근해에 분포하며 특히 한국과 일본에 많이 서식한다.\n\n등지느러미에 가시가 날카로우니 조심해야 한다.")
                3 -> addCollection(collectionList, "갑오징어", 0, "")
                4 -> addCollection(collectionList, "고등어", 0, "")
                5 -> addCollection(collectionList, "광어", R.drawable.gawnga, "순우리말은 넙치이며 우럭과 함께\n한국을 대표하는 양대산맥 횟감이다.\n\n태평양 서부 지역에 주로 분포하며\n모래 바닥을 선호한다.\n\n자연산 넙치는 배의 색깔이 하얗다.")
                6 -> addCollection(collectionList, "노래미", 0, "")
                7 -> addCollection(collectionList, "농어", R.drawable.nonga, "동아시아 지역에만 서식하는 어종이며 최대 130cm까지 자란다.\n나름 대형 어종이지만 봄·여름에는 얕은 바다로 모이는 습성이 있어서 갯바위 낚시로도 잡을 수 있다.\n\n일반적인 횟감과는 식감이 달라서 일부 낚시인들만 선호하는 경향이 있다.")
                8 -> addCollection(collectionList, "대구", 0, "")
                9 -> addCollection(collectionList, "돌돔", R.drawable.doldom, "바다의 왕자라 불리는 고급 어종이다.\n일반적인 횟감 중 가격이 제일 비싸며 그만큼 맛이 좋다.\n\n이름에 돔이 들어가지만 생물학적으로는 우럭목에 속한다.\n\n치악력이 매우 좋아서 바위에 붙은 따개비도 부숴먹으니 바늘을 뺄 때는 항상 조심하자.")
                10 -> addCollection(collectionList, "문어", 0, "")
                11 -> addCollection(collectionList, "방어", 0, "")
                12 -> addCollection(collectionList, "배도라치", 0, "")
                13 -> addCollection(collectionList, "벵에돔", R.drawable.bengedom, "태평양부터 오세아니아까지 서식하는 난류 어종이며 한국에서는 동해와 남해에서만 잡힌다\n인기 대상어임에도 금어기와 금지체장이 없는 어종이다.\n\n입질이 매우 약아서 잡기 까다롭기로 유명하다.")
                14 -> addCollection(collectionList, "병어", 0, "")
                15 -> addCollection(collectionList, "볼락", R.drawable.bollock, "한국에서 제일 흔한 어종이자 횟감이다.\n성체가 30cm도 안될 정도로 작다.\n\n바위 틈이나 방파제, 항구 등 얕은 바다 어디서든 볼 수 있고 입질도 단순하여 낚시 초보자들도 매우 쉽게 낚을 수 있는 어종이다.")
                16 -> addCollection(collectionList, "붕장어", 0, "")
                17 -> addCollection(collectionList, "삼치", 0, "")
                18 -> addCollection(collectionList, "성대", 0, "")
                19 -> addCollection(collectionList, "숭어", 0, "")
                20 -> addCollection(collectionList, "쏨뱅이", 0, "")
                21 -> addCollection(collectionList, "양태", 0, "")
                22 -> addCollection(collectionList, "열기", R.drawable.yulgi, "볼락과에 속하며 붉은색을 띄어서 불볼락이라고도 불린다.\n\n볼락보다 깊은 수심에 서식하며\n크기도 더 크다.\n\n동해, 남해에서 겨울에 주로 낚인다.")
                23 -> addCollection(collectionList, "용치놀래기", 0, "")
                24 -> addCollection(collectionList, "우럭", R.drawable.uruck, "동아시아에만 서식하는 어종이며\n정식 명칭은 조피볼락이다.\n\n한반도 전체 얕은 바다에 서식하여 가장 친근하게 낚을 수 있어 전 국민의 횟감으로도 유명하다.")
                25 -> addCollection(collectionList, "전어", 0, "")
                26 -> addCollection(collectionList, "주꾸미", 0, "")
                27 -> addCollection(collectionList, "쥐치", 0, "")
                28 -> addCollection(collectionList, "참돔", R.drawable.chamdom, "4대 돔 중에서 제일 큰 어종이다.\n\n동아시아부터 태평양까지 서식하며\n한국에서는 특히 남해에서 많이 잡힌다.\n\n나름 고급 어종이지만 양식이 잘 되어 횟집에서 흔히 볼 수 있다.")
                29 -> addCollection(collectionList, "홍어", 0, "")

            }

            list.add(collection)

        }

        return list

    } // checkCollection()


    fun addCollection(list: ArrayList<Collection>, fish: String, image: Int, explane: String) {

        for (j in 0 until list.size) {
            if (list[j].fishName == fish) {

                val date : Long = (list[j].date + "000").toLong()

                collection = Collection(
                    list[j].nickname,
                    list[j].fishName,
                    image,
                    list[j].fishLength,
                    GetDate().getFormatDate5(date),
                    explane
                )
                break
            }
        }

    } // addCollection()


}