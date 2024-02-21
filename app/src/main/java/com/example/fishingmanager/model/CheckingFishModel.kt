package com.example.fishingmanager.model

import com.example.fishingmanager.R
import com.example.fishingmanager.data.CheckingFish
import com.example.fishingmanager.data.History
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import okhttp3.MultipartBody

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


    fun getDescription(fishName : String, arg : Float) : CheckingFish {

        var fishDescription = ""
        var fishArg = arg.toString()

        if (fishArg.substring(0,3) != "100" && fishArg.substring(0,1) != "0") {

            if (fishArg.length > 5) {

                fishArg = fishArg.substring(0,5)

            }

        }

        when (fishName) {

            "가자미" -> fishDescription = ""
            "갈치" -> fishDescription = ""
            "감성돔" -> fishDescription = "참돔, 돌돔, 벵에돔과 함께 4대 돔으로 불리며 그중 으뜸이다.\n지역에 따라 감시, 감생이 등으로 불리기도 한다.\n\n동아시아 연근해에 분포하며 특히 한국과 일본에 많이 서식한다.\n\n등지느러미에 가시가 날카로우니 조심해야 한다."
            "갑오징어" -> fishDescription = ""
            "고등어" -> fishDescription = ""
            "광어" -> fishDescription = "순우리말은 넙치이며 우럭과 함께\n한국을 대표하는 양대산맥 횟감이다.\n\n태평양 서부 지역에 주로 분포하며\n모래 바닥을 선호한다.\n\n자연산 넙치는 배의 색깔이 하얗다."
            "노래미" -> fishDescription = ""
            "농어" -> fishDescription = "동아시아 지역에만 서식하는 어종이며 최대 130cm까지 자란다.\n나름 대형 어종이지만 봄·여름에는 얕은 바다로 모이는 습성이 있어서 갯바위 낚시로도 잡을 수 있다.\n\n일반적인 횟감과는 식감이 달라서 일부 낚시인들만 선호하는 경향이 있다."
            "대구" -> fishDescription = ""
            "돌돔" -> fishDescription = "바다의 왕자라 불리는 고급 어종이다.\n일반적인 횟감 중 가격이 제일 비싸며 그만큼 맛이 좋다.\n\n이름에 돔이 들어가지만 생물학적으로는 우럭목에 속한다.\n\n치악력이 매우 좋아서 바위에 붙은 따개비도 부숴먹으니 바늘을 뺄 때는 항상 조심하자."
            "문어" -> fishDescription = ""
            "방어" -> fishDescription = ""
            "배도라치" -> fishDescription = ""
            "벵에돔" -> fishDescription = "태평양부터 오세아니아까지 서식하는 난류 어종이며 한국에서는 동해와 남해에서만 잡힌다\n인기 대상어임에도 금어기와 금지체장이 없는 어종이다.\n\n입질이 매우 약아서 잡기 까다롭기로 유명하다."
            "병어" -> fishDescription = ""
            "볼락" -> fishDescription = "한국에서 제일 흔한 어종이자 횟감이다.\n성체가 30cm도 안될 정도로 작다.\n\n바위 틈이나 방파제, 항구 등 얕은 바다 어디서든 볼 수 있고 입질도 단순하여 낚시 초보자들도 매우 쉽게 낚을 수 있는 어종이다."
            "붕장어" -> fishDescription = ""
            "삼치" -> fishDescription = ""
            "성대" -> fishDescription = ""
            "숭어" -> fishDescription = ""
            "쏨뱅이" -> fishDescription = ""
            "양태" -> fishDescription = ""
            "열기" -> fishDescription = "볼락과에 속하며 붉은색을 띄어서 불볼락이라고도 불린다.\n\n볼락보다 깊은 수심에 서식하며\n크기도 더 크다.\n\n동해, 남해에서 겨울에 주로 낚인다."
            "용치놀래기" -> fishDescription = ""
            "우럭" -> fishDescription = "동아시아에만 서식하는 어종이며\n정식 명칭은 조피볼락이다.\n\n한반도 전체 얕은 바다에 서식하여 가장 친근하게 낚을 수 있어 전 국민의 횟감으로도 유명하다."
            "전어" -> fishDescription = ""
            "주꾸미" -> fishDescription = ""
            "쥐치" -> fishDescription = ""
            "참돔" -> fishDescription = "4대 돔 중에서 제일 큰 어종이다.\n\n동아시아부터 태평양까지 서식하며\n한국에서는 특히 남해에서 많이 잡힌다.\n\n나름 고급 어종이지만 양식이 잘 되어 횟집에서 흔히 볼 수 있다."
            "홍어" -> fishDescription = ""

        }

        return CheckingFish(fishName, fishArg, fishDescription)

    } // getDescription()


    fun requestSaveHistory(file : MultipartBody.Part, nickname : String, fishName : String, date : String) = webServerRetrofitInterface.saveHistory(file, nickname, fishName, date)


}