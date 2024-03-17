package com.example.fishingmanager.model

import android.annotation.SuppressLint
import com.example.fishingmanager.R
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import com.prolificinteractive.materialcalendarview.CalendarDay
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.Calendar

class ProfileModel {

    lateinit var collection: Collection

    private val webServerRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

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


    fun getFishList(historyList: ArrayList<History>, nickname: String): ArrayList<SelectFish> {

        val list = ArrayList<SelectFish>()
        var fishIcon = 0

        for (i in 0 until historyList.size) {

            if (historyList[i].nickname == nickname) {

                when (historyList[i].fishName) {

                    "가자미" -> fishIcon = R.drawable.gajami
                    "갈치" -> fishIcon = R.drawable.galchi
                    "감성돔" -> fishIcon = R.drawable.gamsungdom
                    "갑오징어" -> fishIcon = R.drawable.gapojinga
                    "고등어" -> fishIcon = R.drawable.godeunga
                    "광어" -> fishIcon = R.drawable.gawnga
                    "노래미" -> fishIcon = R.drawable.norami
                    "농어" -> fishIcon = R.drawable.nonga
                    "대구" -> fishIcon = R.drawable.dagu
                    "돌돔" -> fishIcon = R.drawable.doldom
                    "문어" -> fishIcon = R.drawable.muna
                    "방어" -> fishIcon = R.drawable.banga
                    "배도라치" -> fishIcon = R.drawable.badorachi
                    "벵에돔" -> fishIcon = R.drawable.bengedom
                    "병어" -> fishIcon = R.drawable.buenga
                    "볼락" -> fishIcon = R.drawable.bollock
                    "붕장어" -> fishIcon = R.drawable.boongjanga
                    "삼치" -> fishIcon = R.drawable.samchi
                    "성대" -> fishIcon = R.drawable.sungdae
                    "숭어" -> fishIcon = R.drawable.sunga
                    "쏨뱅이" -> fishIcon = R.drawable.ssombange
                    "양태" -> fishIcon = R.drawable.yangtae
                    "열기" -> fishIcon = R.drawable.yulgi
                    "용치놀래기" -> fishIcon = R.drawable.yongchi
                    "우럭" -> fishIcon = R.drawable.uruck
                    "전어" -> fishIcon = R.drawable.juna
                    "주꾸미" -> fishIcon = R.drawable.jukkumi
                    "쥐치" -> fishIcon = R.drawable.jwichi
                    "참돔" -> fishIcon = R.drawable.chamdom
                    "홍어" -> fishIcon = R.drawable.honga

                }

                list.add(SelectFish(fishIcon, historyList[i].fishName))

            }

        }

        val selectFishList = ArrayList<SelectFish>()
        var duplicateStatus = false

        for (i in 0 until list.size) {

            duplicateStatus = false

            for (j in 0 until selectFishList.size) {

                if (list[i].fishName == selectFishList[j].fishName) {

                    duplicateStatus = true

                }

            }

            if (!duplicateStatus) {

                selectFishList.add(list[i])

            }

        }

        selectFishList.add(0, SelectFish(0, "전 체"))

        return selectFishList

    } // getFishList()


