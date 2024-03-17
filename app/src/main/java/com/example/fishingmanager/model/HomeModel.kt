package com.example.fishingmanager.model

import android.util.Log
import com.example.fishingmanager.R
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.HomeRecommend
import com.example.fishingmanager.data.HomeWeather
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import java.util.Random

class HomeModel {

    private val weatherRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWeatherAPI().create(RetrofitInterface::class.java)

    private val indexRetrofitInterface: RetrofitInterface =
        RetrofitClient.getIndexAPI().create(RetrofitInterface::class.java)

    private val webServerRetrofitInterface: RetrofitInterface =
        RetrofitClient.getWebServer().create(RetrofitInterface::class.java)


    // 날씨 데이터
    fun getWeather(weatherList: ArrayList<ConditionWeather>, location: String): HomeWeather {

        val currentDate = GetDate().getFormatDate2(GetDate().getTime())
        val currentTime = GetDate().getFormatTime(GetDate().getTime())
        var skyImage = 0
        var temp = ""
        var humidity = ""
        var windSpeed = ""

        for (i in 0 until weatherList.size) {

            if (weatherList[i].date == currentDate && weatherList[i].time == currentTime) {

                skyImage = weatherList[i].skyImage
                temp = weatherList[i].temp
                humidity = weatherList[i].humidity
                windSpeed = weatherList[i].windSpeed

            }

        }

        return HomeWeather(location, skyImage, temp, humidity, windSpeed)

    } // getWeather()
    

    // 추천 어종 목록
    fun getRecommendList(indexList: ArrayList<Index.Item>): ArrayList<HomeRecommend>{
        
        val veryGoodList = ArrayList<HomeRecommend>()

        for (i in 0 until indexList.size) {

            if (indexList[i].total_score == "매우좋음") {

                if (indexList[i].date == GetDate().getDaysLater(0) || indexList[i].date == GetDate().getDaysLater(1) || indexList[i].date == GetDate().getDaysLater(2)) {

                    val date = indexList[i].date.substring(5,7) + " / " + indexList[i].date.substring(8,10) + GetDate().getDayOfWeek(indexList[i].date)
                    val location = indexList[i].name + " - " + indexList[i].time_type
                    val fishName = indexList[i].fish_name
                    val fishImage = getFishImage(fishName)

                    veryGoodList.add(HomeRecommend(date, location, fishName, fishImage))

                }

            }
            
        }

        if (veryGoodList.size == 0) {

            for (i in 0 until indexList.size) {

                if (indexList[i].total_score == "좋음") {

                    if (indexList[i].date == GetDate().getDaysLater(0) || indexList[i].date == GetDate().getDaysLater(1) || indexList[i].date == GetDate().getDaysLater(2)) {

                        val date = indexList[i].date.substring(5,7) + " / " + indexList[i].date.substring(8,10) + GetDate().getDayOfWeek(indexList[i].date)
                        val location = indexList[i].name + " - " + indexList[i].time_type
                        val fishName = indexList[i].fish_name
                        val fishImage = getFishImage(fishName)

                        veryGoodList.add(HomeRecommend(date, location, fishName, fishImage))

                    }

                }

            }

        }

        val veryGoodListSize = veryGoodList.size
        var list = ArrayList<HomeRecommend>()
        val random = Random()
        var randomBound = veryGoodListSize

        if (veryGoodListSize >= 10) {

            for (i in 0..9) {

                val result = random.nextInt(randomBound)

                list.add(veryGoodList[result])
                veryGoodList.removeAt(result)
                randomBound--

            }

        } else {

            list = veryGoodList

        }
        
        return list
        
    } // getRecommendList()


    // 물고기 이미지
    private fun getFishImage(fishName: String): Int {

        var fishImage = 0

        when (fishName) {

            "우럭" -> fishImage = R.drawable.uruck
            "광어" -> fishImage = R.drawable.gawnga
            "볼락" -> fishImage = R.drawable.bollock
            "농어" -> fishImage = R.drawable.nonga
            "열기" -> fishImage = R.drawable.yulgi
            "참돔" -> fishImage = R.drawable.chamdom
            "감성돔" -> fishImage = R.drawable.gamsungdom
            "돌돔" -> fishImage = R.drawable.doldom
            "벵에돔" -> fishImage = R.drawable.bengedom

        }

        return fishImage

    } // getFishImage()


    // 최근 잡은 물고기 목록
    fun getRecentCollectionList(historyList : ArrayList<History>) : ArrayList<History> {

        val list = ArrayList<History>()

        for (i in 0 until historyList.size) {

            list.add(historyList[i])

            if (list.size == 10) {
                break
            }

        }

        return list

    } // getRecentCollectionList()


    // Hot 게시글 목록
    fun getHotFeedList(feedList: ArrayList<Feed>): ArrayList<Feed> {

        val list = ArrayList<Feed>()
        var parseDate : String
        var date : String
        var feed : Feed
        var feedImage : String = ""

        for(i in 0 until feedList.size) {

            parseDate = feedList[i].date
            date = GetDate().getFormatDate3(parseDate.toLong())

            if (feedList[i].feedImage == null) {
                feedImage = ""
            }

            feed = Feed(feedList[i].nickname, feedList[i].feedNum, feedList[i].title, feedList[i].content, feedImage, feedList[i].viewCount, date, feedList[i].profileImage)
            list.add(feed)

            if (list.size == 10) {

                break

            }

        }

        return list

    } // getHotFeedList()


    // Retrofit) Weather 데이터 요청
    fun requestWeather(
        pageNo: String,
        numOfRows: String,
        dataType: String,
        baseDate: String,
        baseTime: String,
        nx: String,
        ny: String
    ) = weatherRetrofitInterface.requestWeather(pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny)


    // Retrofit) Index 데이터 요청
    fun requestIndex(type: String, resultType: String) = indexRetrofitInterface.requestIndex(type, resultType)


    // Retrofit) Combine 데이터 요청
    fun requestCombine(nickname: String) = webServerRetrofitInterface.requestDB(nickname)


}