    fun checkCollection(collectionList: ArrayList<Collection>): ArrayList<Collection> {

        val list = ArrayList<Collection>()

        for (i in 0 until 30) {

            collection = Collection("", "", R.drawable.imgcollection, "", "")

            when (i) {

                0 -> addCollection(collectionList, "가자미", R.drawable.gajami, "넙치와 비슷하게 생겼지만, 눈이 오른쪽으로 쏠려있고 크기도 조금 작다.\n\n겉으로 보이는 모습은 비대칭이지만, 뇌를 포함한 기관은 모두 좌우대칭이다.\n\n초보자가 낚시하기 쉬운 어종에 속한다.")
                1 -> addCollection(collectionList, "갈치", R.drawable.galchi, "낚시로 많이 잡히지만, 의외로 심해어다.\n그렇기에 물 밖으로 건져올리면 수압 차이를 견디지 못하고 곧바로 죽어버려서 낚시꾼만이 살아있는 갈치를 볼 수 있다.\n새끼 갈치를 풀치라고 부른다.")
                2 -> addCollection(collectionList, "감성돔", R.drawable.gamsungdom, "참돔, 돌돔, 벵에돔과 함께 4대 돔으로 불리며 그중 으뜸이다.\n지역에 따라 감시, 감생이 등으로 불리기도 한다.\n\n동아시아 연근해에 분포하며 특히 한국과 일본에 많이 서식한다.\n\n등지느러미에 가시가 날카로우니 조심해야 한다.")
                3 -> addCollection(collectionList, "갑오징어", R.drawable.gapojinga, "연체동물이지만 몸에 갑옷과 같은 뼈가 있어 갑오징어라 불린다.\n\n동아시아에서 유난히 많이 잡혀 한국에서 인기가 많다.\n가을철 낚시 대상어로 각광받는 어종이다.")
                4 -> addCollection(collectionList, "고등어", R.drawable.godeunga, "등푸른 생선을 대표하는 어종이며 한국인의 밥상에서 제일 흔히 볼 수 있는 국민생선이다.\n\n따뜻한 바다를 좋아하는 회유성 어종이며 전 세계적으로 널리 분포한다.")
                5 -> addCollection(collectionList, "광어", R.drawable.gawnga, "순우리말은 넙치이며 우럭과 함께\n한국을 대표하는 양대산맥 횟감이다.\n\n태평양 서부 지역에 주로 분포하며\n모래 바닥을 선호한다.\n\n자연산 넙치는 배의 색깔이 하얗다.")
                6 -> addCollection(collectionList, "노래미", R.drawable.norami, "흔하게 생겼지만 의외로 한국과 일본 등지에만 분포하는 어종이다.\n서식지 환경에 영향을 많이 받아서 붉은색, 황색, 검정색 등 다양한 색상을 띄운다.\n\n놀래미라는 이름으로 더 많이 불린다.")
                7 -> addCollection(collectionList, "농어", R.drawable.nonga, "동아시아 지역에만 서식하는 어종이며 최대 130cm까지 자란다.\n나름 대형 어종이지만 봄·여름에는 얕은 바다로 모이는 습성이 있어서 갯바위 낚시로도 잡을 수 있다.\n\n일반적인 횟감과는 식감이 달라서 일부 낚시인들만 선호하는 경향이 있다.")
                8 -> addCollection(collectionList, "대구", R.drawable.dagu, "회로는 안 먹지만, 매운탕, 뽈찜 등 식용으로 유명한 어종이다.\n\n단순히 입이 커서 대구라는 이름이 붙었다.\n\n최대 1m가 넘게 자라서 한 번 잡아본 낚시꾼들은 잊지 못한다고 한다.")
                9 -> addCollection(collectionList, "돌돔", R.drawable.doldom, "바다의 왕자라 불리는 고급 어종이다.\n일반적인 횟감 중 가격이 제일 비싸며 그만큼 맛이 좋다.\n\n이름에 돔이 들어가지만 생물학적으로는 우럭목에 속한다.\n\n치악력이 매우 좋아서 바위에 붙은 따개비도 부숴먹으니 바늘을 뺄 때는 항상 조심하자.")
                10 -> addCollection(collectionList, "문어", R.drawable.muna, "8개의 다리를 가진 것으로 유명하지만, 사실 다리가 아닌 팔이다.\n위장술이 뛰어나 순식간에 주변 환경에 녹아들 수 있으며 지능 또한 뛰어나서 비인간 인격체의 대표종으로 알려져 있다.")
                11 -> addCollection(collectionList, "방어", R.drawable.banga, "전갱잇과에 속하지만 몸길이가 1.5m까지 자라는 대형 어종이다.\n\n겨울철 방어회가 유명한데, 사실 1년 내내 잘 잡히지만 여름철에는 유독 맛이 없기 때문이라고 한다.")
                12 -> addCollection(collectionList, "배도라치", R.drawable.badorachi, "긴 리본형태의 구조이며 먹을 것도 없고 가시가 많아서 잡어로 분류된다.\n\n경남 일부 지역에서는 횟감으로 이용하기도 한다.")
                13 -> addCollection(collectionList, "벵에돔", R.drawable.bengedom, "태평양부터 오세아니아까지 서식하는 난류 어종이며 한국에서는 동해와 남해에서만 잡힌다\n인기 대상어임에도 금어기와 금지체장이 없는 어종이다.\n\n입질이 매우 약아서 잡기 까다롭기로 유명하다.")
                14 -> addCollection(collectionList, "병어", R.drawable.buenga, "무리지어 생활하는 모습 때문에 병사를 뜻하는 한자가 붙어 병어라고 불렸다.\n\n 통통한 몸에 비해 뼈와 내장이 작아서 먹을 수 있는 살이 정말 많다.\n대부분 찜이나 구이로 밥상에 자주 오르지만 간혹 회로 먹기도 한다.")
                15 -> addCollection(collectionList, "볼락", R.drawable.bollock, "한국에서 제일 흔한 어종이자 횟감이다.\n성체가 30cm도 안될 정도로 작다.\n\n바위 틈이나 방파제, 항구 등 얕은 바다 어디서든 볼 수 있고 입질도 단순하여 낚시 초보자들도 매우 쉽게 낚을 수 있는 어종이다.")
                16 -> addCollection(collectionList, "붕장어", R.drawable.boongjanga, "흔히 바닷장어로 불리는 어종이 붕장어다.\n\n낚시꾼들 사이에서는 아나고로 불리는데, 이는 일본어다.\n\n낚시로도 많이 잡히는데, 물 밖으로 나오면 줄을 몸에 휘감아버려서 뒷처리가 골치 아프기로 유명하다.")
                17 -> addCollection(collectionList, "삼치", R.drawable.samchi, "고등어과에 속하는 등푸른 생선이지만 크기도 훨씬 크고 날카로운 몸체를 가졌다.\n\n낚시로는 가을철 서해에서 많이 잡을 수 있다.")
                18 -> addCollection(collectionList, "성대", R.drawable.sungdae, "인지도는 낮지만 어촌 마을 사람들에게는 맛이 좋기로 유명하다.\n\n부레를 이용해 소리를 낼 수 있는 물고기로도 유명하다.\n\n화려한 지느러미 아래 다리가 숨어있어서 육지에 올려놓으면 기어다니기도 한다.")
                19 -> addCollection(collectionList, "숭어", R.drawable.sunga, "태평양, 대서양, 인도양 등 세계 각지에 분포하는 어종이다.\n\n명백한 바닷고기지만 민물을 좋아해서 한강을 따라 올라오기도 한다.\n\n수면위에 떠서 유영할 때가 많아 훌치기 낚시로 유명한 어종이다.")
                20 -> addCollection(collectionList, "쏨뱅이", R.drawable.ssombange, "지역에 따라 쏠치, 수염어, 수수감펭이, 삼식이 등 다양한 별명으로 불린다.\n\n연안에 서식하면 갈색을, 깊은 곳에 서식하면 붉은색을 띈다.\n\n등지느러미에 독가시가 있어서 쏘이지 않게 조심해야 한다.")
                21 -> addCollection(collectionList, "양태", R.drawable.yangtae, "뼈가 억세고 머리가 커서 먹을 살이 없기에 잡어로 취급한다.\n\n작은 사이즈는 망둥어와 비슷한 생김새로 보일 때도 있으나 성체는 60cm까지 자랄 정도로 크다.")
                22 -> addCollection(collectionList, "열기", R.drawable.yulgi, "볼락과에 속하며 붉은색을 띄어서 불볼락이라고도 불린다.\n\n볼락보다 깊은 수심에 서식하며\n크기도 더 크다.\n\n동해, 남해에서 겨울에 주로 낚인다.")
                23 -> addCollection(collectionList, "용치놀래기", R.drawable.yongchi, "여름철 남해안에 출현하며, 제주도에서는 자주 낚을 수 있다.\n\n색깔이 화려하고 예쁘지만 맛은 기대이하라는 평이 많다.\n\n잡어로 분류한다는 낚시꾼도 많다.")
                24 -> addCollection(collectionList, "우럭", R.drawable.uruck, "동아시아에만 서식하는 어종이며\n정식 명칭은 조피볼락이다.\n\n한반도 전체 얕은 바다에 서식하여 가장 친근하게 낚을 수 있어 전 국민의 횟감으로도 유명하다.")
                25 -> addCollection(collectionList, "전어", R.drawable.juna, "집 나간 며느리도 돌아온다는 가을 전어로도 유명하다.\n\n대부분 밥상에 올라가는 전어는 양식이지만, 낚시로도 흔하게 잡을 수 있다.")
                26 -> addCollection(collectionList, "주꾸미", R.drawable.jukkumi, "문어과에 속하며 팔도 8개지만, 크기는 현저히 작다.\n\n다른 연체류보다 크기에 비해 많은양의 타우린이 포함되어 있어 건강식으로도 유명하다.\n\n쭈꾸미라고 부르는 사람이 더 많지만 주꾸미가 표준어다.")
                27 -> addCollection(collectionList, "쥐치", R.drawable.jwichi, "복어의 친척이지만 독이 없어서 식용으로 많이 쓰인다.\n\n흔히 먹는 쥐포를 쥐치로 만든다.\n\n입이 정말 작아서 일반적인 낚시 바늘로는 잡기 어렵다.")
                28 -> addCollection(collectionList, "참돔", R.drawable.chamdom, "4대 돔 중에서 제일 큰 어종이다.\n\n동아시아부터 태평양까지 서식하며\n한국에서는 특히 남해에서 많이 잡힌다.\n\n나름 고급 어종이지만 양식이 잘 되어 횟집에서 흔히 볼 수 있다.")
                29 -> addCollection(collectionList, "홍어", R.drawable.honga, "암모니아 냄새가 유명해 불호하는 사람이 정말 많지만, 매니아층에게는 마리 당 30~150만원에 판매되는 값비싼 고급 어종이다.\n\n삭힌 음식의 대명사로 불리지만, 한국에서만 삭혀 먹는다고 한다.")

            }

            list.add(collection)

        }

        return list

    } // checkCollection()


    fun addCollection(list: ArrayList<Collection>, fish: String, image: Int, explane: String) {

        for (j in 0 until list.size) {
            if (list[j].fishName == fish) {

                val date : Long = (list[j].date).toLong()

                collection = Collection(
                    list[j].nickname,
                    list[j].fishName,
                    image,
                    GetDate().getFormatDate5(date),
                    explane
                )
                break
            }
        }

    } // addCollection()


    fun refreshHistoryList(historyList: ArrayList<History>, nickname: String, fishName: String, date : String) : ArrayList<History> {

        val list = ArrayList<History>()
        var formatDate = ""

        if (fishName != "전 체" && date == "전 체") {

            for (i in 0 until historyList.size) {

                if (historyList[i].nickname == nickname && historyList[i].fishName == fishName) {

                    list.add(historyList[i])

                }

            }

        } else if (fishName != "전 체" && date != "전 체") {

            for (i in 0 until historyList.size) {

                formatDate = historyList[i].date.substring(2,4) + "." + historyList[i].date.substring(5,7) + "." + historyList[i].date.substring(8,10)

                if (historyList[i].nickname == nickname && historyList[i].fishName == fishName && formatDate == date) {

                    list.add(historyList[i])

                }

            }

        } else if (fishName == "전 체" && date == "전 체") {

            for (i in 0 until historyList.size) {

                if (historyList[i].nickname == nickname) {

                    list.add(historyList[i])

                }

            }

        } else if (fishName == "전 체" && date != "전 체"){

            for (i in 0 until historyList.size) {

                formatDate = historyList[i].date.substring(2,4) + "." + historyList[i].date.substring(5,7) + "." + historyList[i].date.substring(8,10)

                if (historyList[i].nickname == nickname && formatDate == date) {

                    list.add(historyList[i])

                }

            }

        }

        return list

    } // changeFish()


    @SuppressLint("SimpleDateFormat")
    fun getCalendarList(historyList : ArrayList<History>) : ArrayList<CalendarDay> {

        val list = ArrayList<CalendarDay>()
        val format = SimpleDateFormat("yyyy.MM.dd")

        for (i in 0 until historyList.size) {

            var date = format.parse(historyList[i].date)
            var calendar = Calendar.getInstance()

            calendar.time = date
            calendar.add(Calendar.MONTH, 1)

            val calendarDay = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))

            list.add(calendarDay)

        }

        return list

    } // getCalendarList()


    fun requestDeleteAccount(nickname : String, type : String) = webServerRetrofitInterface.deleteUserInfo(nickname, type)

    fun requestCombine(nickname: String) = webServerRetrofitInterface.requestDB(nickname)

    fun requestUpdateProfileImage(file : MultipartBody.Part, nickname : String) = webServerRetrofitInterface.updateProfileImage(file, nickname)


}